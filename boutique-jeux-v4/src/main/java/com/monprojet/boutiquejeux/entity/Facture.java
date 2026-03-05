package com.monprojet.boutiquejeux.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Entity @Table(name = "facture") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Facture {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facture") private Long id;
    // V9 : NULL autorisé pour ventes anonymes en magasin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client") private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_magasin", nullable = false) private Magasin magasin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employe") private Employe employe;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_mode_paiement", nullable = false) private ModePaiement modePaiement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bon_achat") private BonAchat bonAchat;
    @Column(name = "date_facture", nullable = false) @Builder.Default private LocalDateTime dateFacture = LocalDateTime.now();
    @Column(name = "montant_total",  nullable = false) @Builder.Default private BigDecimal montantTotal  = BigDecimal.ZERO;
    @Column(name = "montant_remise", nullable = false) @Builder.Default private BigDecimal montantRemise = BigDecimal.ZERO;
    @Column(name = "montant_final",  nullable = false) @Builder.Default private BigDecimal montantFinal  = BigDecimal.ZERO;
    // V9 : champs ventes anonymes
    @Column(name = "nom_client",       length = 100) private String nomClient;
    @Column(name = "email_client",     length = 150) private String emailClient;
    @Column(name = "telephone_client", length = 20)  private String telephoneClient;
    @Column(name = "contexte_vente", nullable = false, length = 20)
    @Enumerated(EnumType.STRING) @Builder.Default private ContexteVente contexteVente = ContexteVente.EN_MAGASIN;
    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneFacture> lignes;
    public enum ContexteVente { EN_LIGNE, EN_MAGASIN }
}
