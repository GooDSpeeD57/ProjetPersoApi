package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name = "type_garantie") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeGarantie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_garantie") private Long id;
    @Column(nullable = false, unique = true, length = 50) private String code;
    private String description;
    @Column(name = "duree_mois", nullable = false) private Integer dureeMois;
    @Column(name = "prix_extension")               private BigDecimal prixExtension;
}
