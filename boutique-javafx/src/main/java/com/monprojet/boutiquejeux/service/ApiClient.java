package com.monprojet.boutiquejeux.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.monprojet.boutiquejeux.util.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * Client HTTP singleton pour appeler l'API boutique-jeux (Spring Boot).
 * Utilise java.net.http.HttpClient (Java 11+), pas de dépendance externe.
 */
public class ApiClient {

    private static ApiClient instance;

    private static final String BASE_URL = "http://localhost:8080/api";

    private final HttpClient http;
    private final ObjectMapper mapper;

    private ApiClient() {
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public static ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    // ── GET ──────────────────────────────────────────────────────

    public Map<String, Object> get(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Authorization", SessionManager.getInstance().getBearerToken())
                .header("Accept", "application/json")
                .GET()
                .build();
        return send(req, new TypeReference<>() {});
    }

    public Map<String, Object> getPublic(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Accept", "application/json")
                .GET()
                .build();
        return send(req, new TypeReference<>() {});
    }

    // ── POST ─────────────────────────────────────────────────────

    public Map<String, Object> post(String path, Object body) throws Exception {
        String json = mapper.writeValueAsString(body);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Authorization", SessionManager.getInstance().getBearerToken())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return send(req, new TypeReference<>() {});
    }

    public Map<String, Object> postPublic(String path, Object body) throws Exception {
        String json = mapper.writeValueAsString(body);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        return send(req, new TypeReference<>() {});
    }

    // ── PATCH ────────────────────────────────────────────────────

    public Map<String, Object> patch(String path, Object body) throws Exception {
        String json = mapper.writeValueAsString(body);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Authorization", SessionManager.getInstance().getBearerToken())
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();
        return send(req, new TypeReference<>() {});
    }

    // ── DELETE ───────────────────────────────────────────────────

    public void delete(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Authorization", SessionManager.getInstance().getBearerToken())
                .DELETE()
                .build();
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) {
            throw new RuntimeException(extractError(resp.body()));
        }
    }

    // ── INTERNE ──────────────────────────────────────────────────

    private <T> T send(HttpRequest req, TypeReference<T> type) throws Exception {
        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) {
            throw new RuntimeException(extractError(resp.body()));
        }
        return mapper.readValue(resp.body(), type);
    }

    private String extractError(String body) {
        try {
            Map<String, Object> err = mapper.readValue(body, new TypeReference<>() {});
            Object msg = err.get("error");
            if (msg == null) msg = err.get("message");
            return msg != null ? msg.toString() : "Erreur API (" + body + ")";
        } catch (Exception e) {
            return body.length() > 200 ? body.substring(0, 200) : body;
        }
    }

    public ObjectMapper getMapper() { return mapper; }
}
