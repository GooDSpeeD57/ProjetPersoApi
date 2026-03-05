package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name = "type_fidelite") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeFidelite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_fidelite") private Long id;
    @Column(nullable = false, unique = true, length = 50) private String code;
    private String description;
    @Column(name = "points_par_euro",    nullable = false) private BigDecimal pointsParEuro;
    @Column(name = "seuil_upgrade_euro")                   private BigDecimal seuilUpgradeEuro;
    @Column(name = "prix_abonnement")                      private BigDecimal prixAbonnement;
}
