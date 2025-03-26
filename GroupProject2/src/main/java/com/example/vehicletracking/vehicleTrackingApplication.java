package com.example.vehicletracking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class vehicleTrackingApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(vehicleTrackingApplication.class.getResource("vehicleTracking.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 570, 240);
        stage.setTitle("Vehicle Tracking");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}