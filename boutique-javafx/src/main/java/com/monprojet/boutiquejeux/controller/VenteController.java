package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.model.LignePanier;
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
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class VenteController {

    // ── Client ───────────────────────────────────────────────────
    @FXML private TextField     rechercheClientField;
    @FXML private Label         labelClient;
    @FXML private Label         labelPoints;

    // ── Catalogue produits ───────────────────────────────────────
    @FXML private TextField     rechercheProduitField;
    @FXML private ToggleGroup   typeVenteGroup;
    @FXML private RadioButton   radioNeuf;
    @FXML private RadioButton   radioOccasion;
    @FXML private TableView<Map<String, Object>> tableProduits;
    @FXML private TableColumn<Map<String, Object>, String> colNomProduit;
    @FXML private TableColumn<Map<String, Object>, String> colGenre;
    @FXML private TableColumn<Map<String, Object>, String> colPrix;
    @FXML private TableColumn<Map<String, Object>, String> colStock;

    // ── Panier ───────────────────────────────────────────────────
    @FXML private TableView<LignePanier>                 tablePanier;
    @FXML private TableColumn<LignePanier, String>       colPanierNom;
    @FXML private TableColumn<LignePanier, String>       colPanierType;
    @FXML private TableColumn<LignePanier, Integer>      colPanierQte;
    @FXML private TableColumn<LignePanier, String>       colPanierPrix;
    @FXML private Label                                  labelTotal;

    // ── Paiement ─────────────────────────────────────────────────
    @FXML private ComboBox<String>  comboPaiement;
    @FXML private CheckBox          checkPointsFidelite;
    @FXML private Label             labelPointsUtilises;
    @FXML private Button            btnValiderVente;
    @FXML private ProgressIndicator spinner;

    private Long   clientId    = null;
    private String clientEmail = null;
    private int    clientPoints = 0;

    private final ObservableList<Map<String, Object>> produits = FXCollections.observableArrayList();
    private final ObservableList<LignePanier>          panier  = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        spinner.setVisible(false);

        // Table produits
        colNomProduit.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("nom", "")));
        colGenre.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("genre", "")));
        colPrix.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOrDefault("prix", "") + " €"));
        colStock.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getOrDefault("stock", 0))));
        tableProduits.setItems(produits);

        // Table panier
        colPanierNom.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        colPanierType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPanierQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colPanierPrix.setCellValueFactory(new PropertyValueFactory<>("prixAffiche"));
        tablePanier.setItems(panier);

        // Paiement
        comboPaiement.setItems(FXCollections.observableArrayList("CARTE", "ESPECES", "CHEQUE"));
        comboPaiement.setValue("CARTE");

        checkPointsFidelite.setDisable(true);

        chargerProduits("");
    }

    // ── Recherche client ─────────────────────────────────────────

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
            Map<String, Object> client = task.getValue();
            clientId     = ((Number) client.get("id")).longValue();
            clientEmail  = (String) client.get("email");
            clientPoints = (int) client.getOrDefault("soldePoints", 0);
            Platform.runLater(() -> {
                labelClient.setText(client.get("prenom") + " " + client.get("nom") + " — " + clientEmail);
                labelPoints.setText("Points fidélité : " + clientPoints);
                checkPointsFidelite.setDisable(clientPoints < 100);
            });
        });
        task.setOnFailed(e -> AlertHelper.error("Client introuvable", task.getException().getMessage()));
        new Thread(task).start();
    }

    // ── Recherche produits ───────────────────────────────────────

    @FXML
    private void handleRechercheProduit() {
        chargerProduits(rechercheProduitField.getText().trim());
    }

    private void chargerProduits(String q) {
        boolean occasion = radioOccasion != null && radioOccasion.isSelected();
        String url = "/produits?q=" + q + "&type=" + (occasion ? "OCCASION" : "NEUF") + "&magasinId=" + SessionManager.getInstance().getMagasinId();

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get(url);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> res = task.getValue();
            List<Map<String, Object>> content = (List<Map<String, Object>>) res.getOrDefault("content", List.of());
            Platform.runLater(() -> { produits.clear(); produits.addAll(content); });
        });
        task.setOnFailed(e -> {/* silencieux */});
        new Thread(task).start();
    }

    // ── Panier ───────────────────────────────────────────────────

    @FXML
    private void handleAjouterPanier() {
        Map<String, Object> produit = tableProduits.getSelectionModel().getSelectedItem();
        if (produit == null) { AlertHelper.warn("Sélection", "Sélectionnez un produit."); return; }

        Long   id    = ((Number) produit.get("id")).longValue();
        String nom   = (String) produit.get("nom");
        String type  = radioOccasion.isSelected() ? "OCCASION" : "NEUF";
        double prix  = ((Number) produit.getOrDefault("prix", 0)).doubleValue();

        // Vérifier si déjà dans le panier
        panier.stream().filter(l -> l.getProduitId().equals(id) && l.getType().equals(type))
              .findFirst()
              .ifPresentOrElse(
                  l -> l.setQuantite(l.getQuantite() + 1),
                  () -> panier.add(new LignePanier(id, nom, type, 1, prix))
              );

        tablePanier.refresh();
        mettreAJourTotal();
    }

    @FXML
    private void handleRetirerPanier() {
        LignePanier ligne = tablePanier.getSelectionModel().getSelectedItem();
        if (ligne != null) { panier.remove(ligne); mettreAJourTotal(); }
    }

    private void mettreAJourTotal() {
        double total = panier.stream().mapToDouble(l -> l.getPrix() * l.getQuantite()).sum();
        labelTotal.setText(String.format("Total : %.2f €", total));
    }

    // ── Valider la vente ─────────────────────────────────────────

    @FXML
    private void handleValiderVente() {
        if (panier.isEmpty()) { AlertHelper.warn("Panier vide", "Ajoutez au moins un produit."); return; }
        if (clientId == null) { AlertHelper.warn("Client", "Identifiez d'abord le client."); return; }

        if (!AlertHelper.confirm("Confirmer la vente",
                "Valider la vente pour " + labelClient.getText() + " ?\n" + labelTotal.getText())) return;

        setLoading(true);

        List<Map<String, Object>> lignes = panier.stream().map(l -> {
            Map<String, Object> m = new HashMap<>();
            m.put("produitId",  l.getProduitId());
            m.put("quantite",   l.getQuantite());
            m.put("typeVente",  l.getType());
            return m;
        }).toList();

        Map<String, Object> body = new HashMap<>();
        body.put("clientId",           clientId);
        body.put("magasinId",          SessionManager.getInstance().getMagasinId());
        body.put("modePaiement",       comboPaiement.getValue());
        body.put("utiliserPoints",     checkPointsFidelite.isSelected());
        body.put("lignes",             lignes);

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().post("/factures", body);
            }
        };
        task.setOnSucceeded(e -> {
            setLoading(false);
            Map<String, Object> facture = task.getValue();
            AlertHelper.success("Vente enregistrée",
                    "✅ Facture n°" + facture.get("id") + " créée !\nPoints gagnés : " + facture.getOrDefault("pointsGagnes", 0));
            panier.clear();
            mettreAJourTotal();
            clientId = null; clientEmail = null;
            Platform.runLater(() -> { labelClient.setText("Aucun client sélectionné"); labelPoints.setText(""); rechercheClientField.clear(); });
        });
        task.setOnFailed(e -> { setLoading(false); AlertHelper.error("Erreur vente", task.getException().getMessage()); });
        new Thread(task).start();
    }

    private void setLoading(boolean b) {
        Platform.runLater(() -> { btnValiderVente.setDisable(b); spinner.setVisible(b); });
    }
}
