package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.*;
public record AvisRequest(
    @NotNull @Min(1) @Max(5) Integer note,
    @Size(max=2000) String commentaire
) {}
