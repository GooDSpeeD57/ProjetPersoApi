package com.monprojet.boutiquejeux.service;
import com.monprojet.boutiquejeux.dto.response.AbonnementResponse;
import com.monprojet.boutiquejeux.entity.*;
import com.monprojet.boutiquejeux.exception.BusinessException;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
@Service @RequiredArgsConstructor
public class AbonnementService {
    private final AbonnementClientRepository abonnementRepository;
    private final ClientRepository clientRepository;
    private final TypeFideliteRepository typeFideliteRepository;
    private final StatutAbonnementRepository statutAbonnementRepository;
    @Transactional(readOnly = true)
    public AbonnementResponse getAbonnementActif(Long clientId) {
        AbonnementClient a = abonnementRepository.findAbonnementActif(clientId, LocalDate.now()).orElseThrow(() -> new ResourceNotFoundException("Aucun abonnement ULTIMATE actif"));
        return toResponse(a);
    }
    @Transactional
    public AbonnementResponse souscrire(Long clientId, Boolean renouvellementAuto) {
        abonnementRepository.findAbonnementActif(clientId, LocalDate.now()).ifPresent(a -> { throw new BusinessException("Abonnement ULTIMATE déjà actif"); });
        Client client = clientRepository.findByIdAndDeletedFalse(clientId).orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
        TypeFidelite ultimate = typeFideliteRepository.findByCode("ULTIMATE").orElseThrow(() -> new ResourceNotFoundException("Type ULTIMATE introuvable"));
        StatutAbonnement statut = statutAbonnementRepository.findByCode("ACTIF").orElseThrow(() -> new ResourceNotFoundException("Statut ACTIF introuvable"));
        client.setTypeFidelite(ultimate); clientRepository.save(client);
        AbonnementClient abo = AbonnementClient.builder().client(client).statutAbonnement(statut).dateDebut(LocalDate.now()).dateFin(LocalDate.now().plusYears(1)).montantPaye(ultimate.getPrixAbonnement()).renouvellementAuto(renouvellementAuto).build();
        return toResponse(abonnementRepository.save(abo));
    }
    @Transactional
    public AbonnementResponse toggleRenouvellement(Long clientId, Boolean actif) {
        AbonnementClient abo = abonnementRepository.findAbonnementActif(clientId, LocalDate.now()).orElseThrow(() -> new ResourceNotFoundException("Aucun abonnement actif"));
        abo.setRenouvellementAuto(actif);
        return toResponse(abonnementRepository.save(abo));
    }
    private AbonnementResponse toResponse(AbonnementClient a) {
        return new AbonnementResponse(a.getId(), a.getStatutAbonnement().getCode(), a.getDateDebut(), a.getDateFin(), a.getMontantPaye(), a.getRenouvellementAuto(), ChronoUnit.DAYS.between(LocalDate.now(), a.getDateFin()));
    }
}
