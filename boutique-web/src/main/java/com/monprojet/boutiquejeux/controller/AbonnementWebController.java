package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/abonnement")
@RequiredArgsConstructor
public class AbonnementWebController {

    private final ApiService api;

    @GetMapping
    String abonnement(HttpSession session, Model model) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";

        try {
            model.addAttribute("abonnement", api.getAbonnementActif(jwt));
        } catch (RuntimeException e) {
            model.addAttribute("abonnement", null);
        }
        return "abonnement/index";
    }

    @PostMapping("/souscrire")
    String souscrire(HttpSession session,
                     @RequestParam(defaultValue = "true") Boolean renouvellementAuto,
                     RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        try {
            api.souscrireUltimate(jwt, renouvellementAuto);
            redirect.addFlashAttribute("successMessage",
                "🎉 Bienvenue dans le programme ULTIMATE ! Profitez de vos avantages exclusifs.");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/abonnement";
    }

    @PostMapping("/renouvellement")
    String toggleRenouvellement(HttpSession session,
                                @RequestParam Boolean actif,
                                RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        try {
            api.toggleRenouvellement(jwt, actif);
            redirect.addFlashAttribute("successMessage",
                "Renouvellement automatique " + (actif ? "activé" : "désactivé"));
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/abonnement";
    }
}
