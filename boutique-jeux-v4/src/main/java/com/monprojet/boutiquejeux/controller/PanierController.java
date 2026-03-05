package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.config.SecurityHelper;
import com.monprojet.boutiquejeux.dto.request.AjoutPanierRequest;
import com.monprojet.boutiquejeux.dto.response.PanierResponse;
import com.monprojet.boutiquejeux.service.PanierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/panier") @RequiredArgsConstructor @Tag(name = "Panier")
public class PanierController {
    private final PanierService panierService; private final SecurityHelper securityHelper;
    @GetMapping              @Operation(summary = "Mon panier actif")
    public ResponseEntity<PanierResponse> monPanier(@AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(panierService.getPanierActif(securityHelper.getClientCourant(ud).getId())); }
    @PostMapping("/lignes")  @Operation(summary = "Ajouter au panier")
    public ResponseEntity<PanierResponse> ajouter(@Valid @RequestBody AjoutPanierRequest req, @AuthenticationPrincipal UserDetails ud) { return ResponseEntity.status(201).body(panierService.ajouterProduit(securityHelper.getClientCourant(ud).getId(), req)); }
    @DeleteMapping("/lignes/{ligneId}") @Operation(summary = "Retirer une ligne")
    public ResponseEntity<Void> supprimer(@PathVariable Long ligneId, @AuthenticationPrincipal UserDetails ud) { panierService.supprimerLigne(securityHelper.getClientCourant(ud).getId(), ligneId); return ResponseEntity.noContent().build(); }
}
