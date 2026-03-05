package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Entity @Table(name = "garantie") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Garantie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_garantie") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facture", nullable = false) private Facture facture;
    @Column(name = "numero_serie", nullable = false, unique = true, length = 100) private String numeroSerie;
    @Column(name = "date_debut", nullable = false) private LocalDate dateDebut;
    @Column(name = "date_fin",   nullable = false) private LocalDate dateFin;
    // mis à jour automatiquement par le trigger trg_extension_garantie_update
    @Column(name = "est_etendue",   nullable = false) @Builder.Default private Boolean estEtendue = false;
    @Column(name = "date_extension")                                         private LocalDate dateExtension;
    @OneToMany(mappedBy = "garantie", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ExtensionGarantie> extensions;
}
