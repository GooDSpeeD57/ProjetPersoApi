package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.dto.CartItem;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/panier")
public class PanierController {

    @GetMapping
    String panier(HttpSession session, Model model) {
        List<CartItem> panier = getCart(session);
        BigDecimal total = panier.stream()
                .map(i -> i.getPrix().multiply(BigDecimal.valueOf(i.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalQte = panier.stream().mapToInt(CartItem::getQuantite).sum();

        model.addAttribute("panier",        panier);
        model.addAttribute("totalPrix",     total);
        model.addAttribute("totalQuantite", totalQte);
        return "panier/index";
    }

    @PostMapping("/ajouter")
    String ajouter(@RequestParam Long       produitId,
                   @RequestParam String     produitNom,
                   @RequestParam(required = false) BigDecimal prix,
                   HttpSession session,
                   RedirectAttributes redirect) {

        List<CartItem> panier = getCart(session);
        CartItem existing = panier.stream()
                .filter(i -> i.getProduitId().equals(produitId))
                .findFirst().orElse(null);

        BigDecimal prixSafe = prix != null ? prix : BigDecimal.ZERO;

        if (existing != null) {
            existing.setQuantite(existing.getQuantite() + 1);
        } else {
            panier.add(new CartItem(produitId, produitNom, prixSafe, 1));
        }
        saveCart(session, panier);
        redirect.addFlashAttribute("successMessage", produitNom + " ajouté au panier");
        return "redirect:/catalogue";
    }

    @PostMapping("/quantite")
    String quantite(@RequestParam Long produitId,
                    @RequestParam int  quantite,
                    HttpSession session) {
        List<CartItem> panier = getCart(session);
        panier.stream()
                .filter(i -> i.getProduitId().equals(produitId))
                .findFirst()
                .ifPresent(i -> {
                    if (quantite <= 0) panier.remove(i);
                    else i.setQuantite(quantite);
                });
        saveCart(session, panier);
        return "redirect:/panier";
    }

    @PostMapping("/retirer")
    String retirer(@RequestParam Long produitId, HttpSession session) {
        List<CartItem> panier = getCart(session);
        panier.removeIf(i -> i.getProduitId().equals(produitId));
        saveCart(session, panier);
        return "redirect:/panier";
    }

    @PostMapping("/vider")
    String vider(HttpSession session) {
        saveCart(session, new ArrayList<>());
        return "redirect:/panier";
    }

    @PostMapping("/valider")
    String valider(HttpSession session, RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) {
            redirect.addFlashAttribute("errorMessage", "Connectez-vous pour finaliser votre commande");
            return "redirect:/auth/login";
        }
        // TODO : appel API /factures pour créer la commande
        redirect.addFlashAttribute("infoMessage", "Fonctionnalité de paiement à venir");
        return "redirect:/panier";
    }

    // ── Helpers session ──
    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        Object cart = session.getAttribute("cart");
        if (cart instanceof List<?>) return (List<CartItem>) cart;
        return new ArrayList<>();
    }

    private void saveCart(HttpSession session, List<CartItem> panier) {
        session.setAttribute("cart", panier);
        session.setAttribute("cartCount", panier.stream().mapToInt(CartItem::getQuantite).sum());
    }
}
