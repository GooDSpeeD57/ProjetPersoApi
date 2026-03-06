package com.monprojet.boutiquejeux.repository;

import com.monprojet.boutiquejeux.entity.HistoriquePoints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriquePointsRepository extends JpaRepository<HistoriquePoints, Long> {

    List<HistoriquePoints> findAllByClientIdOrderByDateOperationDesc(Long clientId);

    Page<HistoriquePoints> findAllByClientIdOrderByDateOperationDesc(Long clientId, Pageable pageable);
}
