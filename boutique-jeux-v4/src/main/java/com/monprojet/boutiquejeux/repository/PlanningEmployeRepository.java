package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.PlanningEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface PlanningEmployeRepository extends JpaRepository<PlanningEmploye, Long> {
    List<PlanningEmploye> findAllByEmployeIdAndDateTravailBetween(Long employeId, LocalDate debut, LocalDate fin);
}
