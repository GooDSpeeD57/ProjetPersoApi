package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.dto.request.*;
import com.monprojet.boutiquejeux.dto.response.GarantieResponse;
import com.monprojet.boutiquejeux.service.GarantieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/garanties") @RequiredArgsConstructor @Tag(name = "Garanties")
public class GarantieController {
    private final GarantieService garantieService;
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDEUR')") @Operation(summary = "Enregistrer une garantie à la vente")
    public ResponseEntity<GarantieResponse> enregistrer(@Valid @RequestBody GarantieRequest req) { return ResponseEntity.status(201).body(garantieService.enregistrer(req)); }
    @PostMapping("/etendre") @PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDEUR')") @Operation(summary = "Étendre une garantie existante")
    public ResponseEntity<GarantieResponse> etendre(@Valid @RequestBody ExtensionGarantieRequest req) { return ResponseEntity.ok(garantieService.etendre(req)); }
    @GetMapping("/verifier") @Operation(summary = "Vérifier une garantie par numéro de série (public SAV)")
    public ResponseEntity<GarantieResponse> verifier(@RequestParam String numeroSerie, @RequestParam Long produitId) { return ResponseEntity.ok(garantieService.verifier(numeroSerie, produitId)); }
}
