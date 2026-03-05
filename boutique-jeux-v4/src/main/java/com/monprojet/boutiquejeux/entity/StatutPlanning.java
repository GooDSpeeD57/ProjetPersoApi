package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name = "statut_planning") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatutPlanning {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_statut_planning") private Long id;
    @Column(nullable = false, unique = true, length = 50) private String code;
}
