package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name = "produit_prix") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProduitPrix {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prix") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_statut_produit", nullable = false) private StatutProduit statutProduit;
    @Column(nullable = false) private BigDecimal prix;
}
