package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.SessionManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.Map;

public class DashboardController {

    @FXML private Label labelBienvenue;
    @FXML private Label labelDate;
    @FXML private Label labelVentesJour;
    @FXML private Label labelCAJour;
    @FXML private Label labelReprisesJour;
    @FXML private Label labelStockBas;
    @FXML private Label labelNbClients;
    @FXML private Label labelMagasin;

    @FXML
    public void initialize() {
        SessionManager s = SessionManager.getInstance();
        labelBienvenue.setText("Bonjour, " + s.getPrenom() + " 👋");
        labelDate.setText(LocalDate.now().toString());
        labelMagasin.setText(s.getMagasinNom() != null ? s.getMagasinNom() : "—");

        chargerStats();
    }

    private void chargerStats() {
        Long magId = SessionManager.getInstance().getMagasinId();
        if (magId == null) return;

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get("/stats/dashboard?magasinId=" + magId);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> stats = task.getValue();
            Platform.runLater(() -> {
                labelVentesJour.setText(String.valueOf(stats.getOrDefault("ventesAujourdhui", 0)));
                labelCAJour.setText(stats.getOrDefault("caAujourdhui", "0") + " €");
                labelReprisesJour.setText(String.valueOf(stats.getOrDefault("reprisesAujourdhui", 0)));
                labelStockBas.setText(String.valueOf(stats.getOrDefault("stocksBas", 0)));
                labelNbClients.setText(String.valueOf(stats.getOrDefault("totalClients", 0)));
            });
        });
        task.setOnFailed(e -> {
            // Stats non disponibles — pas bloquant
            Platform.runLater(() -> {
                labelVentesJour.setText("—");
                labelCAJour.setText("—");
                labelReprisesJour.setText("—");
                labelStockBas.setText("—");
                labelNbClients.setText("—");
            });
        });
        new Thread(task).start();
    }
}
