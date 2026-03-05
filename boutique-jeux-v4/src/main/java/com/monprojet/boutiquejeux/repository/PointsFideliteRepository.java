package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.PointsFidelite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PointsFideliteRepository extends JpaRepository<PointsFidelite, Long> {
    Optional<PointsFidelite> findByClientId(Long clientId);
}
