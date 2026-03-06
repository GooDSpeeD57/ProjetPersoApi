package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.MainApp;
import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.AlertHelper;
import com.monprojet.boutiquejeux.util.SessionManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.util.Map;

public class LoginController {

    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button        loginButton;
    @FXML private Label         errorLabel;
    @FXML private ProgressIndicator spinner;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        spinner.setVisible(false);

        // Enter sur le champ mot de passe → login
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleLogin();
        });
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String pwd   = passwordField.getText();

        if (email.isEmpty() || pwd.isEmpty()) {
            showError("Email et mot de passe obligatoires.");
            return;
        }

        setLoading(true);

        Task<Map<String, Object>> task = new Task<>() {
            @Override
            protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().postPublic("/auth/login", Map.of(
                    "email",      email,
                    "motDePasse", pwd,
                    "userType",   "EMPLOYE"
                ));
            }
        };

        task.setOnSucceeded(e -> {
            setLoading(false);
            Map<String, Object> result = task.getValue();
            String jwt  = (String) result.get("accessToken");
            String role = (String) result.get("role");

            // Vérifier que c'est bien un employé
            if (!role.startsWith("ROLE_VENDEUR") && !role.startsWith("ROLE_MANAGER") && !role.startsWith("ROLE_ADMIN")) {
                showError("Accès réservé aux employés.");
                return;
            }

            SessionManager.getInstance().login(jwt, email, role);

            try {
                MainApp.showMain();
            } catch (Exception ex) {
                showError("Erreur d'ouverture de l'interface : " + ex.getMessage());
            }
        });

        task.setOnFailed(e -> {
            setLoading(false);
            Throwable ex = task.getException();
            showError(ex.getMessage() != null ? ex.getMessage() : "Connexion impossible.");
        });

        new Thread(task).start();
    }

    private void showError(String msg) {
        Platform.runLater(() -> {
            errorLabel.setText(msg);
            errorLabel.setVisible(true);
        });
    }

    private void setLoading(boolean loading) {
        Platform.runLater(() -> {
            loginButton.setDisable(loading);
            spinner.setVisible(loading);
            errorLabel.setVisible(false);
        });
    }
}
