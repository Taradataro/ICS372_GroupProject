package org.example.dealershipapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Updated controller that operates on a DealershipManager (multiple dealers).
 */
public class DealershipController {

    private DealershipManager manager;

    // TableView and TableColumns for vehicle data
    @FXML
    private TableView<Vehicle> vehicleTable;

    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> vehiclePriceColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleMakeColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleModelColumn;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

    public void setDealershipManager(DealershipManager manager) {
        this.manager = manager;
        loadVehicleData("Dealer.xml");
    }

    // Method to load vehicle data from XML file
    private void loadVehicleData(String fileName) {
        try {
            // Load XML file
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(fileName));
            doc.getDocumentElement().normalize();

            NodeList dealerNodes = doc.getElementsByTagName("Dealer");

            // Loop through dealers
            for (int i = 0; i < dealerNodes.getLength(); i++) {
                Node dealerNode = dealerNodes.item(i);
                if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element dealerElement = (Element) dealerNode;
                    NodeList vehicleNodes = dealerElement.getElementsByTagName("Vehicle");

                    // Loop through vehicles
                    for (int j = 0; j < vehicleNodes.getLength(); j++) {
                        Node vehicleNode = vehicleNodes.item(j);
                        if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element vehicleElement = (Element) vehicleNode;

                            String type = vehicleElement.getAttribute("type");
                            String id = vehicleElement.getAttribute("id");
                            String priceStr = vehicleElement.getElementsByTagName("Price").item(0).getTextContent();
                            String make = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
                            String model = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();

                            // Parse the price as a double
                            double price = 0;
                            try {
                                price = Double.parseDouble(priceStr);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid price format for vehicle with ID: " + id);
                                continue;  // Skip this vehicle if the price is invalid
                            }

                            // Default to current date if acquisition date is not available
                            LocalDate acquisitionDate = LocalDate.now();

                            // Create a Vehicle object and add it to the list
                            Vehicle vehicle = new Vehicle(id, make, model, acquisitionDate, price, type);
                            vehicleList.add(vehicle);
                        }
                    }
                }
            }

            // Set up the table columns with the vehicle data
            vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            vehicleMakeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
            vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

            // Add the vehicles to the TableView
            vehicleTable.setItems(vehicleList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddDealer(ActionEvent event) {
        showInputDialog("Add Dealer", "Enter Dealer ID:", dealerId -> {
            showInputDialog("Add Dealer", "Enter Dealer Name:", dealerName -> {
                Dealership newDealer = new Dealership(dealerId, true);
                newDealer.setDealerName(dealerName);
                manager.addDealership(newDealer);

                jsonExport.exportAllDealers(manager, "dealershipsData.json");
                showAlert("Dealer Created",
                        "Dealer '" + dealerName + "' (ID: " + dealerId + ") was added.");
            });
        });
    }

    @FXML
    private void handleAddVehicle(ActionEvent event) {
        // First, ask for which dealer
        showInputDialog("Add Vehicle", "Enter Dealer ID:", dealerId -> {
            Optional<Dealership> dealerOpt = manager.getDealershipById(dealerId);
            if (dealerOpt.isEmpty()) {
                showAlert("Error", "No dealer found with ID: " + dealerId);
                return;
            }
            Dealership dealership = dealerOpt.get();

            // Now show vehicle details dialog
            showVehicleDialog("Add Vehicle", vehicle -> {
                dealership.addVehicle(vehicle);
                jsonExport.exportAllDealers(manager, "dealershipsData.json");
                showAlert("Vehicle Added", "New vehicle added for dealer ID: " + dealerId);
            });
        });
    }

    @FXML
    private void handleLoanVehicle(ActionEvent event) {
        // Prompt for Dealer ID and Vehicle ID
        showInputDialog("Loan Vehicle", "Enter Dealer ID:", dealerId -> {
            Optional<Dealership> dealerOpt = manager.getDealershipById(dealerId);
            if (dealerOpt.isEmpty()) {
                showAlert("Error", "No dealer found with ID: " + dealerId);
                return;
            }
            Dealership dealership = dealerOpt.get();

            showInputDialog("Loan Vehicle", "Enter Vehicle ID:", vehicleId -> {
                dealership.loanVehicle(vehicleId);
                jsonExport.exportAllDealers(manager, "dealershipsData.json");
                showAlert("Vehicle Loaned", "Vehicle " + vehicleId + " loaned (if not sports car).");
            });
        });
    }

    @FXML
    private void handleReturnVehicle(ActionEvent event) {
        showInputDialog("Return Vehicle", "Enter Dealer ID:", dealerId -> {
            Optional<Dealership> dealerOpt = manager.getDealershipById(dealerId);
            if (dealerOpt.isEmpty()) {
                showAlert("Error", "No dealer found with ID: " + dealerId);
                return;
            }
            Dealership dealership = dealerOpt.get();

            showInputDialog("Return Vehicle", "Enter Vehicle ID:", vehicleId -> {
                dealership.returnVehicle(vehicleId);
                jsonExport.exportAllDealers(manager, "dealershipsData.json");
                showAlert("Vehicle Returned", "Vehicle " + vehicleId + " is now available.");
            });
        });
    }

    @FXML
    private void handleTransferVehicle(ActionEvent event) {
        showInputDialog("Transfer Vehicle", "Enter FROM Dealer ID:", fromId -> {
            showInputDialog("Transfer Vehicle", "Enter TO Dealer ID:", toId -> {
                showInputDialog("Transfer Vehicle", "Enter Vehicle ID:", vehicleId -> {
                    boolean success = manager.transferVehicle(vehicleId, fromId, toId);
                    if (success) {
                        jsonExport.exportAllDealers(manager, "dealershipsData.json");
                        showAlert("Transfer Success",
                                "Vehicle " + vehicleId + " transferred from " + fromId + " to " + toId);
                    } else {
                        showAlert("Transfer Failed",
                                "Could not transfer vehicle. Check IDs or receiving status.");
                    }
                });
            });
        });
    }

    @FXML
    private void handlePrintInventory(ActionEvent event) {
        showInputDialog("Print Inventory", "Enter Dealer ID:", dealerId -> {
            Optional<Dealership> dealerOpt = manager.getDealershipById(dealerId);
            if (dealerOpt.isPresent()) {
                dealerOpt.get().printCurrentVehicles();
                showAlert("Inventory Printed", "See console for details of dealer " + dealerId);
            } else {
                showAlert("Error", "No dealer found with ID: " + dealerId);
            }
        });
    }

    @FXML
    private void handleEnableReceiving(ActionEvent event) {
        showInputDialog("Enable Receiving", "Enter Dealer ID:", dealerId -> {
            Optional<Dealership> dealerOpt = manager.getDealershipById(dealerId);
            if (dealerOpt.isPresent()) {
                dealerOpt.get().enableReceiving();
                jsonExport.exportAllDealers(manager, "dealershipsData.json");
                showAlert("Receiving Enabled", "Dealer " + dealerId + " can now receive vehicles.");
            } else {
                showAlert("Error", "No dealer with ID: " + dealerId);
            }
        });
    }

    @FXML
    private void handleDisableReceiving(ActionEvent event) {
        showInputDialog("Disable Receiving", "Enter Dealer ID:", dealerId -> {
            Optional<Dealership> dealerOpt = manager.getDealershipById(dealerId);
            if (dealerOpt.isPresent()) {
                dealerOpt.get().disableReceiving();
                jsonExport.exportAllDealers(manager, "dealershipsData.json");
                showAlert("Receiving Disabled", "Dealer " + dealerId + " can no longer receive vehicles.");
            } else {
                showAlert("Error", "No dealer with ID: " + dealerId);
            }
        });
    }

    @FXML
    private void handleSetDealerName(ActionEvent event) {
        showInputDialog("Set Dealer Name", "Enter Dealer ID:", dealerId -> {
            Optional<Dealership> dealerOpt = manager.getDealershipById(dealerId);
            if (dealerOpt.isEmpty()) {
                showAlert("Error", "No dealer with ID: " + dealerId);
                return;
            }
            Dealership dealership = dealerOpt.get();

            showInputDialog("Set Dealer Name", "Enter new dealer name:", name -> {
                dealership.setDealerName(name);
                jsonExport.exportAllDealers(manager, "dealershipsData.json");
                showAlert("Dealer Name Updated", "Dealer " + dealerId + " name is now: " + name);
            });
        });
    }

    @FXML
    private void handleExit(ActionEvent event) {
        jsonExport.exportAllDealers(manager, "dealershipsData.json");
        System.exit(0);
    }

    // --------------------------------------------------------------------------------
    // Helper methods for dialogs
    // --------------------------------------------------------------------------------

    private void showVehicleDialog(String title, VehicleHandler handler) {
        javafx.scene.control.Dialog<Vehicle> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle(title);

        javafx.scene.control.Label lblId = new javafx.scene.control.Label("Vehicle ID:");
        javafx.scene.control.TextField txtId = new javafx.scene.control.TextField();

        javafx.scene.control.Label lblMake = new javafx.scene.control.Label("Make:");
        javafx.scene.control.TextField txtMake = new javafx.scene.control.TextField();

        javafx.scene.control.Label lblModel = new javafx.scene.control.Label("Model:");
        javafx.scene.control.TextField txtModel = new javafx.scene.control.TextField();

        javafx.scene.control.Label lblType = new javafx.scene.control.Label("Type:");
        javafx.scene.control.TextField txtType = new javafx.scene.control.TextField();

        javafx.scene.control.Label lblPrice = new javafx.scene.control.Label("Price:");
        javafx.scene.control.TextField txtPrice = new javafx.scene.control.TextField();

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(lblId, 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(lblMake, 0, 1);
        grid.add(txtMake, 1, 1);
        grid.add(lblModel, 0, 2);
        grid.add(txtModel, 1, 2);
        grid.add(lblType, 0, 3);
        grid.add(txtType, 1, 3);
        grid.add(lblPrice, 0, 4);
        grid.add(txtPrice, 1, 4);

        dialog.getDialogPane().setContent(grid);

        javafx.scene.control.ButtonType addButtonType =
                new javafx.scene.control.ButtonType("Add", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, javafx.scene.control.ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    double price = Double.parseDouble(txtPrice.getText());
                    return new Vehicle(txtId.getText(), txtMake.getText(), txtModel.getText(),
                            LocalDate.now(), price, txtType.getText());
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid numeric price.");
                }
            }
            return null;
        });

        Optional<Vehicle> result = dialog.showAndWait();
        result.ifPresent(handler::handle);
    }

    private void showInputDialog(String title, String message, InputHandler handler) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(handler::handle);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    interface InputHandler {
        void handle(String input);
    }

    interface VehicleHandler {
        void handle(Vehicle vehicle);
    }
}
