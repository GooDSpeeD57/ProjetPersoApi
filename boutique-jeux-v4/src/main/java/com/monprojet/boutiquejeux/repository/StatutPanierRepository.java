package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.StatutPanier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface StatutPanierRepository extends JpaRepository<StatutPanier, Long> {
    Optional<StatutPanier> findByCode(String code);
}
