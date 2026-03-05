package com.monprojet.boutiquejeux.service;

import com.monprojet.boutiquejeux.dto.request.ExtensionGarantieRequest;
import com.monprojet.boutiquejeux.dto.request.GarantieRequest;
import com.monprojet.boutiquejeux.dto.response.GarantieResponse;
import com.monprojet.boutiquejeux.entity.*;
import com.monprojet.boutiquejeux.exception.BusinessException;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GarantieService {

    private final GarantieRepository       garantieRepository;
    private final ProduitRepository        produitRepository;
    private final FactureRepository        factureRepository;
    private final TypeGarantieRepository   typeGarantieRepository;

    @Transactional
    public GarantieResponse enregistrer(GarantieRequest req) {
        if (garantieRepository.existsByNumeroSerie(req.numeroSerie()))
            throw new BusinessException("Ce numéro de série est déjà enregistré");

        Produit produit = produitRepository.findById(req.produitId()).orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        Facture facture = factureRepository.findById(req.factureId()).orElseThrow(() -> new ResourceNotFoundException("Facture introuvable"));

        if (produit.getNecessiteNumeroSerie() && (req.numeroSerie() == null || req.numeroSerie().isBlank()))
            throw new BusinessException("Ce produit nécessite un numéro de série pour la garantie");

        LocalDate debut = LocalDate.now();
        LocalDate fin   = debut.plusMonths(req.dureeMois());

        Garantie garantie = Garantie.builder()
                .produit(produit).facture(facture)
                .numeroSerie(req.numeroSerie())
                .dateDebut(debut).dateFin(fin)
                .build();

        return toResponse(garantieRepository.save(garantie));
    }

    @Transactional
    public GarantieResponse etendre(ExtensionGarantieRequest req) {
        Garantie    garantie = garantieRepository.findById(req.garantieId()).orElseThrow(() -> new ResourceNotFoundException("Garantie introuvable"));
        TypeGarantie type    = typeGarantieRepository.findById(req.typeGarantieId()).orElseThrow(() -> new ResourceNotFoundException("Type de garantie introuvable"));

        LocalDate nouvelleDateFin = garantie.getDateFin().plusMonths(type.getDureeMois());

        ExtensionGarantie extension = ExtensionGarantie.builder()
                .garantie(garantie).typeGarantie(type)
                .dateFinEtendue(nouvelleDateFin)
                .build();

        // Le trigger trg_extension_garantie_update met à jour date_fin sur garantie côté BDD
        // On le fait aussi côté Java pour cohérence de la réponse immédiate
        garantie.setDateFin(nouvelleDateFin);
        garantie.setEstEtendue(true);
        garantie.getExtensions().add(extension);

        return toResponse(garantieRepository.save(garantie));
    }

    @Transactional(readOnly = true)
    public GarantieResponse verifier(String numeroSerie, Long produitId) {
        return garantieRepository.findByNumeroSerie(numeroSerie)
                .filter(g -> g.getProduit().getId().equals(produitId))
                .filter(g -> !g.getDateFin().isBefore(LocalDate.now()))
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Numéro de série invalide ou garantie expirée"));
    }

    private GarantieResponse toResponse(Garantie g) {
        Facture f = g.getFacture();
        String client      = f.getClient() != null ? f.getClient().getPseudo() : f.getNomClient();
        String clientEmail = f.getClient() != null ? f.getClient().getEmail()  : f.getEmailClient();
        return new GarantieResponse(g.getId(), g.getNumeroSerie(), g.getProduit().getNom(),
                g.getDateDebut(), g.getDateFin(), g.getEstEtendue(), g.getDateExtension(),
                client, clientEmail, f.getContexteVente().name());
    }
}
