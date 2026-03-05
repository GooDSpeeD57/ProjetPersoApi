package com.monprojet.boutiquejeux.service;

import com.monprojet.boutiquejeux.dto.request.AvisRequest;
import com.monprojet.boutiquejeux.dto.response.AvisResponse;
import com.monprojet.boutiquejeux.entity.*;
import com.monprojet.boutiquejeux.exception.BusinessException;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AvisService {

    private final AvisProduitRepository avisRepository;
    private final ClientRepository      clientRepository;
    private final ProduitRepository     produitRepository;
    private final FactureRepository     factureRepository;

    @Transactional
    public AvisResponse deposerAvis(Long clientId, Long produitId, AvisRequest req) {
        if (!factureRepository.clientAacheteProduit(clientId, produitId))
            throw new BusinessException("Vous devez avoir acheté ce produit pour laisser un avis");
        if (avisRepository.existsByClientIdAndProduitId(clientId, produitId))
            throw new BusinessException("Vous avez déjà laissé un avis pour ce produit");
        Client  client  = clientRepository.findByIdAndDeletedFalse(clientId).orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        Produit produit = produitRepository.findById(produitId).orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        AvisProduit avis = AvisProduit.builder()
                .client(client).produit(produit)
                .note(req.note().byteValue()).commentaire(req.commentaire())
                .build();
        avis = avisRepository.save(avis);
        return toResponse(avis);
    }

    @Transactional
    public AvisResponse modifierAvis(Long clientId, Long produitId, AvisRequest req) {
        AvisProduit avis = avisRepository.findByClientIdAndProduitId(clientId, produitId)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        avis.setNote(req.note().byteValue());
        avis.setCommentaire(req.commentaire());
        return toResponse(avisRepository.save(avis));
    }

    private AvisResponse toResponse(AvisProduit a) {
        return new AvisResponse(a.getId(), a.getClient().getPseudo(), a.getNote().intValue(), a.getCommentaire(), a.getDateCreation());
    }
}
