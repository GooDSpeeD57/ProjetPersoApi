package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthWebController {

    private final ApiService api;

    // ── LOGIN ────────────────────────────────────────────────────

    @GetMapping("/login")
    String loginPage(HttpServletRequest request, Model model) {
        if (request.getCookies() != null) {
            Arrays.stream(request.getCookies())
                .filter(c -> "bj_remember_email".equals(c.getName()))
                .findFirst()
                .ifPresent(c -> model.addAttribute("rememberedEmail", c.getValue()));
            Arrays.stream(request.getCookies())
                .filter(c -> "bj_remember_type".equals(c.getName()))
                .findFirst()
                .ifPresent(c -> model.addAttribute("rememberedType", c.getValue()));
        }
        return "auth/login";
    }

    @PostMapping("/login")
    String login(@RequestParam String email,
                 @RequestParam String motDePasse,
                 @RequestParam(defaultValue = "CLIENT") String userType,
                 @RequestParam(required = false) String rememberMe,
                 HttpSession session,
                 HttpServletResponse response,
                 RedirectAttributes redirect) {
        try {
            Map<String, Object> result = api.login(email, motDePasse, userType);
            if (result == null || result.get("accessToken") == null) {
                redirect.addFlashAttribute("errorMessage", "Identifiants incorrects.");
                return "redirect:/auth/login";
            }

            String jwt = (String) result.get("accessToken");
            session.setAttribute("jwt", jwt);
            session.setAttribute("userType", userType);
            session.setAttribute("emailUtilisateur", email);

            if ("CLIENT".equals(userType)) {
                try {
                    var profil = api.getClientMe(jwt);
                    session.setAttribute("prenomUtilisateur",
                        profil != null && profil.get("prenom") != null ? profil.get("prenom") : email);
                } catch (Exception ignored) {
                    session.setAttribute("prenomUtilisateur", email);
                }
                try {
                    var abo = api.getAbonnementMe(jwt);
                    session.setAttribute("isUltimate", abo != null && "ACTIF".equals(abo.get("statut")));
                } catch (Exception ignored) {
                    session.setAttribute("isUltimate", false);
                }
            } else {
                session.setAttribute("prenomUtilisateur", email.split("@")[0]);
                session.setAttribute("isUltimate", false);
            }

            if ("on".equals(rememberMe)) {
                addCookie(response, "bj_remember_email", email, 30 * 24 * 3600);
                addCookie(response, "bj_remember_type", userType, 30 * 24 * 3600);
            } else {
                addCookie(response, "bj_remember_email", "", 0);
                addCookie(response, "bj_remember_type", "", 0);
            }

            return "CLIENT".equals(userType) ? "redirect:/catalogue" : "redirect:/admin";

        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/login";
        }
    }

    // ── INSCRIPTION ──────────────────────────────────────────────

    @GetMapping("/inscription")
    String inscriptionPage() { return "auth/inscription"; }

    @PostMapping("/inscription")
    String inscription(@RequestParam String nom,
                       @RequestParam String prenom,
                       @RequestParam String email,
                       @RequestParam String motDePasse,
                       @RequestParam(required = false) String telephone,
                       @RequestParam(required = false) String dateNaissance,
                       RedirectAttributes redirect) {
        try {
            // Pseudo : enlever accents + caractères invalides [a-zA-Z0-9_-]
            String pseudoBase = Normalizer.normalize(prenom + nom, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-zA-Z0-9_\\-]", "")
                .toLowerCase();
            if (pseudoBase.length() < 3) pseudoBase = pseudoBase + "usr";
            if (pseudoBase.length() > 48) pseudoBase = pseudoBase.substring(0, 48);

            // dateNaissance : toujours une valeur valide (18+ ans) — jamais null dans le body
            String dob = (dateNaissance != null && !dateNaissance.isBlank())
                ? dateNaissance : "1990-01-01";

            // Téléphone : valeur par défaut si vide
            String tel = (telephone != null && !telephone.isBlank())
                ? telephone : "0600000000";

            // HashMap (pas Map.of) → supporte toutes valeurs, pas de NPE
            Map<String, Object> body = new HashMap<>();
            body.put("pseudo",        pseudoBase);
            body.put("nom",           nom);
            body.put("prenom",        prenom);
            body.put("email",         email);
            body.put("motDePasse",    motDePasse);
            body.put("telephone",     tel);
            body.put("dateNaissance", dob);
            body.put("rgpdConsent",   true);

            api.inscription(body);
            redirect.addFlashAttribute("successMessage", "Compte créé avec succès ! Connectez-vous.");
            return "redirect:/auth/login";

        } catch (RuntimeException e) {
            String msg = e.getMessage();
            // Masquer les erreurs 500 brutes à l'utilisateur
            if (msg == null || msg.contains("500") || msg.contains("null")) {
                msg = "Erreur lors de l'inscription. Vérifiez que l'email n'est pas déjà utilisé et que le mot de passe respecte les critères.";
            }
            redirect.addFlashAttribute("errorMessage", msg);
            return "redirect:/auth/inscription";
        }
    }

    // ── LOGOUT ───────────────────────────────────────────────────

    @GetMapping("/logout")
    String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    // ── UTILITAIRE ───────────────────────────────────────────────

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(maxAge);
        c.setPath("/");
        response.addCookie(c);
    }
}
