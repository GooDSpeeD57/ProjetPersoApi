package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.dto.GarantieForm;
import com.monprojet.boutiquejeux.service.ApiService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

// ── MON COMPTE ────────────────────────────────────────────────────────
@Controller
@RequestMapping("/compte")
@RequiredArgsConstructor
class CompteController {

    private final ApiService api;

    @GetMapping
    String compte(HttpSession session, Model model) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";

        model.addAttribute("client",   api.getClientMe(jwt));
        model.addAttribute("points",   api.getClientPoints(jwt));
        model.addAttribute("factures", api.getClientFactures(jwt));
        return "compte/index";
    }

    @PostMapping("/supprimer")
    String supprimer(HttpSession session, RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        try {
            api.deleteClientMe(jwt);
            session.invalidate();
            redirect.addFlashAttribute("successMessage",
                "Votre demande de suppression a été enregistrée (RGPD art.17)");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/compte";
        }
    }
}

// ── ADMIN STOCK ───────────────────────────────────────────────────────
@Controller
@RequestMapping("/admin/stock")
@RequiredArgsConstructor
class AdminStockController {

    private final ApiService api;

    @GetMapping
    String stock(HttpSession session, Model model,
                 @RequestParam(required = false) Long magasinId) {
        String jwt = (String) session.getAttribute("jwt");

        model.addAttribute("stocks",    api.getStocks(jwt, magasinId));
        model.addAttribute("magasins",  api.getMagasins(jwt));
        model.addAttribute("magasinId", magasinId);
        return "admin/stock";
    }

    @PostMapping("/update")
    String update(HttpSession session,
                  @RequestParam Long stockId,
                  @RequestParam int  quantite,
                  RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        try {
            api.updateStock(jwt, stockId, quantite);
            redirect.addFlashAttribute("successMessage", "Stock mis à jour");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/stock";
    }
}

// ── GARANTIES ─────────────────────────────────────────────────────────
@Controller
@RequestMapping("/garanties")
@RequiredArgsConstructor
class GarantieController {

    private final ApiService api;

    @GetMapping
    String garanties(HttpSession session, Model model,
                     @RequestParam(required = false) String numeroSerie,
                     @RequestParam(required = false) Long   produitId) {
        String jwt = (String) session.getAttribute("jwt");

        model.addAttribute("garanties",     api.getGaranties(jwt));
        model.addAttribute("typesGarantie", api.getTypesGarantie(jwt));
        model.addAttribute("produits",      api.getProduits(0, 100, null, null));
        model.addAttribute("garantieForm",  new GarantieForm());

        // Résultat vérification si params présents
        if (numeroSerie != null && produitId != null) {
            model.addAttribute("garantieResultat",  api.verifierGarantie(numeroSerie, produitId));
            model.addAttribute("numeroSerieRecherche", numeroSerie);
            model.addAttribute("produitIdRecherche",   produitId);
        }
        return "garanties/index";
    }

    @PostMapping("/enregistrer")
    String enregistrer(HttpSession session,
                       @Valid @ModelAttribute("garantieForm") GarantieForm form,
                       BindingResult result,
                       Model model,
                       RedirectAttributes redirect) {
        if (result.hasErrors()) {
            String jwt = (String) session.getAttribute("jwt");
            model.addAttribute("garanties",     api.getGaranties(jwt));
            model.addAttribute("typesGarantie", api.getTypesGarantie(jwt));
            model.addAttribute("produits",      api.getProduits(0, 100, null, null));
            return "garanties/index";
        }
        String jwt = (String) session.getAttribute("jwt");
        try {
            api.enregistrerGarantie(jwt, Map.of(
                "numeroSerie", form.getNumeroSerie(),
                "produitId",   form.getProduitId(),
                "factureId",   form.getFactureId(),
                "dureeMois",   form.getDureeMois()
            ));
            redirect.addFlashAttribute("successMessage", "Garantie enregistrée");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/garanties";
    }

    @PostMapping("/etendre")
    String etendre(HttpSession session,
                   @RequestParam Long garantieId,
                   @RequestParam Long typeGarantieId,
                   RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        try {
            api.etendreGarantie(jwt, garantieId, typeGarantieId);
            redirect.addFlashAttribute("successMessage", "Garantie étendue");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/garanties";
    }

    @GetMapping("/verifier")
    String verifier(@RequestParam String numeroSerie,
                    @RequestParam Long   produitId,
                    RedirectAttributes redirect) {
        return "redirect:/garanties?numeroSerie=" + numeroSerie + "&produitId=" + produitId;
    }
}
