package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.AlertHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Map;

public class RechercheController {

    @FXML private TextField      rechercheField;
    @FXML private ToggleGroup    modeGroup;
    @FXML private RadioButton    radioClient;
    @FXML private RadioButton    radioProduit;
    @FXML private ProgressIndicator spinner;

    // Table clients
    @FXML private TableView<Map<String, Object>> tableClients;
    @FXML private TableColumn<Map<String, Object>, String> colCPseudo;
    @FXML private TableColumn<Map<String, Object>, String> colCNom;
    @FXML private TableColumn<Map<String, Object>, String> colCEmail;
    @FXML private TableColumn<Map<String, Object>, String> colCTel;
    @FXML private TableColumn<Map<String, Object>, String> colCPoints;
    @FXML private TableColumn<Map<String, Object>, String> colCFidelite;

    // Table produits
    @FXML private TableView<Map<String, Object>> tableProduits;
    @FXML private TableColumn<Map<String, Object>, String> colPNom;
    @FXML private TableColumn<Map<String, Object>, String> colPGenre;
    @FXML private TableColumn<Map<String, Object>, String> colPPrix;
    @FXML private TableColumn<Map<String, Object>, String> colPType;

    // Détail client sélectionné
    @FXML private Label labelDetailNom;
    @FXML private Label labelDetailEmail;
    @FXML private Label labelDetailTel;
    @FXML private Label labelDetailPoints;
    @FXML private Label labelDetailFidelite;
    @FXML private Label labelDetailCarteFidelite;

    @FXML
    public void initialize() {
        spinner.setVisible(false);

        // Clients
        colCPseudo.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("pseudo", "")));
        colCNom.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOrDefault("prenom", "") + " " + c.getValue().getOrDefault("nom", "")));
        colCEmail.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("email", "")));
        colCTel.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("telephone", "")));
        colCPoints.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getOrDefault("soldePoints", 0))));
        colCFidelite.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("typeFidelite", "")));

        // Produits
        colPNom.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("nom", "")));
        colPGenre.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("genre", "")));
        colPPrix.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOrDefault("prix", "—") + " €"));
        colPType.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("type", "")));

        // Sélection client → afficher détail
        tableClients.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) afficherDetailClient(n);
        });

        // Basculer visibilité selon mode
        modeGroup.selectedToggleProperty().addListener((obs, o, n) -> {
            boolean isClient = n == radioClient;
            tableClients.setVisible(isClient);
            tableProduits.setVisible(!isClient);
        });
        tableClients.setVisible(true);
        tableProduits.setVisible(false);

        rechercheField.setOnAction(e -> handleRecherche());
    }

    @FXML
    private void handleRecherche() {
        String q = rechercheField.getText().trim();
        if (q.isEmpty()) return;
        spinner.setVisible(true);

        boolean isClient = radioClient.isSelected();
        String url = isClient ? "/clients?q=" + q : "/produits?q=" + q;

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get(url);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> res = task.getValue();
            List<Map<String, Object>> content = (List<Map<String, Object>>) res.getOrDefault("content", List.of());
            Platform.runLater(() -> {
                spinner.setVisible(false);
                if (isClient) tableClients.setItems(FXCollections.observableArrayList(content));
                else          tableProduits.setItems(FXCollections.observableArrayList(content));
            });
        });
        task.setOnFailed(e -> { Platform.runLater(() -> spinner.setVisible(false)); });
        new Thread(task).start();
    }

    private void afficherDetailClient(Map<String, Object> c) {
        labelDetailNom.setText(c.getOrDefault("prenom", "") + " " + c.getOrDefault("nom", ""));
        labelDetailEmail.setText((String) c.getOrDefault("email", ""));
        labelDetailTel.setText((String) c.getOrDefault("telephone", ""));
        labelDetailPoints.setText(String.valueOf(c.getOrDefault("soldePoints", 0)) + " pts");
        labelDetailFidelite.setText((String) c.getOrDefault("typeFidelite", ""));
        labelDetailCarteFidelite.setText((String) c.getOrDefault("numeroCarteFidelite", ""));
    }
}
