package com.monprojet.boutiquejeux.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
public record AbonnementResponse(Long id, String statut, LocalDate dateDebut, LocalDate dateFin, BigDecimal montantPaye, Boolean renouvellementAuto, long joursRestants) {}
