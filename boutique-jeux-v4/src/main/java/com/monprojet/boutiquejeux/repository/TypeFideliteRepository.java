package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.TypeFidelite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TypeFideliteRepository extends JpaRepository<TypeFidelite, Long> {
    Optional<TypeFidelite> findByCode(String code);
}
