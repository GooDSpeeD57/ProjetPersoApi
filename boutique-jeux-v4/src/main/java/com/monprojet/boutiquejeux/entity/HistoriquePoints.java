package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name = "historique_points") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HistoriquePoints {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false) private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facture") private Facture facture;
    @Column(name = "type_operation", nullable = false, length = 20) private String typeOperation;
    @Column(nullable = false) private Integer points;
    private String commentaire;
    @Column(name = "date_operation", nullable = false, updatable = false)
    @Builder.Default private LocalDateTime dateOperation = LocalDateTime.now();
}
