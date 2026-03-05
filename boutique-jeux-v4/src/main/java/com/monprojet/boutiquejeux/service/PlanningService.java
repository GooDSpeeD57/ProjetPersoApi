package com.monprojet.boutiquejeux.service;
import com.monprojet.boutiquejeux.dto.request.PlanningRequest;
import com.monprojet.boutiquejeux.dto.response.PlanningResponse;
import com.monprojet.boutiquejeux.entity.*;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
@Service @RequiredArgsConstructor
public class PlanningService {
    private final PlanningEmployeRepository planningRepository;
    private final EmployeRepository employeRepository;
    private final StatutPlanningRepository statutPlanningRepository;
    public List<PlanningResponse> getPlanningEmploye(Long employeId, LocalDate debut, LocalDate fin) {
        return planningRepository.findAllByEmployeIdAndDateTravailBetween(employeId, debut, fin).stream().map(this::toResponse).toList();
    }
    @Transactional
    public PlanningResponse creer(PlanningRequest req) {
        Employe employe = employeRepository.findByIdAndDeletedFalse(req.employeId()).orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));
        StatutPlanning statut = statutPlanningRepository.findById(req.statutPlanningId()).orElseThrow(() -> new ResourceNotFoundException("Statut planning introuvable"));
        PlanningEmploye p = PlanningEmploye.builder().employe(employe).statutPlanning(statut).dateTravail(req.dateTravail()).heureDebut(req.heureDebut()).heureFin(req.heureFin()).build();
        return toResponse(planningRepository.save(p));
    }
    @Transactional
    public void supprimer(Long id) {
        if (!planningRepository.existsById(id)) throw new ResourceNotFoundException("Créneau introuvable");
        planningRepository.deleteById(id);
    }
    private PlanningResponse toResponse(PlanningEmploye p) { return new PlanningResponse(p.getId(), p.getEmploye().getPrenom() + " " + p.getEmploye().getNom(), p.getStatutPlanning().getCode(), p.getDateTravail(), p.getHeureDebut(), p.getHeureFin()); }
}
