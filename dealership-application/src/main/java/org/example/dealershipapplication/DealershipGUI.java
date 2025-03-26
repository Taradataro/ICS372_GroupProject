package org.example.dealershipapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DealershipGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dealership_gui.fxml"));
            Parent root = loader.load();

            // Create or load a manager (loading JSON first as before)
            DealershipManager manager = jsonExport.loadAllDealers("dealershipsData.json");

            // Alternatively, you can import data from an XML file:
            // Uncomment the next line to import from XML as well
            XMLImporter.importFromXML("path/to/your/file.xml", manager);  // Ensure the path is correct

            // If no dealers were loaded, manager is empty. You can add default dealers if desired.
            if (manager.getAllDealerships().isEmpty()) {
                manager.addDealership(new Dealership("D001", true));
            }

            // Get the controller and set the manager
            DealershipController controller = loader.getController();
            controller.setDealershipManager(manager);

            // Set up the primary stage and show it
            primaryStage.setTitle("Dealership Management System");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
