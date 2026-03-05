package com.monprojet.boutiquejeux.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record EmployeRequest(

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

    @NotNull(message = "Le rôle est obligatoire")
    Long roleId,

    @NotNull(message = "Le magasin est obligatoire")
    Long magasinId,

    @NotNull(message = "La date d'embauche est obligatoire")
    @PastOrPresent(message = "La date d'embauche ne peut pas être dans le futur")
    LocalDate dateEmbauche

) {}
