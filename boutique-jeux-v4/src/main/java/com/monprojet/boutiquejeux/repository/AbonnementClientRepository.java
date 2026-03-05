package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.AbonnementClient;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface AbonnementClientRepository extends JpaRepository<AbonnementClient, Long> {
    @Query("SELECT a FROM AbonnementClient a JOIN a.statutAbonnement s WHERE a.client.id = :clientId AND s.code = 'ACTIF' AND a.dateFin >= :today")
    Optional<AbonnementClient> findAbonnementActif(@Param("clientId") Long clientId, @Param("today") LocalDate today);
    @Query("SELECT a FROM AbonnementClient a JOIN a.statutAbonnement s WHERE s.code = 'ACTIF' AND a.dateFin < :today")
    List<AbonnementClient> findAbonnementsExpires(@Param("today") LocalDate today);
}
