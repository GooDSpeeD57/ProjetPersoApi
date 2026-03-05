package com.monprojet.boutiquejeux.service;
import com.monprojet.boutiquejeux.dto.response.*;
import com.monprojet.boutiquejeux.entity.Facture;
import com.monprojet.boutiquejeux.exception.BusinessException;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.FactureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class FactureService {
    private final FactureRepository factureRepository;
    public Page<FactureResponse> getFacturesClient(Long clientId, Pageable pageable) {
        return factureRepository.findAllByClientId(clientId, pageable).map(this::toResponse);
    }
    public FactureResponse getFactureById(Long factureId, Long clientId) {
        Facture f = factureRepository.findById(factureId).orElseThrow(() -> new ResourceNotFoundException("Facture introuvable"));
        if (f.getClient() == null || !f.getClient().getId().equals(clientId)) throw new BusinessException("Accès refusé à cette facture");
        return toResponse(f);
    }
    public Page<FactureResponse> getFacturesMagasin(Long magasinId, Pageable pageable) {
        return factureRepository.findAllByMagasinId(magasinId, pageable).map(this::toResponse);
    }
    private FactureResponse toResponse(Facture f) {
        // V9 : client peut être null (facture anonyme) → COALESCE côté Java
        String clientAffiche = f.getClient() != null ? f.getClient().getPseudo() : f.getNomClient();
        String emailAffiche  = f.getClient() != null ? f.getClient().getEmail()  : f.getEmailClient();
        List<LigneFactureResponse> lignes = f.getLignes() == null ? List.of() :
                f.getLignes().stream().map(l -> new LigneFactureResponse(l.getId(), l.getProduit().getId(), l.getProduit().getNom(), l.getPrix().getStatutProduit().getCode(), l.getQuantite(), l.getPrixUnitaire(), l.getPrixUnitaire().multiply(BigDecimal.valueOf(l.getQuantite())))).toList();
        return new FactureResponse(f.getId(), f.getDateFacture(), clientAffiche, emailAffiche, f.getMagasin().getNom(), f.getModePaiement().getCode(), f.getEmploye() != null ? f.getEmploye().getPrenom() + " " + f.getEmploye().getNom() : null, f.getMontantTotal(), f.getMontantRemise(), f.getMontantFinal(), f.getContexteVente().name(), lignes);
    }
}
