package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/compte")
@RequiredArgsConstructor
public class CompteWebController {

    private final ApiService api;

    @GetMapping
    String compte(HttpSession session, Model model) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";

        model.addAttribute("client",    api.getClientMe(jwt));
        model.addAttribute("points",    api.getClientPoints(jwt));
        model.addAttribute("factures",  api.getClientFactures(jwt));

        // Abonnement ULTIMATE si client
        if ("CLIENT".equals(session.getAttribute("userType"))) {
            try { model.addAttribute("abonnement", api.getAbonnementActif(jwt)); }
            catch (Exception ignored) {}
        }
        return "compte/index";
    }

    @PostMapping("/supprimer")
    String supprimer(HttpSession session, RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";
        try {
            api.deleteClientMe(jwt);
            session.invalidate();
            redirect.addFlashAttribute("successMessage", "Votre compte a été supprimé.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/compte";
        }
    }
}
