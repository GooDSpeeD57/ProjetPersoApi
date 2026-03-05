package com.monprojet.boutiquejeux.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record InscriptionRequest(

    @NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 3, max = 50, message = "Le pseudo doit contenir entre 3 et 50 caractères")
    @Pattern(
        regexp = "^[a-zA-Z0-9_\\-]+$",
        message = "Le pseudo ne peut contenir que des lettres, chiffres, tirets et underscores"
    )
    String pseudo,

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Pattern(
        regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$",
        message = "Le nom ne peut contenir que des lettres, espaces, tirets et apostrophes"
    )
    String nom,

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    @Pattern(
        regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$",
        message = "Le prénom ne peut contenir que des lettres, espaces, tirets et apostrophes"
    )
    String prenom,

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    @Size(max = 150, message = "L'email ne peut pas dépasser 150 caractères")
    String email,

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_])[A-Za-z\\d@$!%*?&\\-_]{8,}$",
        message = "Le mot de passe doit contenir au moins 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial"
    )
    String motDePasse,

    @Pattern(
        regexp = "^(\\+33|0)[1-9](\\d{2}){4}$",
        message = "Format téléphone invalide (ex: 0612345678 ou +33612345678)"
    )
    String telephone,

    @Past(message = "La date de naissance doit être dans le passé")
    LocalDate dateNaissance,

    @NotNull(message = "Le consentement RGPD est obligatoire")
    @AssertTrue(message = "Vous devez accepter les conditions RGPD pour vous inscrire")
    Boolean rgpdConsent

) {}
