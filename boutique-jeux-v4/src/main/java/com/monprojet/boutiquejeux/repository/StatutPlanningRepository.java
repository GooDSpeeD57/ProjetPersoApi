package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.StatutPlanning;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface StatutPlanningRepository extends JpaRepository<StatutPlanning, Long> {
    Optional<StatutPlanning> findByCode(String code);
}
