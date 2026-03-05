package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
@Entity @Table(name = "avis_produit")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AvisProduit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avis") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false) private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false) private Produit produit;
    @Column(nullable = false) private Byte note;
    @Column(columnDefinition = "TEXT") private String commentaire;
    @CreatedDate      @Column(name = "date_creation",     nullable = false, updatable = false) private LocalDateTime dateCreation;
    @LastModifiedDate @Column(name = "date_modification", nullable = false)                    private LocalDateTime dateModification;
}
