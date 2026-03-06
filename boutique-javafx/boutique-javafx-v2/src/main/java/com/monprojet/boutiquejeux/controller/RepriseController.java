package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.AlertHelper;
import com.monprojet.boutiquejeux.util.SessionManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepriseController {

    @FXML private TextField   rechercheClientField;
    @FXML private Label       labelClient;
    @FXML private TextField   rechercheProduitField;
    @FXML private TableView<Map<String, Object>> tableProduits;
    @FXML private TableColumn<Map<String, Object>, String> colNom;
    @FXML private TableColumn<Map<String, Object>, String> colGenre;
    @FXML private TableColumn<Map<String, Object>, String> colPrixNeuf;

    @FXML private ComboBox<String> comboEtat;
    @FXML private TextArea         noteArea;
    @FXML private Label            labelPrixEstime;
    @FXML private TextField        prixFinalField;
    @FXML private Button           btnValiderReprise;
    @FXML private ProgressIndicator spinner;

    private Long   clientId   = null;
    private Long   produitId  = null;
    private double prixEstime = 0;

    /** Coefficients de reprise selon état */
    private static final Map<String, Double> COEFF = Map.of(
        "NEUF_SCELLE",   0.70,
        "TRES_BON_ETAT", 0.55,
        "BON_ETAT",      0.40,
        "ETAT_MOYEN",    0.25,
        "MAUVAIS_ETAT",  0.10
    );

    @FXML
    public void initialize() {
        spinner.setVisible(false);

        colNom.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("nom", "")));
        colGenre.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("genre", "")));
        colPrixNeuf.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOrDefault("prix", "") + " €"));
        tableProduits.getItems().clear();

        comboEtat.setItems(FXCollections.observableArrayList(
                "NEUF_SCELLE", "TRES_BON_ETAT", "BON_ETAT", "ETAT_MOYEN", "MAUVAIS_ETAT"));
        comboEtat.setValue("BON_ETAT");
        comboEtat.setOnAction(e -> calculerPrixEstime());

        tableProduits.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> calculerPrixEstime());
    }

    @FXML
    private void handleRechercheClient() {
        String q = rechercheClientField.getText().trim();
        if (q.isEmpty()) return;
        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get("/clients/search?q=" + q);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> c = task.getValue();
            clientId = ((Number) c.get("id")).longValue();
            Platform.runLater(() -> labelClient.setText(c.get("prenom") + " " + c.get("nom") + " (" + c.get("email") + ")"));
        });
        task.setOnFailed(e -> AlertHelper.error("Client introuvable", task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    private void handleRechercheProduit() {
        String q = rechercheProduitField.getText().trim();
        if (q.isEmpty()) return;
        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get("/produits?q=" + q);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> res = task.getValue();
            List<Map<String, Object>> content = (List<Map<String, Object>>) res.getOrDefault("content", List.of());
            Platform.runLater(() -> tableProduits.setItems(FXCollections.observableArrayList(content)));
        });
        task.setOnFailed(e -> AlertHelper.error("Erreur", task.getException().getMessage()));
        new Thread(task).start();
    }

    private void calculerPrixEstime() {
        Map<String, Object> produit = tableProduits.getSelectionModel().getSelectedItem();
        if (produit == null || comboEtat.getValue() == null) return;
        produitId = ((Number) produit.get("id")).longValue();
        double prixNeuf = ((Number) produit.getOrDefault("prix", 0)).doubleValue();
        double coeff    = COEFF.getOrDefault(comboEtat.getValue(), 0.30);
        prixEstime      = Math.round(prixNeuf * coeff * 100.0) / 100.0;
        Platform.runLater(() -> {
            labelPrixEstime.setText(String.format("Prix estimé : %.2f €", prixEstime));
            prixFinalField.setText(String.format("%.2f", prixEstime));
        });
    }

    @FXML
    private void handleValiderReprise() {
        if (clientId == null)  { AlertHelper.warn("Client",  "Identifiez d'abord le client."); return; }
        if (produitId == null) { AlertHelper.warn("Produit", "Sélectionnez un produit."); return; }

        double prixFinal;
        try { prixFinal = Double.parseDouble(prixFinalField.getText().replace(',', '.')); }
        catch (NumberFormatException ex) { AlertHelper.error("Prix", "Prix final invalide."); return; }

        if (!AlertHelper.confirm("Confirmer la reprise",
                String.format("Reprendre ce produit pour %.2f € ?\n" +
                              "Le client recevra un avoir ou un virement.", prixFinal))) return;

        setLoading(true);

        Map<String, Object> body = new HashMap<>();
        body.put("clientId",   clientId);
        body.put("produitId",  produitId);
        body.put("magasinId",  SessionManager.getInstance().getMagasinId());
        body.put("etat",       comboEtat.getValue());
        body.put("prixReprise",prixFinal);
        body.put("note",       noteArea.getText());

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().post("/reprises", body);
            }
        };
        task.setOnSucceeded(e -> {
            setLoading(false);
            Map<String, Object> res = task.getValue();
            AlertHelper.success("Reprise enregistrée",
                    "✅ Reprise n°" + res.get("id") + " créée !\nAvoir client : " + String.format("%.2f €", prixFinal));
            resetForm();
        });
        task.setOnFailed(e -> { setLoading(false); AlertHelper.error("Erreur reprise", task.getException().getMessage()); });
        new Thread(task).start();
    }

    private void resetForm() {
        clientId = null; produitId = null;
        Platform.runLater(() -> {
            rechercheClientField.clear(); labelClient.setText("Aucun client sélectionné");
            rechercheProduitField.clear(); tableProduits.getItems().clear();
            labelPrixEstime.setText("Prix estimé : —"); prixFinalField.clear();
            noteArea.clear(); comboEtat.setValue("BON_ETAT");
        });
    }

    private void setLoading(boolean b) {
        Platform.runLater(() -> { btnValiderReprise.setDisable(b); spinner.setVisible(b); });
    }
}
