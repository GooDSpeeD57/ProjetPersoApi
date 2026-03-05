package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.TypeGarantie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TypeGarantieRepository extends JpaRepository<TypeGarantie, Long> {
    Optional<TypeGarantie> findByCode(String code);
}
