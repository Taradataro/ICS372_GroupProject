package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hellofx.Vehicle;


public class VehicleDialogController {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField idField;

    @FXML
    private TextField makeField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField typeField;

    // Called by the Main controller to pass a vehicle object to a dialog
    public void setVehicle(Vehicle vehicle) {
        // Bind the vehicle property to the dialog text field
        idField.textProperty().bindBidirectional(vehicle.idProperty());
        typeField.textProperty().bindBidirectional(vehicle.typeProperty());
        makeField.textProperty().bindBidirectional(vehicle.makeProperty());
        modelField.textProperty().bindBidirectional(vehicle.modelProperty());
        priceField.textProperty().bindBidirectional(vehicle.priceProperty());

        // Validate
        Button button = (Button) dialogPane.lookupButton(ButtonType.OK);
        button.addEventFilter(
                ActionEvent.ACTION, event -> {
                    if (!validateData()) {
                        // Validation failed -> prevent the dialog to close
                        event.consume();
                    }
                });
    }

    private boolean validateData() {
        if (idField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter vehicle Id");
            idField.requestFocus();
            return false;
        }

        if (typeField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter vehicle type");
            typeField.requestFocus();
            return false;
        }

        if (makeField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter vehicle make");
            makeField.requestFocus();
            return false;
        }

        if (modelField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter vehicle model");
            modelField.requestFocus();
            return false;
        }

        if (priceField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter vehicle price");
            priceField.requestFocus();
            return false;
        }
        return true;
    }

    // Show alert dialog
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        // Create alert object
        Alert alert = new Alert(alertType);
        // Set all attribute and show
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
