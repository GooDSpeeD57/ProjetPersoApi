package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "type_categorie") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeCategorie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_categorie") private Long id;
    @Column(nullable = false, unique = true, length = 50) private String code;
}
