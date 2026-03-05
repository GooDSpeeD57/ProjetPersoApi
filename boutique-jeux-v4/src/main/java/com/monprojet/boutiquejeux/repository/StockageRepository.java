package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.Stockage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface StockageRepository extends JpaRepository<Stockage, Long> {
    Optional<Stockage> findByProduitIdAndMagasinId(Long produitId, Long magasinId);
    List<Stockage> findAllByMagasinId(Long magasinId);
    List<Stockage> findAllByProduitId(Long produitId);
}
