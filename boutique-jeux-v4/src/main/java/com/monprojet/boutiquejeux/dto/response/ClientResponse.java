package com.monprojet.boutiquejeux.dto.response;
import java.time.LocalDate;
import java.time.LocalDateTime;
public record ClientResponse(
    Long id, String pseudo, String nom, String prenom,
    String email, String telephone, LocalDate dateNaissance,
    String niveauFidelite, String numeroCarteFidelite,
    Boolean rgpdConsent, LocalDateTime dateCreation
) {}
