package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.dto.InscriptionForm;
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

@Controller
class HomeController {
    @GetMapping("/")
    String home() { return "index"; }
}

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {

    private final ApiService api;

    @GetMapping("/login")
    String loginPage() { return "auth/login"; }

    @GetMapping("/inscription")
    String inscriptionPage(Model model) {
        model.addAttribute("inscriptionForm", new InscriptionForm());
        return "auth/inscription";
    }

    @PostMapping("/inscription")
    String inscription(@Valid @ModelAttribute("inscriptionForm") InscriptionForm form,
                       BindingResult result,
                       HttpSession session,
                       RedirectAttributes redirect,
                       Model model) {

        if (result.hasErrors()) return "auth/inscription";

        try {
            Map<String, Object> resp = api.inscription(Map.of(
                "nom",         form.getNom(),
                "prenom",      form.getPrenom(),
                "pseudo",      form.getPseudo(),
                "email",       form.getEmail(),
                "motDePasse",  form.getMotDePasse(),
                "telephone",   form.getTelephone(),
                "dateNaissance", form.getDateNaissance(),
                "rgpdConsent", form.getRgpdConsent()
            ));

            if (resp != null && resp.get("accessToken") != null) {
                session.setAttribute("jwt", resp.get("accessToken"));
                session.setAttribute("userEmail", resp.get("email"));
                session.setAttribute("userRole",  resp.get("role"));
            }
            redirect.addFlashAttribute("successMessage", "Compte créé avec succès ! Bienvenue 🎮");
            return "redirect:/catalogue";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/inscription";
        }
    }
}