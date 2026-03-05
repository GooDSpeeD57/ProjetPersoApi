package com.monprojet.boutiquejeux.repository;
import com.monprojet.boutiquejeux.entity.BonAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BonAchatRepository extends JpaRepository<BonAchat, Long> {
    Optional<BonAchat> findByCodeBon(String codeBon);
    List<BonAchat> findAllByClientIdAndUtiliseFalse(Long clientId);
}
