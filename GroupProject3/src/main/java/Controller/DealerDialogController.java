package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hellofx.Dealer;

public class DealerDialogController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private DialogPane dialogPane;

    // Called by main controller to pass a dealer object to a dialog
    public void setDealer(Dealer dealer) {
        // Bind the dealer property to dialog text field
        idField.textProperty().bindBidirectional(dealer.idProperty());
        nameField.textProperty().bindBidirectional(dealer.nameProperty());

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
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter dealer Id");
            idField.requestFocus();
            return false;
        }

        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter dealer name");
            nameField.requestFocus();
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
