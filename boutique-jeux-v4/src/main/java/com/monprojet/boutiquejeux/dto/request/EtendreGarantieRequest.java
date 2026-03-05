package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor
public class EtendreGarantieRequest {
    @NotNull private Long garantieId;
    @NotNull private Long typeGarantieId;
}
