module org.example.hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    requires kotlin.stdlib;

    opens org.example.hellofx to javafx.fxml;
    exports org.example.hellofx;
    exports Controller;
    opens Controller to javafx.fxml;
}
