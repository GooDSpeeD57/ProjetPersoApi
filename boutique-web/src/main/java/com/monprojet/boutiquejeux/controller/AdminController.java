package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApiService api;

    // ── TABLEAU DE BORD ──────────────────────────────────────────
    @GetMapping
    String dashboard(HttpSession session, Model model) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        model.addAttribute("stats", api.getAdminStats(jwt));
        return "admin/dashboard";
    }

    // ── GESTION EMPLOYÉS ─────────────────────────────────────────
    @GetMapping("/employes")
    String employes(HttpSession session, Model model) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        model.addAttribute("employes", api.getEmployes(jwt));
        model.addAttribute("roles",    api.getRoles(jwt));
        model.addAttribute("magasins", api.getMagasinsAll(jwt));
        return "admin/employes";
    }

    @PostMapping("/employes/creer")
    String creerEmploye(HttpSession session,
                        @RequestParam String nom,
                        @RequestParam String prenom,
                        @RequestParam String email,
                        @RequestParam String motDePasse,
                        @RequestParam Long   roleId,
                        @RequestParam Long   magasinId,
                        @RequestParam String dateEmbauche,
                        RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        try {
            api.creerEmploye(jwt, Map.of(
                "nom", nom, "prenom", prenom, "email", email,
                "motDePasse", motDePasse, "roleId", roleId,
                "magasinId", magasinId, "dateEmbauche", dateEmbauche
            ));
            redirect.addFlashAttribute("successMessage", "Employé créé avec succès");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/employes";
    }

    @PostMapping("/employes/supprimer/{id}")
    String supprimerEmploye(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        try {
            api.supprimerEmploye(jwt, id);
            redirect.addFlashAttribute("successMessage", "Employé désactivé");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/employes";
    }

    // ── GESTION CLIENTS ──────────────────────────────────────────
    @GetMapping("/clients")
    String clients(HttpSession session, Model model,
                   @RequestParam(defaultValue = "0") int page) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        model.addAttribute("clients", api.getClients(jwt, page));
        model.addAttribute("page", page);
        return "admin/clients";
    }

    // ── PROMOTIONS ───────────────────────────────────────────────
    @GetMapping("/promotions")
    String promotions(HttpSession session, Model model) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        model.addAttribute("promotions",    api.getPromotions(jwt));
        model.addAttribute("typesReduction",api.getTypesReduction(jwt));
        return "admin/promotions";
    }

    @PostMapping("/promotions/creer")
    String creerPromotion(HttpSession session,
                          @RequestParam String codePromo,
                          @RequestParam String description,
                          @RequestParam Long   typeReductionId,
                          @RequestParam Double valeur,
                          @RequestParam String dateDebut,
                          @RequestParam String dateFin,
                          RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        try {
            api.creerPromotion(jwt, Map.of(
                "codePromo", codePromo, "description", description,
                "typeReductionId", typeReductionId, "valeur", valeur,
                "dateDebut", dateDebut, "dateFin", dateFin
            ));
            redirect.addFlashAttribute("successMessage", "Promotion créée avec succès");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/promotions";
    }

    // ── GESTION PRODUITS ─────────────────────────────────────────
    @GetMapping("/produits")
    String produits(HttpSession session, Model model,
                    @RequestParam(defaultValue = "0") int page) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        model.addAttribute("produits",   api.getProduitsAdmin(jwt, page));
        model.addAttribute("categories", api.getCategories(jwt));
        model.addAttribute("page", page);
        return "admin/produits";
    }

    // ── AUDIT LOG ────────────────────────────────────────────────
    @GetMapping("/audit")
    String audit(HttpSession session, Model model) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        model.addAttribute("logs", api.getAuditLogs(jwt));
        return "admin/audit";
    }
}
