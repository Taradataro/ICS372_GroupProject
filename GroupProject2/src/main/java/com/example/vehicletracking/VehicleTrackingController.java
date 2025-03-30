package com.example.vehicletracking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class VehicleTrackingController {

    @FXML private Label dealerNameLabel;
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, String> vehicleTypeColumn;
    @FXML private TableColumn<Vehicle, String> vehiclePriceColumn;
    @FXML private TableColumn<Vehicle, String> vehicleMakeColumn;
    @FXML private TableColumn<Vehicle, String> vehicleModelColumn;
    @FXML private TableColumn<Vehicle, String> vehicleStatusColumn;

    private List<Dealer> dealers;
    private Dealer currentDealer;
    private final DealerXMLParser xmlParser = new DealerXMLParser();
    private final DealerJSONParser jsonParser = new DealerJSONParser();

    @FXML
    public void initialize() {
        // Set up table columns
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        vehicleMakeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        vehicleStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load initial data from XML
        loadXMLData();
    }

    private void loadXMLData() {
        dealers = xmlParser.parseDealerXML("Dealer.xml");
        if (!dealers.isEmpty()) {
            currentDealer = dealers.get(0);
            updateUI();
        }
    }

    private void loadJSONData() {
        List<Vehicle> jsonVehicles = jsonParser.parseCarInventoryJSON("carinv.json");
        if (!jsonVehicles.isEmpty()) {
            // Create a new dealer for JSON data
            Dealer jsonDealer = new Dealer("JSON Dealer");
            jsonDealer.setVehicles(jsonVehicles);

            // Keep existing XML dealers if they exist
            List<Dealer> xmlDealers = dealers.stream()
                    .filter(d -> !d.getName().equals("JSON Dealer"))
                    .collect(Collectors.toList());

            xmlDealers.add(jsonDealer);
            dealers = xmlDealers;
            currentDealer = jsonDealer;
            updateUI();
        }
    }

    private void updateUI() {
        dealerNameLabel.setText(currentDealer.getName());
        vehicleTable.getItems().setAll(currentDealer.getVehicles());
    }

    @FXML
    public void handleAddVehicle() {
        // Create a dialog for vehicle details
        TextInputDialog typeDialog = new TextInputDialog();
        typeDialog.setTitle("Add New Vehicle");
        typeDialog.setHeaderText("Enter vehicle type:");
        Optional<String> typeResult = typeDialog.showAndWait();

        // If user entered a valid type, continue asking for the other details
        typeResult.ifPresent(type -> {
            // Ask for the vehicle price
            TextInputDialog priceDialog = new TextInputDialog();
            priceDialog.setTitle("Enter Vehicle Price");
            priceDialog.setHeaderText("Enter the price of the vehicle:");
            Optional<String> priceResult = priceDialog.showAndWait();

            // Proceed if the user entered a price
            priceResult.ifPresent(price -> {
                // Ask for the vehicle make
                TextInputDialog makeDialog = new TextInputDialog();
                makeDialog.setTitle("Enter Vehicle Make");
                makeDialog.setHeaderText("Enter the make of the vehicle:");
                Optional<String> makeResult = makeDialog.showAndWait();

                // Proceed if the user entered a make
                makeResult.ifPresent(make -> {
                    // Ask for the vehicle model (or any other property you wish to collect)
                    TextInputDialog modelDialog = new TextInputDialog();
                    modelDialog.setTitle("Enter Vehicle Model");
                    modelDialog.setHeaderText("Enter the model of the vehicle:");
                    Optional<String> modelResult = modelDialog.showAndWait();

                    // Proceed if the user entered a model
                    modelResult.ifPresent(model -> {
                        // Generate a new unique ID for the vehicle
                        String newId = "V" + (currentDealer.getVehicles().size() + 1000);

                        // Create a new Vehicle object with the user inputs
                        Vehicle newVehicle = new Vehicle(type, newId, price, make, model);

                        // Add the new vehicle to the dealer and the table
                        currentDealer.addVehicle(newVehicle);
                        vehicleTable.getItems().add(newVehicle);
                        saveChanges();
                    });
                });
            });
        });
    }

    @FXML
    public void handleTransferVehicle() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No vehicle selected for transfer.");
            return;
        }

        // Remove from current dealer
        currentDealer.getVehicles().remove(selected);

        // Get the other data source (XML or JSON)
        if (currentDealer.getName().equals("JSON Dealer")) {
            // Transfer to XML
            transferToXML(selected);
        } else {
            // Transfer to JSON
            transferToJSON(selected);
        }

        // Save the changes
        saveChanges();

        // Update UI
        updateUI();
        showAlert("Vehicle transferred successfully.");
    }

    private void transferToXML(Vehicle vehicle) {
        // Find the XML dealer (assuming it's the first one in the list)
        Dealer xmlDealer = dealers.stream()
                .filter(d -> !d.getName().equals("JSON Dealer"))
                .findFirst()
                .orElse(null);

        if (xmlDealer != null) {
            xmlDealer.addVehicle(vehicle);
            // Remove from the JSON dealer if it exists
            Dealer jsonDealer = dealers.stream()
                    .filter(d -> d.getName().equals("JSON Dealer"))
                    .findFirst()
                    .orElse(null);
            if (jsonDealer != null) {
                jsonDealer.getVehicles().remove(vehicle);
            }
        }
    }

    private void transferToJSON(Vehicle vehicle) {
        // Find or create the JSON dealer
        Dealer jsonDealer = dealers.stream()
                .filter(d -> d.getName().equals("JSON Dealer"))
                .findFirst()
                .orElseGet(() -> {
                    Dealer newDealer = new Dealer("JSON Dealer");
                    dealers.add(newDealer);
                    return newDealer;
                });

        jsonDealer.addVehicle(vehicle);

        // Remove from the XML dealer if it exists
        Dealer xmlDealer = dealers.stream()
                .filter(d -> !d.getName().equals("JSON Dealer"))
                .findFirst()
                .orElse(null);

        if (xmlDealer != null) {
            xmlDealer.getVehicles().remove(vehicle);
        }
    }

    private void saveChanges() {
        // Save all dealers to XML
        xmlParser.saveDealerXML("Dealer.xml", dealers);

        // Save all vehicles from all dealers to JSON
        List<Vehicle> allVehicles = dealers.stream()
                .flatMap(dealer -> dealer.getVehicles().stream())
                .collect(Collectors.toList());
        jsonParser.saveToInfJson(allVehicles, "inf.json");
    }
    private Dealer getTargetDealer() {
        // Simple logic to find the other dealer
        if (dealers.size() > 1) {
            return dealers.stream().filter(dealer -> dealer != currentDealer).findFirst().orElse(null);
        }
        return null;
    }

    @FXML
    public void handleSwitchDataSource() {
        if (currentDealer.getName().equals("JSON Dealer")) {
            loadXMLData();
        } else {
            loadJSONData();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleloanVehicle(ActionEvent actionEvent) {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Normalize the type for comparison
            String vehicleType = selected.getType().toLowerCase().trim();

            // Check if the vehicle is a sports car
            if ("sports car".equals(vehicleType)) {
                showLoanRestrictionAlert();
            } else if ("unavailable".equalsIgnoreCase(selected.getStatus())) {
                showAlert("This vehicle is already on loan.");
            } else {
                // Set the status to "Unavailable" and refresh the table
                selected.setStatus("Unavailable");
                vehicleTable.refresh();
            }
        } else {
            showAlert("No vehicle selected for loan.");
        }
    }

    private void showLoanRestrictionAlert() {
        Alert alert = new Alert(AlertType.WARNING); // Using WARNING instead of INFORMATION
        alert.setTitle("Loan Restriction");
        alert.setHeaderText("Sports Car Loan Policy");
        alert.setContentText("Sports cars are not available for loan at this dealership.");
        alert.showAndWait();
    }

    @FXML
    public void handleReturnVehicle(ActionEvent actionEvent) {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Available");
            vehicleTable.refresh();
        } else {
            showAlert("No vehicle selected for return.");
        }
    }
}