package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name = "extension_garantie") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExtensionGarantie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extension") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_garantie", nullable = false) private Garantie garantie;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_garantie", nullable = false) private TypeGarantie typeGarantie;
    @Column(name = "date_achat",       nullable = false) @Builder.Default private LocalDateTime dateAchat = LocalDateTime.now();
    @Column(name = "date_fin_etendue", nullable = false) private LocalDate dateFinEtendue;
}
