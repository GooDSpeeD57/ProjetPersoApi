package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.Produit;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    @Query("""
        SELECT p FROM Produit p JOIN p.categorie c
        WHERE p.deleted = false AND p.actif = true
        AND (:search    IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%',:search,'%')))
        AND (:categorieId IS NULL OR c.id = :categorieId)
        AND (:plateforme  IS NULL OR p.plateforme = :plateforme)
        AND (:niveauAcces IS NULL OR p.niveauAccesMin = :niveauAcces)
    """)
    Page<Produit> searchProduits(
        @Param("search") String search,
        @Param("categorieId") Long categorieId,
        @Param("plateforme") String plateforme,
        @Param("niveauAcces") String niveauAcces,
        Pageable pageable);
}
