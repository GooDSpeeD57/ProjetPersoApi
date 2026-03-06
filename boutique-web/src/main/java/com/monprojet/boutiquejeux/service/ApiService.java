package com.monprojet.boutiquejeux.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final RestClient restClient;

    // ── PRODUITS ──────────────────────────────────────────────

    public Map<String, Object> getProduits(int page, int size, String search, String plateforme) {
        try {
            StringBuilder uri = new StringBuilder("/produits?page=" + page + "&size=" + size);
            if (search     != null && !search.isBlank())     uri.append("&search=").append(enc(search));
            if (plateforme != null && !plateforme.isBlank()) uri.append("&plateforme=").append(enc(plateforme));
            return restClient.get().uri(uri.toString())
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getProduits error: {}", e.getMessage()); return null; }
    }

    public Map<String, Object> getProduitDetail(Long id) {
        try {
            return restClient.get().uri("/produits/" + id)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getProduitDetail error: {}", e.getMessage()); return null; }
    }

    // ── AUTH ──────────────────────────────────────────────────

    public Map<String, Object> inscription(Map<String, Object> body) {
        try {
            return restClient.post().uri("/auth/inscription")
                    .contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("inscription error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public Map<String, Object> login(String email, String motDePasse, String userType) {
        try {
            return restClient.post().uri("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("email", email, "motDePasse", motDePasse, "userType", userType))
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("login error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    // ── CLIENT ────────────────────────────────────────────────

    public Map<String, Object> getClientMe(String jwt) {
        try {
            return restClient.get().uri("/clients/me").header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getClientMe error: {}", e.getMessage()); return null; }
    }

    public Map<String, Object> getClientPoints(String jwt) {
        try {
            return restClient.get().uri("/clients/me/points").header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getClientPoints error: {}", e.getMessage()); return null; }
    }

    public Map<String, Object> getClientFactures(String jwt) {
        try {
            return restClient.get().uri("/factures/mes-achats").header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getClientFactures error: {}", e.getMessage()); return null; }
    }

    public void deleteClientMe(String jwt) {
        try {
            restClient.delete().uri("/clients/me").header("Authorization", "Bearer " + jwt)
                    .retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("deleteClientMe error: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression du compte");
        }
    }

    // ── ABONNEMENT ────────────────────────────────────────────

    public Map<String, Object> getAbonnementMe(String jwt) {
        try {
            return restClient.get().uri("/abonnements/me").header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.warn("getAbonnementMe: {}", e.getMessage()); return null; }
    }

    public Map<String, Object> souscrireUltimate(String jwt, boolean renouvellementAuto) {
        try {
            return restClient.post().uri("/abonnements/souscrire?renouvellementAuto=" + renouvellementAuto)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("souscrireUltimate error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public Map<String, Object> toggleRenouvellement(String jwt, boolean actif) {
        try {
            return restClient.patch().uri("/abonnements/me/renouvellement?actif=" + actif)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("toggleRenouvellement error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    // ── STOCK ─────────────────────────────────────────────────

    public List<Map<String, Object>> getStocks(String jwt, Long magasinId) {
        try {
            String uri = "/stock/magasin/" + (magasinId != null ? magasinId : 1);
            return restClient.get().uri(uri).header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getStocks error: {}", e.getMessage()); return null; }
    }

    public List<Map<String, Object>> getMagasins(String jwt) {
        return List.of(
            Map.of("id", 1L, "nom", "Micromania Moulins-lès-Metz (1)"),
            Map.of("id", 2L, "nom", "Micromania Moulins-lès-Metz (2)"),
            Map.of("id", 3L, "nom", "Micromania Moulins-lès-Metz (3)"),
            Map.of("id", 4L, "nom", "Micromania Moulins-lès-Metz (4)")
        );
    }

    public void updateStock(String jwt, Long stockId, int quantite) {
        try {
            restClient.patch().uri("/stock/magasin/1/produit/" + stockId + "?quantite=" + quantite)
                    .header("Authorization", "Bearer " + jwt).retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("updateStock error: {}", e.getMessage());
            throw new RuntimeException("Erreur mise à jour stock");
        }
    }

    public void updateStockMagasin(String jwt, Long magasinId, Long produitId, Integer quantite) {
        try {
            restClient.patch().uri("/stock/magasin/" + magasinId + "/produit/" + produitId + "?quantite=" + quantite)
                    .header("Authorization", "Bearer " + jwt).retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("updateStockMagasin error: {}", e.getMessage());
            throw new RuntimeException("Erreur mise à jour stock");
        }
    }

    // ── GARANTIES ─────────────────────────────────────────────

    public List<Map<String, Object>> getGaranties(String jwt) {
        try {
            return restClient.get().uri("/garanties/actives").header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getGaranties error: {}", e.getMessage()); return null; }
    }

    public List<Map<String, Object>> getTypesGarantie(String jwt) {
        return List.of(
            Map.of("id", 1L, "code", "STANDARD_CONSOLE",   "description", "Garantie console 24 mois"),
            Map.of("id", 2L, "code", "STANDARD_ACCESSOIRE", "description", "Garantie accessoire 12 mois"),
            Map.of("id", 3L, "code", "ETENDUE_CONSOLE",     "description", "Extension console +12 mois"),
            Map.of("id", 4L, "code", "ETENDUE_ACCESSOIRE",  "description", "Extension accessoire +12 mois")
        );
    }

    public Map<String, Object> verifierGarantie(String numeroSerie, Long produitId) {
        try {
            return restClient.get()
                    .uri("/garanties/verifier?numeroSerie=" + enc(numeroSerie) + "&produitId=" + produitId)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("verifierGarantie error: {}", e.getMessage()); return null; }
    }

    public void enregistrerGarantie(String jwt, Map<String, Object> body) {
        try {
            restClient.post().uri("/garanties").header("Authorization", "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("enregistrerGarantie error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public void etendreGarantie(String jwt, Long garantieId, Long typeGarantieId) {
        try {
            restClient.post().uri("/garanties/etendre").header("Authorization", "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("garantieId", garantieId, "typeGarantieId", typeGarantieId))
                    .retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("etendreGarantie error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    // ── PLANNING ──────────────────────────────────────────────

    public List<Map<String, Object>> getMonPlanning(String jwt, String debut, String fin) {
        try {
            return restClient.get().uri("/planning/me?debut=" + debut + "&fin=" + fin)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getMonPlanning error: {}", e.getMessage()); return null; }
    }

    public Map<String, Object> creerCreneau(String jwt, Map<String, Object> body) {
        try {
            return restClient.post().uri("/planning").header("Authorization", "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("creerCreneau error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public void supprimerCreneau(String jwt, Long planningId) {
        try {
            restClient.delete().uri("/planning/" + planningId)
                    .header("Authorization", "Bearer " + jwt).retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("supprimerCreneau error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public List<Map<String, Object>> getStatutsPlanning(String jwt) {
        return List.of(
            Map.of("id", 1L, "code", "PREVU"),
            Map.of("id", 2L, "code", "PRESENT"),
            Map.of("id", 3L, "code", "ABSENT"),
            Map.of("id", 4L, "code", "CONGE")
        );
    }

    // ── ADMIN ─────────────────────────────────────────────────

    public Map<String, Object> getClients(String jwt, int page, int size, String search) {
        try {
            StringBuilder uri = new StringBuilder("/admin/clients?page=" + page + "&size=" + size);
            if (search != null && !search.isBlank()) uri.append("&search=").append(enc(search));
            return restClient.get().uri(uri.toString()).header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getClients error: {}", e.getMessage()); return null; }
    }

    public void supprimerClient(String jwt, Long clientId) {
        try {
            restClient.delete().uri("/admin/clients/" + clientId)
                    .header("Authorization", "Bearer " + jwt).retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("supprimerClient error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public Map<String, Object> getEmployes(String jwt, int page, int size) {
        try {
            return restClient.get().uri("/admin/employes?page=" + page + "&size=" + size)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getEmployes error: {}", e.getMessage()); return null; }
    }

    public Map<String, Object> creerEmploye(String jwt, Map<String, Object> body) {
        try {
            return restClient.post().uri("/admin/employes").header("Authorization", "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("creerEmploye error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public void supprimerEmploye(String jwt, Long employeId) {
        try {
            restClient.delete().uri("/admin/employes/" + employeId)
                    .header("Authorization", "Bearer " + jwt).retrieve().toBodilessEntity();
        } catch (RestClientException e) {
            log.error("supprimerEmploye error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public List<Map<String, Object>> getPromotions(String jwt) {
        try {
            return restClient.get().uri("/admin/promotions").header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getPromotions error: {}", e.getMessage()); return null; }
    }

    public Map<String, Object> creerPromotion(String jwt, Map<String, Object> body) {
        try {
            return restClient.post().uri("/admin/promotions").header("Authorization", "Bearer " + jwt)
                    .contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("creerPromotion error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public List<Map<String, Object>> getAuditLogs(String jwt, int page, int size) {
        try {
            return restClient.get().uri("/admin/audit?page=" + page + "&size=" + size)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) { log.error("getAuditLogs error: {}", e.getMessage()); return null; }
    }

    public List<Map<String, Object>> getRoles(String jwt) {
        return List.of(
            Map.of("id", 1L, "code", "VENDEUR"),
            Map.of("id", 2L, "code", "MANAGER"),
            Map.of("id", 3L, "code", "ADMIN")
        );
    }

    // ── ALIAS / SURCHARGES pour les contrôleurs ───────────────

    /** Abonnement actif du client connecté */
    public Map<String, Object> getAbonnementActif(String jwt) {
        return getAbonnementMe(jwt);
    }

    /** Lister les plannings avec filtres employé + dates */
    public List<Map<String, Object>> getPlannings(String jwt, Long employeId, java.time.LocalDate debut, java.time.LocalDate fin) {
        return getMonPlanning(jwt, debut != null ? debut.toString() : null, fin != null ? fin.toString() : null);
    }

    /** Créer un créneau (alias avec Map) */
    public void creerPlanning(String jwt, Map<String, Object> body) {
        creerCreneau(jwt, body);
    }

    /** Supprimer un créneau planning */
    public void supprimerPlanning(String jwt, Long id) {
        supprimerCreneau(jwt, id);
    }

    /** Employés (liste paginée, page 0 par défaut) */
    public Map<String, Object> getEmployes(String jwt) {
        return getEmployes(jwt, 0, 50);
    }

    /** Clients paginés */
    public Map<String, Object> getClients(String jwt, int page) {
        return getClients(jwt, page, 20, null);
    }

    /** Magasins (alias) */
    public List<Map<String, Object>> getMagasinsAll(String jwt) {
        return getMagasins(jwt);
    }

    /** Types de réduction (hardcodé) */
    public List<Map<String, Object>> getTypesReduction(String jwt) {
        return List.of(
            Map.of("id", 1L, "code", "POURCENTAGE"),
            Map.of("id", 2L, "code", "MONTANT_FIXE")
        );
    }

    /** Catégories produits (hardcodé depuis la BDD) */
    public List<Map<String, Object>> getCategories(String jwt) {
        return List.of(
            Map.of("id", 1L, "nom", "Jeux PS5"),
            Map.of("id", 2L, "nom", "Jeux Xbox Series"),
            Map.of("id", 3L, "nom", "Jeux Nintendo Switch"),
            Map.of("id", 4L, "nom", "Jeux PC"),
            Map.of("id", 5L, "nom", "Consoles"),
            Map.of("id", 6L, "nom", "Accessoires"),
            Map.of("id", 7L, "nom", "Goodies")
        );
    }

    /** Produits admin (même endpoint que catalogue) */
    public Map<String, Object> getProduitsAdmin(String jwt, int page) {
        return getProduits(page, 20, null, null);
    }

    /** Logs audit (page 0, 50 entrées) */
    public List<Map<String, Object>> getAuditLogs(String jwt) {
        return getAuditLogs(jwt, 0, 50);
    }

    /** Stats admin (synthèse hardcodée pour le dashboard) */
    public Map<String, Object> getAdminStats(String jwt) {
        // Retourne des stats basiques disponibles
        Map<String, Object> stats = new java.util.LinkedHashMap<>();
        try { var clients = getClients(jwt, 0, 1, null); if (clients != null) stats.put("totalClients", clients.get("totalElements")); } catch (Exception ignored) {}
        try { var employes = getEmployes(jwt, 0, 1); if (employes != null) stats.put("totalEmployes", employes.get("totalElements")); } catch (Exception ignored) {}
        try { var produits = getProduits(0, 1, null, null); if (produits != null) stats.put("totalProduits", produits.get("totalElements")); } catch (Exception ignored) {}
        return stats;
    }

    // ── UTILITAIRE ────────────────────────────────────────────

    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public String extractMessage(String rawError) {
        if (rawError == null) return "Erreur serveur";
        int start = rawError.indexOf('"');
        int end   = rawError.lastIndexOf('"');
        if (start >= 0 && end > start) return rawError.substring(start + 1, end);
        return rawError;
    }
}
