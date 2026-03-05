package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name = "produit_video") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProduitVideo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_video") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @Column(nullable = false) private String url;
    @Column(nullable = false) private String titre;
    @Column(nullable = false) @Builder.Default private Integer ordre = 0;
    @Column(nullable = false, length = 10) @Builder.Default private String langue = "fr";
    @Column(name = "sous_titres_url") private String sousTitresUrl;
    @Column(name = "audio_desc_url")  private String audioDescUrl;
    @Column(columnDefinition = "TEXT") private String transcription;
    @Column(name = "date_creation", nullable = false, updatable = false)
    @Builder.Default private LocalDateTime dateCreation = LocalDateTime.now();
}
