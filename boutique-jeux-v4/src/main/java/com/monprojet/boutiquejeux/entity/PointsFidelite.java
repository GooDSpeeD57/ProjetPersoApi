package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name = "points_fidelite")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PointsFidelite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_points_fidelite") private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false, unique = true) private Client client;
    @Column(name = "solde_points",         nullable = false) @Builder.Default private Integer soldePoints = 0;
    @Column(name = "total_achats_annuel",  nullable = false) @Builder.Default private BigDecimal totalAchatsAnnuel = BigDecimal.ZERO;
    @Column(name = "date_debut_periode",   nullable = false) private LocalDate dateDebutPeriode;
    @LastModifiedDate
    @Column(name = "date_modification", nullable = false) private LocalDateTime dateModification;
}
