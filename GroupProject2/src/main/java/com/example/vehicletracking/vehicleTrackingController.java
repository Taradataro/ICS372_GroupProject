package com.example.vehicletracking;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class vehicleTrackingController {

    @FXML
    private Label dealerNameLabel;

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

    @FXML
    public void initialize() {
        // Set up the columns for the TableView
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        vehiclePriceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        vehicleMakeColumn.setCellValueFactory(new PropertyValueFactory<>("Make"));
        vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("Model"));

        // Parse XML and populate table
        DealerXMLParser parser = new DealerXMLParser();
        List<Dealer> dealers = parser.parseDealerXML("Dealer.xml");

        if (!dealers.isEmpty()) {
            // Set the dealer's name to the label
            dealerNameLabel.setText(dealers.get(0).getName());

            // Add the vehicles from the first dealer
            vehicleTable.getItems().addAll(dealers.get(0).getVehicles());
        }
    }
}