module com.monprojet.boutiquejeux {
    // JavaFX 23
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // HTTP + JSON
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // Lombok (compile-time only)
    requires static lombok;

    // Ouvrir les packages aux modules JavaFX / Jackson
    opens com.monprojet.boutiquejeux            to javafx.fxml, javafx.graphics;
    opens com.monprojet.boutiquejeux.controller to javafx.fxml;
    opens com.monprojet.boutiquejeux.model      to com.fasterxml.jackson.databind, javafx.base;
    opens com.monprojet.boutiquejeux.util       to javafx.fxml;
    opens com.monprojet.boutiquejeux.service    to javafx.fxml;

    exports com.monprojet.boutiquejeux;
    exports com.monprojet.boutiquejeux.controller;
    exports com.monprojet.boutiquejeux.service;
    exports com.monprojet.boutiquejeux.model;
    exports com.monprojet.boutiquejeux.util;
}
