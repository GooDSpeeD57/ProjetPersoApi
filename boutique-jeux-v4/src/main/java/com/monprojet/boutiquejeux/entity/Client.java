package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name = "client")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client") private Long id;
    @Column(nullable = false, unique = true, length = 50)  private String pseudo;
    @Column(nullable = false, length = 100)                private String nom;
    @Column(nullable = false, length = 100)                private String prenom;
    @Column(name = "date_naissance")                       private LocalDate dateNaissance;
    @Column(nullable = false, unique = true, length = 150) private String email;
    private String telephone;
    @Column(name = "mot_de_passe", nullable = false)       private String motDePasse;
    @Column(name = "numero_carte_fidelite", unique = true) private String numeroCarteFidelite;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_fidelite", nullable = false)
    private TypeFidelite typeFidelite;
    @Column(nullable = false) @Builder.Default private Boolean deleted = false;
    // RGPD art.7
    @Column(name = "rgpd_consent",      nullable = false) @Builder.Default private Boolean rgpdConsent = false;
    @Column(name = "rgpd_consent_date")                                     private LocalDateTime rgpdConsentDate;
    @Column(name = "rgpd_consent_ip", length = 45)                          private String rgpdConsentIp;
    // RGPD art.17
    @Column(name = "demande_suppression", nullable = false) @Builder.Default private Boolean demandeSuppression = false;
    @Column(name = "date_suppression")                                        private LocalDateTime dateSuppression;
    @CreatedDate  @Column(name = "date_creation",    nullable = false, updatable = false) private LocalDateTime dateCreation;
    @LastModifiedDate @Column(name = "date_modification", nullable = false)               private LocalDateTime dateModification;
}
