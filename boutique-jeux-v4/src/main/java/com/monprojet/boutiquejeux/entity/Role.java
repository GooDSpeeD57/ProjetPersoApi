package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "role") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role") private Long id;
    @Column(nullable = false, unique = true, length = 50) private String code;
}
