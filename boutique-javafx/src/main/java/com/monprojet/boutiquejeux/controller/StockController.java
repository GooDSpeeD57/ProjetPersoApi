package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.AlertHelper;
import com.monprojet.boutiquejeux.util.SessionManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockController {

    @FXML private TextField  rechercheField;
    @FXML private ComboBox<String> filtreType;
    @FXML private TableView<Map<String, Object>> tableStock;
    @FXML private TableColumn<Map<String, Object>, String> colNom;
    @FXML private TableColumn<Map<String, Object>, String> colGenre;
    @FXML private TableColumn<Map<String, Object>, String> colType;
    @FXML private TableColumn<Map<String, Object>, String> colQte;
    @FXML private TableColumn<Map<String, Object>, String> colPrix;
    @FXML private TableColumn<Map<String, Object>, String> colAlerte;

    @FXML private Label       labelProduitSelec;
    @FXML private Spinner<Integer> spinnerQte;
    @FXML private Button      btnMettreAJour;
    @FXML private ProgressIndicator spinner;
    @FXML private Label       labelTotalRefs;

    private final ObservableList<Map<String, Object>> stocks = FXCollections.observableArrayList();
    private Map<String, Object> stockSelectionne = null;

    @FXML
    public void initialize() {
        spinner.setVisible(false);

        colNom.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("nomProduit", "")));
        colGenre.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("genre", "")));
        colType.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("typeStock", "")));
        colQte.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getOrDefault("quantite", 0))));
        colPrix.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOrDefault("prix", "—") + " €"));
        colAlerte.setCellValueFactory(c -> {
            int qte = ((Number) c.getValue().getOrDefault("quantite", 99)).intValue();
            int seuil = ((Number) c.getValue().getOrDefault("seuilAlerte", 5)).intValue();
            return new SimpleStringProperty(qte <= seuil ? "⚠️ Stock bas" : "✅ OK");
        });

        // Couleur rouge si stock bas
        tableStock.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { setStyle(""); return; }
                int qte   = ((Number) item.getOrDefault("quantite", 99)).intValue();
                int seuil = ((Number) item.getOrDefault("seuilAlerte", 5)).intValue();
                setStyle(qte <= seuil ? "-fx-background-color: #3d1a1a;" : "");
            }
        });

        tableStock.setItems(stocks);
        tableStock.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            stockSelectionne = n;
            if (n != null) {
                labelProduitSelec.setText((String) n.getOrDefault("nomProduit", "—"));
                int qte = ((Number) n.getOrDefault("quantite", 0)).intValue();
                spinnerQte.getValueFactory().setValue(qte);
            }
        });

        filtreType.setItems(FXCollections.observableArrayList("TOUS", "NEUF", "OCCASION"));
        filtreType.setValue("TOUS");
        filtreType.setOnAction(e -> chargerStock());

        spinnerQte.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9999, 0));

        chargerStock();
    }

    @FXML
    private void handleRecherche() { chargerStock(); }

    private void chargerStock() {
        String q    = rechercheField.getText().trim();
        String type = "TOUS".equals(filtreType.getValue()) ? "" : "&type=" + filtreType.getValue();
        Long magId  = SessionManager.getInstance().getMagasinId();
        spinner.setVisible(true);

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get("/stocks?magasinId=" + magId + "&q=" + q + type);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> res = task.getValue();
            List<Map<String, Object>> content = (List<Map<String, Object>>) res.getOrDefault("content", List.of());
            Platform.runLater(() -> {
                stocks.clear();
                stocks.addAll(content);
                labelTotalRefs.setText("Total : " + content.size() + " référence(s)");
                spinner.setVisible(false);
            });
        });
        task.setOnFailed(e -> { Platform.runLater(() -> spinner.setVisible(false)); });
        new Thread(task).start();
    }

    @FXML
    private void handleMettreAJour() {
        if (stockSelectionne == null) { AlertHelper.warn("Sélection", "Sélectionnez un stock à modifier."); return; }

        Long stockId  = ((Number) stockSelectionne.get("id")).longValue();
        int  nouvelle = spinnerQte.getValue();

        if (!AlertHelper.confirm("Mise à jour stock",
                "Modifier la quantité de « " + stockSelectionne.get("nomProduit") + " » à " + nouvelle + " ?")) return;

        Map<String, Object> body = new HashMap<>();
        body.put("quantite", nouvelle);

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().patch("/stocks/" + stockId, body);
            }
        };
        task.setOnSucceeded(e -> {
            AlertHelper.success("Stock mis à jour", "✅ Quantité modifiée avec succès.");
            chargerStock();
        });
        task.setOnFailed(e -> AlertHelper.error("Erreur", task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML private void handleRefresh() { chargerStock(); }
}
