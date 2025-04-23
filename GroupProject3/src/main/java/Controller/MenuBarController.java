package Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.hellofx.Dealer;
import org.example.hellofx.JSONFileHandler;

import java.io.File;
import java.util.List;

public class MenuBarController {
    private Stage stage;

    // Extension filters
    private final FileChooser.ExtensionFilter xmlExtension = new FileChooser.ExtensionFilter("XML Files", "*.xml");
    private final FileChooser.ExtensionFilter jsonExtension = new FileChooser.ExtensionFilter("JSON Files", "*.json");
    private final FileChooser.ExtensionFilter allFile = new FileChooser.ExtensionFilter("All Files", "*.*");

    // Set the application stage (use this method if you're setting the controller manually or via FXMLLoader)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Method to open a file
    public String openFile(Label outputLabel) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(xmlExtension, jsonExtension, allFile);
        fileChooser.setTitle("Import XML or JSON data");

        File safeDefaultDir = new File(System.getProperty("user.home"));
        if (safeDefaultDir.exists() && safeDefaultDir.isDirectory()) {
            fileChooser.setInitialDirectory(safeDefaultDir);
        }

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("File selected");
            System.out.println(selectedFile.getPath());
            outputLabel.setText("You selected " + selectedFile.getName());
            return selectedFile.getPath();
        } else {
            System.out.println("File selection cancelled");
            return "src/main/resources/Dealer.xml"; // Optional: default fallback path
        }
    }

    // Method to save a file
    public void saveFile(Label outputLabel, List<Dealer> dealers) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(jsonExtension, allFile, xmlExtension);
        fileChooser.setTitle("Save As");

        File safeDefaultDir = new File(System.getProperty("user.home"));
        if (safeDefaultDir.exists() && safeDefaultDir.isDirectory()) {
            fileChooser.setInitialDirectory(safeDefaultDir);
        }

        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            System.out.println("File selected");
            System.out.println(selectedFile.getPath());
            outputLabel.setText("You have saved " + selectedFile.getName() + " successfully");
            JSONFileHandler.saveAsJson(dealers, selectedFile.getPath());
        } else {
            System.out.println("File selection cancelled");
        }
    }

    // Method to exit the application
    public void exit(AnchorPane scenePane) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        alert.setHeaderText("You're about to exit the app!");
        alert.setContentText("Make sure you save before exiting.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            System.out.println("Successfully exited");
            stage.close();
        }
    }
}
