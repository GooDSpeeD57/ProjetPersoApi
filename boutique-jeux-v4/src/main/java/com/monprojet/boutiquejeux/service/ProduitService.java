package com.monprojet.boutiquejeux.service;

import com.monprojet.boutiquejeux.dto.response.*;
import com.monprojet.boutiquejeux.entity.*;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProduitService {

    private final ProduitRepository      produitRepository;
    private final AvisProduitRepository  avisRepository;

    public Page<ProduitResponse> rechercher(String search, Long categorieId, String plateforme, String niveauAcces, Pageable pageable) {
        return produitRepository.searchProduits(search, categorieId, plateforme, niveauAcces, pageable)
                .map(this::toResponse);
    }

    public ProduitResponse findById(Long id) {
        Produit p = produitRepository.findById(id)
                .filter(pr -> !pr.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable : " + id));
        return toResponse(p);
    }

    public Page<AvisResponse> getAvis(Long produitId, Pageable pageable) {
        return avisRepository.findByProduitIdOrderByDateCreationDesc(produitId, pageable)
                .map(a -> new AvisResponse(a.getId(), a.getClient().getPseudo(),
                        a.getNote().intValue(), a.getCommentaire(), a.getDateCreation()));
    }

    private ProduitResponse toResponse(Produit p) {
        BigDecimal prixNeuf     = getPrix(p, "NEUF");
        BigDecimal prixOccasion = getPrix(p, "OCCASION");
        ProduitImage img = p.getImages() == null ? null :
                p.getImages().stream().filter(ProduitImage::getPrincipale).findFirst().orElse(
                p.getImages().isEmpty() ? null : p.getImages().getFirst());
        Double note  = avisRepository.findNoteMoyenneByProduitId(p.getId());
        long nbAvis  = avisRepository.countByProduitId(p.getId());
        List<ImageResponse> images = p.getImages() == null ? List.of() :
                p.getImages().stream()
                        .map(i -> new ImageResponse(i.getId(), i.getUrl(), i.getAlt(), i.getDecorative(), i.getPrincipale(), i.getOrdre()))
                        .toList();
        return new ProduitResponse(
                p.getId(), p.getNom(), p.getDescription(), p.getPlateforme(), p.getPegi(),
                p.getCategorie().getNom(), p.getCategorie().getTypeCategorie().getCode(),
                p.getNiveauAccesMin(), p.getNecessiteNumeroSerie(),
                prixNeuf, prixOccasion,
                img != null ? img.getUrl() : null,
                img != null ? img.getAlt() : null,
                images, note, nbAvis);
    }

    private BigDecimal getPrix(Produit p, String statut) {
        if (p.getPrix() == null) return null;
        return p.getPrix().stream()
                .filter(pr -> pr.getStatutProduit().getCode().equals(statut))
                .map(ProduitPrix::getPrix)
                .findFirst().orElse(null);
    }
}
