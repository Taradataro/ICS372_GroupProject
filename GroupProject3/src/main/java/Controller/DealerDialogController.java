package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.hellofx.Dealer;

public class DealerDialogController {

    @FXML private DialogPane dialogPane;
    @FXML private TextField idField, nameField;

    public void setDealer(Dealer dealer) {
        idField.  textProperty().bindBidirectional(dealer.idProperty());
        nameField.textProperty().bindBidirectional(dealer.nameProperty());

        Button ok = (Button) dialogPane.lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, e -> {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "❗ Form Error", "Please fill both ID & Name!");
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
