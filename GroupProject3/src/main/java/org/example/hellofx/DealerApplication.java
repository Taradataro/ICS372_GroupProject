package org.example.hellofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class DealerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Load the fxml file to the root
            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));

            // Create a Scene by passing the root
            Scene scene = new Scene(root);

            stage.setTitle("Dealership Management System"); // Set the title of the stage
            // Set the icon of the stage
            stage.getIcons().add(new Image("icon.jpg"));
            // Add the scene to the stage
            stage.setScene(scene);
            stage.show(); // show the stage

            stage.setOnCloseRequest(windowEvent -> {
                windowEvent.consume();
                exitProgram(stage);
            });
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Can't open the file");
        }
    }

    // Ask for confirmation when we try to exit the program
    public void exitProgram(Stage stage) {
        // Create an alert window to ask for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit???");
        alert.setHeaderText("You're about to exit the app!");
        alert.setContentText("Make sure you save before existing?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("You're successfully logout");
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}