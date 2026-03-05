package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.config.SecurityHelper;
import com.monprojet.boutiquejeux.dto.request.PlanningRequest;
import com.monprojet.boutiquejeux.dto.response.PlanningResponse;
import com.monprojet.boutiquejeux.service.PlanningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
@RestController @RequestMapping("/api/planning") @RequiredArgsConstructor @Tag(name = "Planning")
public class PlanningController {
    private final PlanningService planningService; private final SecurityHelper securityHelper;
    @GetMapping("/me") @Operation(summary = "Mon planning")
    public ResponseEntity<List<PlanningResponse>> monPlanning(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate debut, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate fin, @AuthenticationPrincipal UserDetails ud) { return ResponseEntity.ok(planningService.getPlanningEmploye(securityHelper.getEmployeCourant(ud).getId(), debut, fin)); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','MANAGER')") @Operation(summary = "Créer un créneau")
    public ResponseEntity<PlanningResponse> creer(@Valid @RequestBody PlanningRequest req) { return ResponseEntity.status(201).body(planningService.creer(req)); }
    @DeleteMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','MANAGER')") @Operation(summary = "Supprimer un créneau")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) { planningService.supprimer(id); return ResponseEntity.noContent().build(); }
}
