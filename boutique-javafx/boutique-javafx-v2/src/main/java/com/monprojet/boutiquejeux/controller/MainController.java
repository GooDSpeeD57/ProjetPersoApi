package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.MainApp;
import com.monprojet.boutiquejeux.util.AlertHelper;
import com.monprojet.boutiquejeux.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML private Label     labelEmploye;
    @FXML private Label     labelRole;
    @FXML private Label     labelMagasin;
    @FXML private StackPane contentArea;

    // Boutons de navigation sidebar
    @FXML private Button btnDashboard;
    @FXML private Button btnCreerClient;
    @FXML private Button btnVente;
    @FXML private Button btnReprise;
    @FXML private Button btnStock;
    @FXML private Button btnGaranties;
    @FXML private Button btnPlanning;
    @FXML private Button btnRecherche;

    private Button activeButton;

    @FXML
    public void initialize() {
        SessionManager session = SessionManager.getInstance();
        labelEmploye.setText(session.getPrenom());
        labelRole.setText(formatRole(session.getRole()));
        labelMagasin.setText(session.getMagasinNom() != null ? session.getMagasinNom() : "");

        // Masquer les boutons selon le rôle
        if (!session.isManager()) {
            btnPlanning.setVisible(false);
            btnPlanning.setManaged(false);
        }

        // Charger le dashboard par défaut
        handleDashboard();
    }

    @FXML private void handleDashboard()   { navigate("/fxml/Dashboard.fxml",    btnDashboard); }
    @FXML private void handleCreerClient() { navigate("/fxml/CreerClient.fxml",   btnCreerClient); }
    @FXML private void handleVente()       { navigate("/fxml/Vente.fxml",         btnVente); }
    @FXML private void handleReprise()     { navigate("/fxml/Reprise.fxml",       btnReprise); }
    @FXML private void handleStock()       { navigate("/fxml/Stock.fxml",         btnStock); }
    @FXML private void handleGaranties()   { navigate("/fxml/Garanties.fxml",     btnGaranties); }
    @FXML private void handlePlanning()    { navigate("/fxml/Planning.fxml",      btnPlanning); }
    @FXML private void handleRecherche()   { navigate("/fxml/Recherche.fxml",     btnRecherche); }

    @FXML
    private void handleLogout() {
        if (AlertHelper.confirm("Déconnexion", "Voulez-vous vraiment vous déconnecter ?")) {
            SessionManager.getInstance().logout();
            try {
                MainApp.showLogin();
            } catch (IOException e) {
                AlertHelper.error("Erreur", e.getMessage());
            }
        }
    }

    private void navigate(String fxmlPath, Button button) {
        try {
            // Highlight bouton actif
            if (activeButton != null) activeButton.getStyleClass().remove("nav-active");
            button.getStyleClass().add("nav-active");
            activeButton = button;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            AlertHelper.error("Navigation", "Impossible de charger : " + fxmlPath + "\n" + e.getMessage());
        }
    }

    private String formatRole(String role) {
        if (role == null) return "";
        return switch (role) {
            case "ROLE_ADMIN"   -> "Administrateur";
            case "ROLE_MANAGER" -> "Manager";
            case "ROLE_VENDEUR" -> "Vendeur";
            default             -> role;
        };
    }
}
