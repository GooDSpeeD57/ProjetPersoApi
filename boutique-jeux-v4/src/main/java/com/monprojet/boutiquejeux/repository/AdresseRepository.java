package com.monprojet.boutiquejeux.repository;

import com.monprojet.boutiquejeux.entity.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    List<Adresse> findByIdMagasin(Long idMagasin);
    List<Adresse> findByIdClient(Long idClient);
}
