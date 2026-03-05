package com.monprojet.boutiquejeux.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class InscriptionForm {

    @NotBlank
    @Size(min=2,max=100)
    private String nom;

    @NotBlank
    @Size(min=2,max=100)
    private String prenom;

    @NotBlank
    @Size(min=3,max=50)
    @Pattern(regexp="^[a-zA-Z0-9_\\-]+$", message="Lettres, chiffres, _ ou -")
    private String pseudo;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=8)
    @Pattern(
            regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_]).{8,}$",
            message="Min 8 cars, 1 maj, 1 min, 1 chiffre, 1 spécial"
    )
    private String motDePasse;

    @NotBlank
    private String confirmMotDePasse;

    @NotBlank(message="Téléphone obligatoire")
    @Pattern(regexp="^(\\+33|0)[1-9](\\d{2}){4}$", message="Format invalide (ex: 0612345678)")
    private String telephone;

    @NotNull(message="Date de naissance obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;

    @NotNull(message="Obligatoire")
    @AssertTrue(message="Vous devez accepter les conditions RGPD")
    private Boolean rgpdConsent;

    @AssertTrue(message="Les mots de passe ne correspondent pas")
    public boolean isPasswordMatching() {
        if (motDePasse == null || confirmMotDePasse == null) {
            return false;
        }
        return motDePasse.equals(confirmMotDePasse);
    }
}