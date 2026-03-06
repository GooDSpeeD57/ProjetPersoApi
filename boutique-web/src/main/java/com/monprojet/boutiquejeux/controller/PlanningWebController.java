package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Controller
@RequestMapping("/admin/planning")
@RequiredArgsConstructor
public class PlanningWebController {

    private final ApiService api;

    @GetMapping
    String planning(HttpSession session, Model model,
                    @RequestParam(required = false) Long employeId,
                    @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                    @RequestParam(required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        String jwt = (String) session.getAttribute("jwt");
        if (jwt == null) return "redirect:/auth/login";

        if (debut == null) debut = LocalDate.now().withDayOfMonth(1);
        if (fin   == null) fin   = debut.plusMonths(1).minusDays(1);

        model.addAttribute("employes",  api.getEmployes(jwt));
        model.addAttribute("statuts",   api.getStatutsPlanning(jwt));
        model.addAttribute("plannings", api.getPlannings(jwt, employeId, debut, fin));
        model.addAttribute("employeId", employeId);
        model.addAttribute("debut",     debut);
        model.addAttribute("fin",       fin);
        return "admin/planning";
    }

    @PostMapping("/creer")
    String creer(HttpSession session,
                 @RequestParam Long       employeId,
                 @RequestParam Long       statutPlanningId,
                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTravail,
                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin,
                 RedirectAttributes redirect) {

        String jwt = (String) session.getAttribute("jwt");
        try {
            api.creerPlanning(jwt, Map.of(
                "employeId",       employeId,
                "statutPlanningId",statutPlanningId,
                "dateTravail",     dateTravail.toString(),
                "heureDebut",      heureDebut.toString(),
                "heureFin",        heureFin.toString()
            ));
            redirect.addFlashAttribute("successMessage", "Créneau créé avec succès");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/planning";
    }

    @PostMapping("/supprimer/{id}")
    String supprimer(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
        String jwt = (String) session.getAttribute("jwt");
        try {
            api.supprimerPlanning(jwt, id);
            redirect.addFlashAttribute("successMessage", "Créneau supprimé");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/planning";
    }
}
