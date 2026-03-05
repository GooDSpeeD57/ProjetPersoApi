package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.*;
public record AjoutPanierRequest(
    @NotNull Long produitId,
    @NotNull Long prixId,
    @NotNull @Min(1) Integer quantite
) {}
