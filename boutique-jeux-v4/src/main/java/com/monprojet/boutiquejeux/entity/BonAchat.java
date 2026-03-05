package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name = "bon_achat") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BonAchat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bon_achat") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false) private Client client;
    @Column(name = "code_bon", nullable = false, unique = true, length = 50) private String codeBon;
    @Column(nullable = false) private BigDecimal valeur;
    @Column(name = "points_utilises", nullable = false) private Integer pointsUtilises;
    @Column(nullable = false) @Builder.Default private Boolean utilise = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facture") private Facture facture;
    @Column(name = "date_creation",    nullable = false, updatable = false) @Builder.Default private LocalDateTime dateCreation = LocalDateTime.now();
    @Column(name = "date_utilisation") private LocalDateTime dateUtilisation;
}
