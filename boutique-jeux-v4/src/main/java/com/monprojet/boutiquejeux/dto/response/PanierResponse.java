package com.monprojet.boutiquejeux.dto.response;
import java.math.BigDecimal;
import java.util.List;
public record PanierResponse(Long id, String statut, List<LignePanierResponse> lignes, BigDecimal total) {}
