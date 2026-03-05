package com.monprojet.boutiquejeux.dto.response;
import java.math.BigDecimal;
public record LignePanierResponse(Long id, Long produitId, String produitNom, String statutProduit, Integer quantite, BigDecimal prixUnitaire, BigDecimal sousTotal) {}
