package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.config.SecurityHelper;
import com.monprojet.boutiquejeux.dto.response.FactureResponse;
import com.monprojet.boutiquejeux.service.FactureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/factures") @RequiredArgsConstructor @Tag(name = "Factures")
public class FactureController {
    private final FactureService factureService; private final SecurityHelper securityHelper;
    @GetMapping("/mes-achats") @Operation(summary = "Mes factures")
    public ResponseEntity<Page<FactureResponse>> mesAchats(@AuthenticationPrincipal UserDetails ud, @PageableDefault(size=10) Pageable p) { return ResponseEntity.ok(factureService.getFacturesClient(securityHelper.getClientCourant(ud).getId(), p)); }
    @GetMapping("/{id}")       @Operation(summary = "Détail facture")
    public ResponseEntity<FactureResponse> getById(@PathVariable Long id, @AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(factureService.getFactureById(id, securityHelper.getClientCourant(ud).getId())); }
    @GetMapping("/admin/magasin/{magasinId}") @PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDEUR')") @Operation(summary = "Factures magasin (employé)")
    public ResponseEntity<Page<FactureResponse>> parMagasin(@PathVariable Long magasinId, @PageableDefault(size=20) Pageable p) { return ResponseEntity.ok(factureService.getFacturesMagasin(magasinId, p)); }
}
