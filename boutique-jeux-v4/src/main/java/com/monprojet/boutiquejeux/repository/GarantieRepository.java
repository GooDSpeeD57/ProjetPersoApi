package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.Garantie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface GarantieRepository extends JpaRepository<Garantie, Long> {
    Optional<Garantie> findByNumeroSerie(String numeroSerie);
    boolean existsByNumeroSerie(String numeroSerie);
}
