package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PanierRepository extends JpaRepository<Panier, Long> {
    Optional<Panier> findByClientIdAndStatutPanierCode(Long clientId, String statutCode);
}
