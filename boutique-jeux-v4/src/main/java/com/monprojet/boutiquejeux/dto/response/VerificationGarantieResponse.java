package com.monprojet.boutiquejeux.dto.response;
import lombok.*;
import java.time.LocalDate;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VerificationGarantieResponse {
    private Boolean   valide;
    private String    message;
    private String    numeroSerie;
    private LocalDate dateFin;     // null si invalide
}
