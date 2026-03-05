package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.config.SecurityHelper;
import com.monprojet.boutiquejeux.dto.response.AbonnementResponse;
import com.monprojet.boutiquejeux.service.AbonnementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/abonnements") @RequiredArgsConstructor @Tag(name = "Abonnement ULTIMATE")
public class AbonnementController {
    private final AbonnementService abonnementService; private final SecurityHelper securityHelper;
    @GetMapping("/me")       @Operation(summary = "Mon abonnement actif")
    public ResponseEntity<AbonnementResponse> monAbonnement(@AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(abonnementService.getAbonnementActif(securityHelper.getClientCourant(ud).getId())); }
    @PostMapping("/souscrire") @Operation(summary = "Souscrire à ULTIMATE")
    public ResponseEntity<AbonnementResponse> souscrire(@RequestParam(defaultValue="true") Boolean renouvellementAuto, @AuthenticationPrincipal UserDetails ud) { return ResponseEntity.status(201).body(abonnementService.souscrire(securityHelper.getClientCourant(ud).getId(), renouvellementAuto)); }
    @PatchMapping("/me/renouvellement") @Operation(summary = "Toggle renouvellement auto")
    public ResponseEntity<AbonnementResponse> toggleRenouvellement(@RequestParam Boolean actif, @AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(abonnementService.toggleRenouvellement(securityHelper.getClientCourant(ud).getId(), actif)); }
}
