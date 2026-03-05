package com.monprojet.boutiquejeux.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ExtensionGarantieResponse {
    private Long          id;
    private String        typeGarantie;
    private BigDecimal    prixExtension;
    private LocalDateTime dateAchat;
    private LocalDate     dateFinEtendue;
}
