package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name = "ligne_panier") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LignePanier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ligne_panier") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_panier", nullable = false) private Panier panier;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_prix", nullable = false) private ProduitPrix prix;
    @Column(nullable = false) private Integer quantite;
    @Column(name = "prix_unitaire", nullable = false) private BigDecimal prixUnitaire;
}
