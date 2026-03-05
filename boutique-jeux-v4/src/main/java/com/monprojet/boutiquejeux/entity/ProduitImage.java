package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name = "produit_image") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProduitImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @Column(nullable = false) private String url;
    @Column(nullable = false) @Builder.Default private String alt = "";
    @Column(nullable = false) @Builder.Default private Boolean decorative = false;
    @Column(nullable = false) @Builder.Default private Boolean principale = false;
    @Column(nullable = false) @Builder.Default private Integer ordre = 0;
    @Column(name = "date_creation", nullable = false, updatable = false)
    @Builder.Default private LocalDateTime dateCreation = LocalDateTime.now();
}
