package com.monprojet.boutiquejeux.controller;

import com.monprojet.boutiquejeux.service.ApiClient;
import com.monprojet.boutiquejeux.util.SessionManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PlanningController {

    @FXML private DatePicker datePicker;
    @FXML private TableView<Map<String, Object>> tablePlanning;
    @FXML private TableColumn<Map<String, Object>, String> colEmploye;
    @FXML private TableColumn<Map<String, Object>, String> colRole;
    @FXML private TableColumn<Map<String, Object>, String> colLundi;
    @FXML private TableColumn<Map<String, Object>, String> colMardi;
    @FXML private TableColumn<Map<String, Object>, String> colMercredi;
    @FXML private TableColumn<Map<String, Object>, String> colJeudi;
    @FXML private TableColumn<Map<String, Object>, String> colVendredi;
    @FXML private ProgressIndicator spinner;

    @FXML
    public void initialize() {
        spinner.setVisible(false);
        datePicker.setValue(LocalDate.now());

        colEmploye.setCellValueFactory(c  -> new SimpleStringProperty((String) c.getValue().getOrDefault("nomEmploye", "")));
        colRole.setCellValueFactory(c     -> new SimpleStringProperty((String) c.getValue().getOrDefault("role", "")));
        colLundi.setCellValueFactory(c    -> new SimpleStringProperty((String) c.getValue().getOrDefault("lundi", "")));
        colMardi.setCellValueFactory(c    -> new SimpleStringProperty((String) c.getValue().getOrDefault("mardi", "")));
        colMercredi.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("mercredi", "")));
        colJeudi.setCellValueFactory(c    -> new SimpleStringProperty((String) c.getValue().getOrDefault("jeudi", "")));
        colVendredi.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue().getOrDefault("vendredi", "")));

        handleCharger();
    }

    @FXML
    private void handleCharger() {
        LocalDate date    = datePicker.getValue();
        Long      magId   = SessionManager.getInstance().getMagasinId();
        spinner.setVisible(true);

        Task<Map<String, Object>> task = new Task<>() {
            @Override protected Map<String, Object> call() throws Exception {
                return ApiClient.getInstance().get("/plannings?magasinId=" + magId + "&semaine=" + date);
            }
        };
        task.setOnSucceeded(e -> {
            Map<String, Object> res = task.getValue();
            List<Map<String, Object>> rows = (List<Map<String, Object>>) res.getOrDefault("content", List.of());
            Platform.runLater(() -> {
                tablePlanning.setItems(FXCollections.observableArrayList(rows));
                spinner.setVisible(false);
            });
        });
        task.setOnFailed(e -> Platform.runLater(() -> spinner.setVisible(false)));
        new Thread(task).start();
    }
}
