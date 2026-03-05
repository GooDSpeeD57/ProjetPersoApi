package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.AvisProduit;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface AvisProduitRepository extends JpaRepository<AvisProduit, Long> {
    Page<AvisProduit> findByProduitIdOrderByDateCreationDesc(Long produitId, Pageable pageable);
    Optional<AvisProduit> findByClientIdAndProduitId(Long clientId, Long produitId);
    boolean existsByClientIdAndProduitId(Long clientId, Long produitId);
    @Query("SELECT AVG(a.note) FROM AvisProduit a WHERE a.produit.id = :produitId")
    Double findNoteMoyenneByProduitId(@Param("produitId") Long produitId);
    long countByProduitId(Long produitId);
}
