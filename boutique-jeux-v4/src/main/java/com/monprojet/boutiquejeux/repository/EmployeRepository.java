package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    Optional<Employe> findByEmailAndDeletedFalse(String email);
    Optional<Employe> findByIdAndDeletedFalse(Long id);
}
