package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.AlertHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class GarantiesController {

    @FXML private TextField rechercheClientField;
    @FXML private Label     labelClient;
    @FXML private TableView<Map<String, Object>> tableGaranties;
    @FXML private TableColumn<Map<String, Object>, String> colProduit;
    @FXML private TableColumn<Map<String, Object>, String> colDateAchat;
    @FXML private TableColumn<Map<String, Object>, String> colDateFin;
    @FXML private TableColumn<Map<String, Object>, String> colStatut;
    @FXML private TableColumn<Map<String, Object>, String> colDuree;
    @FXML private ProgressIndicator spinner;
    @FXML private Label labelNbGaranties;

    @FXML
    public void initialize() {
        spinner.setVisible(false);
        tableGaranties.getItems().clear();

        colProduit.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("nomProduit", "")));
        colDateAchat.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("dateAchat", "")));
        colDateFin.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("dateFinGarantie", "")));
        colDuree.setCellValueFactory(c -> {
            int mois = ((Number) c.getValue().getOrDefault("dureeGarantieMois", 0)).intValue();
            return new SimpleStringProperty(mois + " mois");
        });
        colStatut.setCellValueFactory(c -> {
            String fin = (String) c.getValue().getOrDefault("dateFinGarantie", "");
            boolean active;
            try { active = LocalDate.parse(fin).isAfter(LocalDate.now()); }
            catch (Exception ex) { active = false; }
            return new SimpleStringProperty(active ? "✅ Active" : "❌ Expirée");
        });

        // Couleur selon statut
        tableGaranties.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { setStyle(""); return; }
                String fin = (String) item.getOrDefault("dateFinGarantie", "");
                try {
                    boolean active = LocalDate.parse(fin).isAfter(LocalDate.now());
                    setStyle(active ? "-fx-background-color: #1a3d1a;" : "-fx-background-color: #2d2d2d;");
                } catch (Exception ex) { setStyle(""); }
            }
        });
    }

    @FXML
    private void handleRechercheClient() {
        String q = rechercheClientField.getText().trim();
        if (q.isEmpty()) return;
        spinner.setVisible(true);

        Task<Map<String, Object>> clientTask = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get("/clients/search?q=" + q);
            }
        };
        clientTask.setOnSucceeded(e -> {
            Map<String, Object> client = clientTask.getValue();
            Long clientId = ((Number) client.get("id")).longValue();
            Platform.runLater(() -> labelClient.setText(client.get("prenom") + " " + client.get("nom")));
            chargerGaranties(clientId);
        });
        clientTask.setOnFailed(e -> {
            spinner.setVisible(false);
            AlertHelper.error("Client introuvable", clientTask.getException().getMessage());
        });
        new Thread(clientTask).start();
    }

    private void chargerGaranties(Long clientId) {
        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get("/garanties?clientId=" + clientId);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> res = task.getValue();
            List<Map<String, Object>> garanties = (List<Map<String, Object>>) res.getOrDefault("content", List.of());
            Platform.runLater(() -> {
                tableGaranties.setItems(FXCollections.observableArrayList(garanties));
                labelNbGaranties.setText(garanties.size() + " garantie(s) trouvée(s)");
                spinner.setVisible(false);
            });
        });
        task.setOnFailed(e -> Platform.runLater(() -> spinner.setVisible(false)));
        new Thread(task).start();
    }
}
