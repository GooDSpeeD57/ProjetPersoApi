package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name = "abonnement_client") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AbonnementClient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abonnement") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false) private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_statut_abonnement", nullable = false) private StatutAbonnement statutAbonnement;
    @Column(name = "date_debut",   nullable = false) private LocalDate dateDebut;
    @Column(name = "date_fin",     nullable = false) private LocalDate dateFin;
    @Column(name = "montant_paye", nullable = false) private BigDecimal montantPaye;
    @Column(name = "date_paiement", nullable = false) @Builder.Default private LocalDateTime datePaiement = LocalDateTime.now();
    @Column(name = "renouvellement_auto", nullable = false) @Builder.Default private Boolean renouvellementAuto = true;
    @Column(name = "date_resiliation") private LocalDateTime dateResiliation;
}
