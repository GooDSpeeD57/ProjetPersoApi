package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.config.SecurityHelper;
import com.monprojet.boutiquejeux.dto.response.*;
import com.monprojet.boutiquejeux.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/clients") @RequiredArgsConstructor @Tag(name = "Client")
public class ClientController {
    private final ClientService clientService; private final FideliteService fideliteService; private final SecurityHelper securityHelper;
    @GetMapping("/me")       @Operation(summary = "Mon profil")
    public ResponseEntity<ClientResponse> monProfil(@AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(clientService.findById(securityHelper.getClientCourant(ud).getId())); }
    @GetMapping("/me/points") @Operation(summary = "Mes points fidélité")
    public ResponseEntity<PointsFideliteResponse> mesPoints(@AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(fideliteService.getPoints(securityHelper.getClientCourant(ud).getId())); }
    @DeleteMapping("/me")    @Operation(summary = "Demande suppression RGPD art.17")
    public ResponseEntity<Void> demanderSuppression(@AuthenticationPrincipal UserDetails ud) { clientService.demanderSuppression(securityHelper.getClientCourant(ud).getId()); return ResponseEntity.noContent().build(); }
}
