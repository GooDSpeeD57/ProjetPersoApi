package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "type_reduction") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeReduction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_reduction") private Long id;
    @Column(nullable = false, unique = true, length = 50) private String code;
}
