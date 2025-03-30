module com.example.vehicletracking {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires javafx.graphics;
    requires javafx.base;

    opens com.example.vehicletracking to javafx.fxml;
    exports com.example.vehicletracking;
}