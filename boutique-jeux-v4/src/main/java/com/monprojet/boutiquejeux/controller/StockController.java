package com.monprojet.boutiquejeux.controller;
import com.monprojet.boutiquejeux.dto.response.StockageResponse;
import com.monprojet.boutiquejeux.service.StockageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/stock") @RequiredArgsConstructor @PreAuthorize("hasAnyRole('ADMIN','MANAGER','VENDEUR')") @Tag(name = "Stock")
public class StockController {
    private final StockageService stockageService;
    @GetMapping("/magasin/{magasinId}")   @Operation(summary = "Stock d'un magasin")  public ResponseEntity<List<StockageResponse>> parMagasin(@PathVariable Long magasinId) { return ResponseEntity.ok(stockageService.getStockMagasin(magasinId)); }
    @GetMapping("/produit/{produitId}")   @Operation(summary = "Stock d'un produit")  public ResponseEntity<List<StockageResponse>> parProduit(@PathVariable Long produitId)  { return ResponseEntity.ok(stockageService.getStockProduit(produitId)); }
    @PatchMapping("/magasin/{magasinId}/produit/{produitId}") @Operation(summary = "Mettre à jour le stock")
    public ResponseEntity<StockageResponse> update(@PathVariable Long magasinId, @PathVariable Long produitId, @RequestParam Integer quantite) { return ResponseEntity.ok(stockageService.updateStock(magasinId, produitId, quantite)); }
}
