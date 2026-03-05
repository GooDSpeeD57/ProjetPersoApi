package com.monprojet.boutiquejeux.dto.response;
import java.time.LocalDate;
public record GarantieResponse(
    Long id, String numeroSerie, String produit,
    LocalDate dateDebut, LocalDate dateFin,
    Boolean estEtendue, LocalDate dateExtension,
    String client, String clientEmail, String contexteVente
) {}
