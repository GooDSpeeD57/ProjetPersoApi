package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name = "ligne_facture") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LigneFacture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ligne") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facture", nullable = false) private Facture facture;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_prix", nullable = false) private ProduitPrix prix;
    @Column(nullable = false) private Integer quantite;
    @Column(name = "prix_unitaire", nullable = false) private BigDecimal prixUnitaire;
}
