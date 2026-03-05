package com.monprojet.boutiquejeux.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Service centralisé pour tous les appels vers l'API Spring Boot (port 8080).
 * Chaque méthode retourne null en cas d'erreur (loggée) pour ne pas crasher la vue.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final RestClient restClient;

    // ── PRODUITS ──────────────────────────────────────────────

    public Map<String, Object> getProduits(int page, int size, String search, String plateforme) {
        try {
            String uri = "/produits?page=" + page + "&size=" + size;
            if (search     != null && !search.isBlank())     uri += "&search="     + search;
            if (plateforme != null && !plateforme.isBlank()) uri += "&plateforme=" + plateforme;
            return restClient.get().uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getProduits error: {}", e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getProduitDetail(Long id) {
        try {
            return restClient.get().uri("/produits/" + id)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getProduitDetail error: {}", e.getMessage());
            return null;
        }
    }

    // ── AUTH ──────────────────────────────────────────────────

    public Map<String, Object> inscription(Map<String, Object> body) {
        try {
            return restClient.post().uri("/auth/inscription")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
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
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("login error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    // ── CLIENT ────────────────────────────────────────────────

    public Map<String, Object> getClientMe(String jwtToken) {
        try {
            return restClient.get().uri("/clients/me")
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getClientMe error: {}", e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getClientPoints(String jwtToken) {
        try {
            return restClient.get().uri("/clients/me/points")
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getClientPoints error: {}", e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getClientFactures(String jwtToken) {
        try {
            return restClient.get().uri("/clients/me/factures")
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getClientFactures error: {}", e.getMessage());
            return null;
        }
    }

    public void deleteClientMe(String jwtToken) {
        try {
            restClient.delete().uri("/clients/me")
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            log.error("deleteClientMe error: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression du compte");
        }
    }

    // ── STOCK ─────────────────────────────────────────────────

    public java.util.List<Map<String, Object>> getStocks(String jwtToken, Long magasinId) {
        try {
            String uri = "/stock";
            if (magasinId != null) uri += "?magasinId=" + magasinId;
            return restClient.get().uri(uri)
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getStocks error: {}", e.getMessage());
            return null;
        }
    }

    public java.util.List<Map<String, Object>> getMagasins(String jwtToken) {
        try {
            return restClient.get().uri("/stock/magasins")
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getMagasins error: {}", e.getMessage());
            return null;
        }
    }

    public void updateStock(String jwtToken, Long stockId, int quantite) {
        try {
            restClient.put().uri("/stock/" + stockId)
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("quantite", quantite))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            log.error("updateStock error: {}", e.getMessage());
            throw new RuntimeException("Erreur mise à jour stock");
        }
    }

    // ── GARANTIES ─────────────────────────────────────────────

    public java.util.List<Map<String, Object>> getGaranties(String jwtToken) {
        try {
            return restClient.get().uri("/garanties")
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getGaranties error: {}", e.getMessage());
            return null;
        }
    }

    public java.util.List<Map<String, Object>> getTypesGarantie(String jwtToken) {
        try {
            return restClient.get().uri("/garanties/types")
                    .header("Authorization", "Bearer " + jwtToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("getTypesGarantie error: {}", e.getMessage());
            return null;
        }
    }

    public Map<String, Object> verifierGarantie(String numeroSerie, Long produitId) {
        try {
            return restClient.get()
                    .uri("/garanties/verifier?numeroSerie=" + numeroSerie + "&produitId=" + produitId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            log.error("verifierGarantie error: {}", e.getMessage());
            return null;
        }
    }

    public void enregistrerGarantie(String jwtToken, Map<String, Object> body) {
        try {
            restClient.post().uri("/garanties")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            log.error("enregistrerGarantie error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    public void etendreGarantie(String jwtToken, Long garantieId, Long typeGarantieId) {
        try {
            restClient.post().uri("/garanties/etendre")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("garantieId", garantieId, "typeGarantieId", typeGarantieId))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            log.error("etendreGarantie error: {}", e.getMessage());
            throw new RuntimeException(extractMessage(e.getMessage()));
        }
    }

    // ── UTILITAIRE ────────────────────────────────────────────

    private String extractMessage(String rawError) {
        if (rawError == null) return "Erreur serveur";
        // Extrait le message entre guillemets si présent
        int start = rawError.indexOf('"');
        int end   = rawError.lastIndexOf('"');
        if (start >= 0 && end > start) return rawError.substring(start + 1, end);
        return rawError;
    }
}
