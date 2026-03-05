package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.ProduitPrix;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ProduitPrixRepository extends JpaRepository<ProduitPrix, Long> {
    Optional<ProduitPrix> findByProduitIdAndStatutProduitCode(Long produitId, String code);
}
