package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.ExtensionGarantie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ExtensionGarantieRepository extends JpaRepository<ExtensionGarantie, Long> {
    List<ExtensionGarantie> findAllByGarantieId(Long garantieId);
}
