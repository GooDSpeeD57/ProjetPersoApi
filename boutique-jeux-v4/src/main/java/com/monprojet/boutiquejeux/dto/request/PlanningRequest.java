package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
public record PlanningRequest(
    @NotNull Long employeId,
    @NotNull Long statutPlanningId,
    @NotNull LocalDate dateTravail,
    @NotNull LocalTime heureDebut,
    @NotNull LocalTime heureFin
) {}
