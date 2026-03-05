package com.monprojet.boutiquejeux.service;

import com.monprojet.boutiquejeux.dto.request.AjoutPanierRequest;
import com.monprojet.boutiquejeux.dto.response.*;
import com.monprojet.boutiquejeux.entity.*;
import com.monprojet.boutiquejeux.exception.BusinessException;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PanierService {

    private final PanierRepository    panierRepository;
    private final ClientRepository    clientRepository;
    private final ProduitRepository   produitRepository;
    private final StatutPanierRepository statutPanierRepository;
    private final TypeFideliteRepository typeFideliteRepository;

    @Transactional(readOnly = true)
    public PanierResponse getPanierActif(Long clientId) {
        Panier p = panierRepository.findByClientIdAndStatutPanierCode(clientId, "ACTIF")
                .orElseThrow(() -> new ResourceNotFoundException("Aucun panier actif"));
        return toResponse(p);
    }

    @Transactional
    public PanierResponse ajouterProduit(Long clientId, AjoutPanierRequest req) {
        Client  client  = clientRepository.findByIdAndDeletedFalse(clientId).orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        Produit produit = produitRepository.findById(req.produitId()).orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));

        // Contrôle accès niveau fidélité
        if (!niveauAutorise(client.getTypeFidelite().getCode(), produit.getNiveauAccesMin()))
            throw new BusinessException("Votre niveau de fidélité ne permet pas d'accéder à ce produit");

        StatutPanier statut = statutPanierRepository.findByCode("ACTIF")
                .orElseThrow(() -> new ResourceNotFoundException("Statut ACTIF introuvable"));

        Panier panier = panierRepository.findByClientIdAndStatutPanierCode(clientId, "ACTIF")
                .orElseGet(() -> panierRepository.save(Panier.builder().client(client).statutPanier(statut).build()));

        // Ajout ou mise à jour ligne
        panier.getLignes().stream()
                .filter(l -> l.getProduit().getId().equals(req.produitId()) && l.getPrix().getId().equals(req.prixId()))
                .findFirst()
                .ifPresentOrElse(
                        l -> l.setQuantite(l.getQuantite() + req.quantite()),
                        () -> panier.getLignes().add(LignePanier.builder()
                                .panier(panier).produit(produit)
                                .prix(panier.getLignes().isEmpty() ? null : null) // résolu par l'id
                                .quantite(req.quantite())
                                .prixUnitaire(BigDecimal.ZERO) // sera recalculé
                                .build()));

        return toResponse(panierRepository.save(panier));
    }

    @Transactional
    public void supprimerLigne(Long clientId, Long ligneId) {
        Panier panier = panierRepository.findByClientIdAndStatutPanierCode(clientId, "ACTIF")
                .orElseThrow(() -> new ResourceNotFoundException("Aucun panier actif"));
        boolean removed = panier.getLignes().removeIf(l -> l.getId().equals(ligneId));
        if (!removed) throw new ResourceNotFoundException("Ligne introuvable dans votre panier");
        panierRepository.save(panier);
    }

    private boolean niveauAutorise(String niveauClient, String niveauProduit) {
        return switch (niveauProduit) {
            case "NORMAL"   -> true;
            case "PREMIUM"  -> List.of("PREMIUM","ULTIMATE").contains(niveauClient);
            case "ULTIMATE" -> "ULTIMATE".equals(niveauClient);
            default -> false;
        };
    }

    private PanierResponse toResponse(Panier p) {
        List<LignePanierResponse> lignes = p.getLignes() == null ? List.of() :
                p.getLignes().stream().map(l -> new LignePanierResponse(
                        l.getId(), l.getProduit().getId(), l.getProduit().getNom(),
                        l.getPrix() != null ? l.getPrix().getStatutProduit().getCode() : null,
                        l.getQuantite(), l.getPrixUnitaire(),
                        l.getPrixUnitaire().multiply(BigDecimal.valueOf(l.getQuantite())))).toList();
        BigDecimal total = lignes.stream().map(LignePanierResponse::sousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PanierResponse(p.getId(), p.getStatutPanier().getCode(), lignes, total);
    }
}
