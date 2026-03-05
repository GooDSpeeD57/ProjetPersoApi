package com.monprojet.boutiquejeux.dto.response;
import java.time.LocalDate;
import java.time.LocalTime;
public record PlanningResponse(Long id, String employe, String statut, LocalDate dateTravail, LocalTime heureDebut, LocalTime heureFin) {}
