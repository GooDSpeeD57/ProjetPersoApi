package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Entity @Table(name = "panier") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Panier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_panier") private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false) private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_statut_panier", nullable = false) private StatutPanier statutPanier;
    @Column(name = "date_creation", nullable = false, updatable = false)
    @Builder.Default private LocalDateTime dateCreation = LocalDateTime.now();
    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LignePanier> lignes;
}
