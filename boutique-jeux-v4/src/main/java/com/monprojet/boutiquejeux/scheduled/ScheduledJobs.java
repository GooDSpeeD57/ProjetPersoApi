package com.monprojet.boutiquejeux.scheduled;

import com.monprojet.boutiquejeux.entity.*;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledJobs {

    private final AbonnementClientRepository abonnementRepository;
    private final ClientRepository           clientRepository;
    private final TypeFideliteRepository     typeFideliteRepository;
    private final StatutAbonnementRepository statutAbonnementRepository;

    /** Chaque nuit à 2h00 : expire les abonnements ULTIMATE arrivés à terme */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void expireAbonnementsUltimate() {
        List<AbonnementClient> expires = abonnementRepository.findAbonnementsExpires(LocalDate.now());
        if (expires.isEmpty()) return;
        log.info("[SCHEDULED] Expiration ULTIMATE : {} abonnement(s)", expires.size());
        var statutExpire = statutAbonnementRepository.findByCode("EXPIRE").orElseThrow(() -> new ResourceNotFoundException("Statut EXPIRE introuvable"));
        TypeFidelite premium = typeFideliteRepository.findByCode("PREMIUM").orElseThrow(() -> new ResourceNotFoundException("Type PREMIUM introuvable"));
        for (AbonnementClient abo : expires) {
            abo.setStatutAbonnement(statutExpire);
            abonnementRepository.save(abo);
            Client client = abo.getClient();
            client.setTypeFidelite(premium);
            clientRepository.save(client);
            log.info("[SCHEDULED] Abonnement {} expiré → client {} → PREMIUM", abo.getId(), client.getEmail());
        }
    }

    /** Chaque nuit à 3h00 : anonymisation RGPD art.17 */
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void anonymiserClientsSuppression() {
        var page = clientRepository.findAllEnAttenteSupression(PageRequest.of(0, 100));
        if (page.isEmpty()) return;
        log.info("[SCHEDULED] Anonymisation RGPD : {} client(s)", page.getTotalElements());
        for (Client c : page) {
            String anon = "anonyme-" + c.getId();
            c.setPseudo(anon); c.setNom("SUPPRIME"); c.setPrenom("SUPPRIME");
            c.setEmail(anon + "@supprime.invalid");
            c.setTelephone(null); c.setDateNaissance(null);
            c.setMotDePasse("SUPPRIME"); c.setNumeroCarteFidelite(null);
            c.setRgpdConsentIp(null); c.setDeleted(true);
            clientRepository.save(c);
            log.info("[SCHEDULED] Client {} anonymisé (RGPD art.17)", c.getId());
        }
    }
}
