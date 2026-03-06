package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.AlertHelper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CreerClientController {

    @FXML private TextField     pseudoField;
    @FXML private TextField     nomField;
    @FXML private TextField     prenomField;
    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField     telephoneField;
    @FXML private DatePicker    dateNaissancePicker;
    @FXML private CheckBox      rgpdCheck;
    @FXML private Button        btnCreer;
    @FXML private Label         resultLabel;
    @FXML private ProgressIndicator spinner;

    private static final Pattern EMAIL_RE = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PWD_RE   = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\-_])[A-Za-z\\d@$!%*?&\\-_]{8,}$");

    @FXML
    public void initialize() {
        spinner.setVisible(false);
        resultLabel.setVisible(false);
        // Date max = aujourd'hui - 18 ans
        dateNaissancePicker.setValue(LocalDate.now().minusYears(18));
    }

    @FXML
    private void handleCreer() {
        if (!valider()) return;

        setLoading(true);

        Map<String, Object> body = new HashMap<>();
        body.put("pseudo",        pseudoField.getText().trim());
        body.put("nom",           nomField.getText().trim());
        body.put("prenom",        prenomField.getText().trim());
        body.put("email",         emailField.getText().trim().toLowerCase());
        body.put("motDePasse",    passwordField.getText());
        body.put("telephone",     telephoneField.getText().trim());
        body.put("dateNaissance", dateNaissancePicker.getValue().toString());
        body.put("rgpdConsent",   rgpdCheck.isSelected());

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().post("/auth/inscription", body);
            }
        };

        task.setOnSucceeded(e -> {
            setLoading(false);
            Map<String, Object> res = task.getValue();
            String email = (String) res.getOrDefault("email", emailField.getText());
            showSuccess("✅ Compte créé pour " + email + " — 10 points de fidélité offerts !");
            clearForm();
        });

        task.setOnFailed(e -> {
            setLoading(false);
            showError("❌ " + task.getException().getMessage());
        });

        new Thread(task).start();
    }

    @FXML private void handleReset() { clearForm(); }

    private boolean valider() {
        String email  = emailField.getText().trim();
        String pwd    = passwordField.getText();
        String pseudo = pseudoField.getText().trim();
        LocalDate dob = dateNaissancePicker.getValue();

        if (pseudo.isEmpty() || nomField.getText().isBlank() || prenomField.getText().isBlank()) {
            showError("Pseudo, nom et prénom sont obligatoires."); return false;
        }
        if (!EMAIL_RE.matcher(email).matches()) {
            showError("Email invalide."); return false;
        }
        if (!PWD_RE.matcher(pwd).matches()) {
            showError("Mot de passe : 8 car. min, maj, min, chiffre et caractère spécial (@$!%*?&-_)."); return false;
        }
        if (dob == null || dob.isAfter(LocalDate.now().minusYears(18))) {
            showError("Le client doit avoir au moins 18 ans."); return false;
        }
        if (!rgpdCheck.isSelected()) {
            showError("Le consentement RGPD est obligatoire."); return false;
        }
        return true;
    }

    private void clearForm() {
        pseudoField.clear(); nomField.clear(); prenomField.clear();
        emailField.clear(); passwordField.clear(); telephoneField.clear();
        dateNaissancePicker.setValue(LocalDate.now().minusYears(18));
        rgpdCheck.setSelected(false);
    }

    private void setLoading(boolean b) {
        Platform.runLater(() -> { btnCreer.setDisable(b); spinner.setVisible(b); });
    }
    private void showError(String msg) {
        Platform.runLater(() -> { resultLabel.setText(msg); resultLabel.setStyle("-fx-text-fill: #e74c3c;"); resultLabel.setVisible(true); });
    }
    private void showSuccess(String msg) {
        Platform.runLater(() -> { resultLabel.setText(msg); resultLabel.setStyle("-fx-text-fill: #2ecc71;"); resultLabel.setVisible(true); });
    }
}
