package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "magasin") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Magasin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_magasin") private Long id;
    @Column(nullable = false, length = 100) private String nom;
    private String telephone;
}
