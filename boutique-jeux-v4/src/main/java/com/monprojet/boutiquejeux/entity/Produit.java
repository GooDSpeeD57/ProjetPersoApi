package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity @Table(name = "produit")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Produit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit") private Long id;
    @Column(nullable = false)    private String nom;
    @Column(columnDefinition = "TEXT") private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categorie", nullable = false) private Categorie categorie;
    @Column(name = "date_sortie")  private LocalDate dateSortie;
    private String editeur;
    private String constructeur;
    private Integer pegi;
    private String plateforme;
    @Column(nullable = false) @Builder.Default private Boolean actif = true;
    @Column(nullable = false) @Builder.Default private Boolean deleted = false;
    @Column(name = "niveau_acces_min", nullable = false, length = 20) @Builder.Default private String niveauAccesMin = "NORMAL";
    @Column(nullable = false, length = 10) @Builder.Default private String langue = "fr";
    // V9 : numéro de série obligatoire pour garantie (CONSOLE, ACCESSOIRE)
    @Column(name = "necessite_numero_serie", nullable = false) @Builder.Default private Boolean necessiteNumeroSerie = false;
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProduitImage> images;
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProduitVideo> videos;
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProduitPrix> prix;
    @CreatedDate      @Column(name = "date_creation",     nullable = false, updatable = false) private LocalDateTime dateCreation;
    @LastModifiedDate @Column(name = "date_modification", nullable = false)                    private LocalDateTime dateModification;
}
