package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "categorie") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Categorie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie") private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_categorie", nullable = false) private TypeCategorie typeCategorie;
    @Column(nullable = false, unique = true, length = 100) private String nom;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false) @Builder.Default private Boolean actif = true;
}
