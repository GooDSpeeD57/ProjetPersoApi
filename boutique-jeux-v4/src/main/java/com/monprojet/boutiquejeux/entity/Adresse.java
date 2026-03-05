package com.monprojet.boutiquejeux.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "adresse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adresse")
    private Long id;

    @Column(name = "id_client")
    private Long idClient;  // NULL si l'adresse appartient à un magasin

    @Column(name = "id_magasin")
    private Long idMagasin; // NULL si l'adresse appartient à un client

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_adresse", nullable = false)
    private TypeAdresse typeAdresse;

    @Column(nullable = false, length = 255)
    private String rue;

    @Column(nullable = false, length = 100)
    private String ville;

    @Column(nullable = false, length = 10, name = "code_postal")
    private String codePostal;

    @Column(nullable = false, length = 100)
    private String pays;
}
