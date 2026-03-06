package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CatalogueWebController {

    private final ApiService api;

    @GetMapping("/")
    String home() {
        return "redirect:/catalogue";
    }

    @GetMapping("/catalogue")
    String catalogue(@RequestParam(defaultValue = "0")   int    page,
                     @RequestParam(defaultValue = "20")  int    size,
                     @RequestParam(required = false)      String search,
                     @RequestParam(required = false)      String plateforme,
                     Model model) {
        model.addAttribute("produits",   api.getProduits(page, size, search, plateforme));
        model.addAttribute("search",     search);
        model.addAttribute("plateforme", plateforme);
        model.addAttribute("page",       page);
        return "catalogue/index";
    }

    @GetMapping("/catalogue/{id}")
    String detail(@PathVariable Long id, Model model) {
        model.addAttribute("produit", api.getProduitDetail(id));
        return "catalogue/detail";
    }
}
