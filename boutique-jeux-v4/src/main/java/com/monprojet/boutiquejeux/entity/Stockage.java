package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "stockage") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Stockage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stockage") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_magasin", nullable = false) private Magasin magasin;
    @Column(nullable = false) @Builder.Default private Integer quantite = 0;
}
