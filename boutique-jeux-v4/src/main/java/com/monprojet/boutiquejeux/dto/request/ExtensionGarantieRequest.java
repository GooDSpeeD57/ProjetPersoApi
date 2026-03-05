package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.NotNull;
public record ExtensionGarantieRequest(
    @NotNull Long garantieId,
    @NotNull Long typeGarantieId
) {}
