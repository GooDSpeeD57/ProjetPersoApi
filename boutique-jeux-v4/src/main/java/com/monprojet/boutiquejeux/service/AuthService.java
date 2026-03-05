package com.monprojet.boutiquejeux.service;

import com.monprojet.boutiquejeux.dto.request.InscriptionRequest;
import com.monprojet.boutiquejeux.dto.request.LoginRequest;
import com.monprojet.boutiquejeux.dto.response.AuthResponse;
import com.monprojet.boutiquejeux.entity.Client;
import com.monprojet.boutiquejeux.entity.TypeFidelite;
import com.monprojet.boutiquejeux.exception.BusinessException;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.ClientRepository;
import com.monprojet.boutiquejeux.repository.TypeFideliteRepository;
import com.monprojet.boutiquejeux.security.jwt.JwtService;
import com.monprojet.boutiquejeux.security.service.ClientUserDetailsService;
import com.monprojet.boutiquejeux.security.service.EmployeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository          clientRepository;
    private final TypeFideliteRepository    typeFideliteRepository;
    private final ClientUserDetailsService  clientUDS;
    private final EmployeUserDetailsService employeUDS;
    private final JwtService                jwtService;
    private final PasswordEncoder           passwordEncoder;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        UserDetails userDetails = "EMPLOYE".equals(req.userType())
                ? employeUDS.loadUserByUsername(req.email())
                : clientUDS.loadUserByUsername(req.email());

        if (!passwordEncoder.matches(req.motDePasse(), userDetails.getPassword())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return new AuthResponse(
                jwtService.generateAccessToken(userDetails, req.userType()),
                jwtService.generateRefreshToken(userDetails),
                req.userType(), req.email(), role);
    }

    @Transactional
    public AuthResponse inscrire(InscriptionRequest req, String ipAdresse) {
        if (clientRepository.findByEmailAndDeletedFalse(req.email()).isPresent())
            throw new BusinessException("Email déjà utilisé");

        // Validation métier : age minimum 18 ans
        if (req.dateNaissance() != null && req.dateNaissance().isAfter(LocalDate.now().minusYears(18)))
            throw new BusinessException("Vous devez avoir au moins 18 ans pour vous inscrire");

        TypeFidelite normal = typeFideliteRepository.findByCode("NORMAL")
                .orElseThrow(() -> new ResourceNotFoundException("Type fidélité NORMAL introuvable"));

        Client client = Client.builder()
                .pseudo(req.pseudo())
                .nom(req.nom())
                .prenom(req.prenom())
                .email(req.email())
                .motDePasse(passwordEncoder.encode(req.motDePasse()))
                .telephone(req.telephone())
                .typeFidelite(normal)
                .numeroCarteFidelite(UUID.randomUUID().toString().substring(0, 16).toUpperCase())
                .rgpdConsent(req.rgpdConsent())
                .rgpdConsentDate(req.rgpdConsent() ? LocalDateTime.now() : null)
                .rgpdConsentIp(req.rgpdConsent() ? ipAdresse : null)
                .build();

        clientRepository.save(client);
        UserDetails userDetails = clientUDS.loadUserByUsername(client.getEmail());
        return new AuthResponse(
                jwtService.generateAccessToken(userDetails, "CLIENT"),
                jwtService.generateRefreshToken(userDetails),
                "CLIENT", client.getEmail(), "ROLE_NORMAL");
    }
}
