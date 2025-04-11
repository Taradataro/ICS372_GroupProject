module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.web;


    opens org.example to javafx.fxml;
    exports org.example;
    exports Controller;
    opens Controller to javafx.fxml;
}