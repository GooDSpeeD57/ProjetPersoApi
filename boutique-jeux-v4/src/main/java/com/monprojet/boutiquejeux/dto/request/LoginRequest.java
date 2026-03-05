package com.monprojet.boutiquejeux.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String motDePasse,
    @NotBlank @Pattern(regexp = "CLIENT|EMPLOYE") String userType
) {}
