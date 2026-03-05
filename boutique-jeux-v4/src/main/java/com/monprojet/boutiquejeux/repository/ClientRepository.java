package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmailAndDeletedFalse(String email);
    Optional<Client> findByIdAndDeletedFalse(Long id);
    @Query("SELECT c FROM Client c WHERE c.demandeSuppression = true AND c.deleted = false")
    Page<Client> findAllEnAttenteSupression(Pageable pageable);
}
