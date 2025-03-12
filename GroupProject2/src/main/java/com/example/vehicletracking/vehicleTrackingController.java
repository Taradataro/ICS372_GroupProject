package com.example.vehicletracking;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class vehicleTrackingController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Let start");
    }
}