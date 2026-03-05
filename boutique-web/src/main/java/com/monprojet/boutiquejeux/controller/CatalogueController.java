package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/catalogue")
@RequiredArgsConstructor
public class CatalogueController {

    private final ApiService api;

    @GetMapping
    String catalogue(Model model,
                     @RequestParam(defaultValue = "0")  int    page,
                     @RequestParam(defaultValue = "12") int    size,
                     @RequestParam(required = false)    String search,
                     @RequestParam(required = false)    String plateforme,
                     @RequestParam(required = false)    String niveauAcces) {

        Map<String, Object> produits = api.getProduits(page, size, search, plateforme);
        model.addAttribute("produits",    produits);
        model.addAttribute("search",      search);
        model.addAttribute("plateforme",  plateforme);
        model.addAttribute("niveauAcces", niveauAcces);
        return "catalogue/index";
    }

    @GetMapping("/{id}")
    String detail(@PathVariable Long id, Model model) {
        Map<String, Object> produit = api.getProduitDetail(id);
        model.addAttribute("produit", produit);
        return "catalogue/detail";
    }
}
