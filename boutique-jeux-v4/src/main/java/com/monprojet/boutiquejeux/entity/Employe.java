package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name = "employe")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Employe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employe") private Long id;
    @Column(nullable = false, length = 100)                private String nom;
    @Column(nullable = false, length = 100)                private String prenom;
    @Column(nullable = false, unique = true, length = 150) private String email;
    @Column(name = "mot_de_passe", nullable = false)       private String motDePasse;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "id_role",    nullable = false) private Role role;
    @ManyToOne(fetch = FetchType.LAZY)  @JoinColumn(name = "id_magasin", nullable = false) private Magasin magasin;
    @Column(name = "date_embauche") private LocalDate dateEmbauche;
    @Column(nullable = false) @Builder.Default private Boolean deleted = false;
    @CreatedDate      @Column(name = "date_creation",     nullable = false, updatable = false) private LocalDateTime dateCreation;
    @LastModifiedDate @Column(name = "date_modification", nullable = false)                    private LocalDateTime dateModification;
}
