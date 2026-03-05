package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.dto.request.*;
import com.monprojet.boutiquejeux.dto.response.AuthResponse;
import com.monprojet.boutiquejeux.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
@Tag(name = "Authentification")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")    @Operation(summary = "Connexion client ou employé")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) { return ResponseEntity.ok(authService.login(req)); }
    @PostMapping("/inscription") @Operation(summary = "Inscription nouveau client")
    public ResponseEntity<AuthResponse> inscrire(@Valid @RequestBody InscriptionRequest req, HttpServletRequest http) {
        String ip = http.getHeader("X-Forwarded-For"); if (ip == null) ip = http.getRemoteAddr();
        return ResponseEntity.status(201).body(authService.inscrire(req, ip));
    }
}
