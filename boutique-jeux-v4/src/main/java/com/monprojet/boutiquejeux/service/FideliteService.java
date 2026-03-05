package com.monprojet.boutiquejeux.service;
import com.monprojet.boutiquejeux.dto.response.PointsFideliteResponse;
import com.monprojet.boutiquejeux.entity.PointsFidelite;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.PointsFideliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class FideliteService {
    private final PointsFideliteRepository pointsRepository;
    @Transactional(readOnly = true)
    public PointsFideliteResponse getPoints(Long clientId) {
        PointsFidelite pf = pointsRepository.findByClientId(clientId).orElseThrow(() -> new ResourceNotFoundException("Compte points introuvable"));
        return new PointsFideliteResponse(pf.getSoldePoints(), pf.getTotalAchatsAnnuel(), pf.getDateDebutPeriode(), pf.getClient().getTypeFidelite().getCode());
    }
}
