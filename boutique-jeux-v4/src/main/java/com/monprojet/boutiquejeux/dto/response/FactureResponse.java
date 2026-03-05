package com.monprojet.boutiquejeux.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
public record FactureResponse(
    Long id, LocalDateTime dateFacture,
    String client, String clientEmail, String magasin,
    String modePaiement, String vendeur,
    BigDecimal montantTotal, BigDecimal montantRemise, BigDecimal montantFinal,
    String contexteVente, List<LigneFactureResponse> lignes
) {}
