package com.monprojet.boutiquejeux.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
public record PointsFideliteResponse(Integer soldePoints, BigDecimal totalAchatsAnnuel, LocalDate dateDebutPeriode, String niveauFidelite) {}
