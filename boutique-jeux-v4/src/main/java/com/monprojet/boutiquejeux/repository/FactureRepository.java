package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.Facture;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface FactureRepository extends JpaRepository<Facture, Long> {
    Page<Facture> findAllByClientId(Long clientId, Pageable pageable);
    Page<Facture> findAllByMagasinId(Long magasinId, Pageable pageable);
    @Query("SELECT COUNT(lf) > 0 FROM LigneFacture lf JOIN lf.facture f WHERE f.client.id = :clientId AND lf.produit.id = :produitId")
    boolean clientAacheteProduit(@Param("clientId") Long clientId, @Param("produitId") Long produitId);
}
