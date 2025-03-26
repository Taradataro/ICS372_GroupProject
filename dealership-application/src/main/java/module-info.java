module org.example.dealershipapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires json.simple;

    opens org.example.dealershipapplication to javafx.fxml;
    exports org.example.dealershipapplication;
}
