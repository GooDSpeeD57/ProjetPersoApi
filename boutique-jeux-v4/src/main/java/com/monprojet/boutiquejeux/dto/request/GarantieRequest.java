package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record GarantieRequest(
    @NotNull  Long    produitId,
    @NotNull  Long    factureId,
    @NotBlank String  numeroSerie,
    @NotNull  Integer dureeMois
) {}
