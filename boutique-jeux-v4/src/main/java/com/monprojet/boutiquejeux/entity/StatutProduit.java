package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "statut_produit") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatutProduit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_produit") private Long id;
    @Column(nullable = false, unique = true, length = 50) private String code;
}
