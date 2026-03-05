package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.config.SecurityHelper;
import com.monprojet.boutiquejeux.dto.request.AvisRequest;
import com.monprojet.boutiquejeux.dto.response.*;
import com.monprojet.boutiquejeux.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/produits") @RequiredArgsConstructor @Tag(name = "Produits")
public class ProduitController {
    private final ProduitService produitService; private final AvisService avisService; private final SecurityHelper securityHelper;
    @GetMapping            @Operation(summary = "Recherche produits (public)")
    public ResponseEntity<Page<ProduitResponse>> rechercher(@RequestParam(required=false) String search, @RequestParam(required=false) Long categorieId, @RequestParam(required=false) String plateforme, @RequestParam(required=false) String niveauAcces, @PageableDefault(size=20) Pageable pageable) { return ResponseEntity.ok(produitService.rechercher(search, categorieId, plateforme, niveauAcces, pageable)); }
    @GetMapping("/{id}")   @Operation(summary = "Détail produit (public)")
    public ResponseEntity<ProduitResponse> getById(@PathVariable Long id) { return ResponseEntity.ok(produitService.findById(id)); }
    @GetMapping("/{id}/avis") @Operation(summary = "Avis produit (public)")
    public ResponseEntity<Page<AvisResponse>> getAvis(@PathVariable Long id, @PageableDefault(size=10) Pageable pageable) { return ResponseEntity.ok(produitService.getAvis(id, pageable)); }
    @PostMapping("/{id}/avis") @Operation(summary = "Déposer un avis")
    public ResponseEntity<AvisResponse> deposerAvis(@PathVariable Long id, @Valid @RequestBody AvisRequest req, @AuthenticationPrincipal UserDetails ud) { return ResponseEntity.status(201).body(avisService.deposerAvis(securityHelper.getClientCourant(ud).getId(), id, req)); }
    @PutMapping("/{id}/avis")  @Operation(summary = "Modifier mon avis")
    public ResponseEntity<AvisResponse> modifierAvis(@PathVariable Long id, @Valid @RequestBody AvisRequest req, @AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(avisService.modifierAvis(securityHelper.getClientCourant(ud).getId(), id, req)); }
}
