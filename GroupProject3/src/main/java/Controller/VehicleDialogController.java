package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hellofx.Vehicle;

public class VehicleDialogController {

    @FXML private DialogPane dialogPane;
    @FXML private TextField idField, typeField, makeField, modelField, priceField;

    public void setVehicle(Vehicle vehicle) {
        idField.   textProperty().bindBidirectional(vehicle.idProperty());
        typeField. textProperty().bindBidirectional(vehicle.typeProperty());
        makeField. textProperty().bindBidirectional(vehicle.makeProperty());
        modelField.textProperty().bindBidirectional(vehicle.modelProperty());
        priceField.textProperty().bindBidirectional(vehicle.priceProperty());

        Button ok = (Button) dialogPane.lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, e -> {
            if (idField.getText().isEmpty() ||
                    typeField.getText().isEmpty() ||
                    makeField.getText().isEmpty() ||
                    modelField.getText().isEmpty() ||
                    priceField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "❗ Form Error", "All fields must be filled!");
                e.consume();
            }
        });
    }

    private void showAlert(Alert.AlertType t, String title, String msg) {
        Alert a = new Alert(t);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
