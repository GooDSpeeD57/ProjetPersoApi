package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class EnregistrerGarantieRequest {
    @NotNull  private Long   produitId;
    @NotNull  private Long   factureId;
    @NotBlank private String numeroSerie;
    @NotNull @Min(1) private Integer dureeMois;
}
