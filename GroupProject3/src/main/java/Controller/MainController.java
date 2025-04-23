package Controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.example.hellofx.*;


import java.net.URL;
import java.util.*;

enum DialogMode {ADD, UPDATE}

public class MainController implements Initializable {
    // Pane
    @FXML private AnchorPane scenePane;
    @FXML private Label outputLabel;
    // Menu bar
    @FXML private Label myLabel;
    @FXML private ChoiceBox<String> dealerChoiceBox;
    @FXML private Button updateButton;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button transferButton;
    @FXML private Button rentButton;
    @FXML private Button returnButton;
    // Table view
    @FXML private TableView vehicleTable;
    @FXML private TableColumn<Vehicle, String> typeCol;
    @FXML private TableColumn<Vehicle, String> idCol;
    @FXML private TableColumn<Vehicle, String> makeCol;
    @FXML private TableColumn<Vehicle, String> modelCol;
    @FXML private TableColumn<Vehicle, String> priceCol;
    @FXML private TableColumn<Vehicle, String> statusCol;
    //Filter options
    @FXML private ComboBox<String> typeFilterComboBox;
    @FXML private ComboBox<String> makeFilterComboBox;
    @FXML private ComboBox<String> statusFilterComboBox;

    List<Dealer> dealers;
    private Dealer currentDealer;

    ObservableList<Vehicle> dealersOL = null;

    // Auto called when the view is created
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set default value
        dealerChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String s) { return (s == null) ? "Select Dealer" : s; }

            @Override
            public String fromString(String s) { return null; }
        });
        // Handling when user select dealer -> load the table based on selection
        dealerChoiceBox.setOnAction(this::handleSelectDealer);

        // Build the table
        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol);

        // If no vehicle selected then disable update, delete, transfer, rent, return button buttons
        deleteButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        updateButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        transferButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        rentButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        returnButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );
    }

    // Handle when user select open file
    @FXML
    void handleOpenFile(ActionEvent event) {
        System.out.println("Opening the file");
        String filePath = new MenuBarController().openFile(outputLabel);
        // Instantiate new dealers and get the dealers from given file
        dealers = DealerSingleton.getDealers(filePath);

        // Create a list to store all the dealers name
        List<String> namesList = new ArrayList<>();
        for (Dealer dealer : dealers) {
            namesList.add(dealer.getName());

        }
        // Make the name list observable so it auto update the UI
        ObservableList<String> dealerNameOL = FXCollections.observableArrayList(namesList);
        // Choice box switch dealer: Adding all dealer name to the choiceList
        dealerChoiceBox.setItems(dealerNameOL);
    }

    // When user click save option from menu
    @FXML
    void handleSaveFile(ActionEvent event) {
        System.out.println("Saving the file");
        new MenuBarController().saveFile(outputLabel, dealers);
    }

    // When user select close option from menu
    @FXML
    void handleCloseApp(ActionEvent event) {
        System.out.println("Closing the file");
        new MenuBarController().exit(scenePane);
    }

    // Handle when user select vehicle from the Choice Box
    void handleSelectDealer(ActionEvent event) {
        // Get the value from the user choice
        String dealerName = dealerChoiceBox.getValue(); // get dealer name
        myLabel.setText(dealerName); // Change the label based on selected dealer
        // Loop through each dealer
        for (int i = 0; i < dealers.size(); i++) {
            if (!dealers.isEmpty()) {
                currentDealer = dealers.get(i);
                dealersOL = FXCollections.observableArrayList(currentDealer.getVehicles());
                // If their name matches -> add the data to the table
                if (currentDealer.getName().equals(dealerName)) {
                    vehicleTable.setItems(dealersOL);
                    // Populate filter dropdowns based on current dealer's vehicles
                    Set<String> types = new HashSet<>();
                    Set<String> makes = new HashSet<>();
                    Set<String> statuses = new HashSet<>();

                    for (Vehicle v : currentDealer.getVehicles()) {
                        types.add(v.getType());
                        makes.add(v.getMake());
                        statuses.add(v.getStatus());
                    }
                    typeFilterComboBox.setItems(FXCollections.observableArrayList(types));
                    makeFilterComboBox.setItems(FXCollections.observableArrayList(makes));
                    statusFilterComboBox.setItems(FXCollections.observableArrayList(statuses));
                    // Clear previous selections
                    typeFilterComboBox.setValue(null);
                    makeFilterComboBox.setValue(null);
                    statusFilterComboBox.setValue(null);

                    // Add change listeners to trigger filtering
                    typeFilterComboBox.setOnAction(e -> applyFilters());
                    makeFilterComboBox.setOnAction(e -> applyFilters());
                    statusFilterComboBox.setOnAction(e -> applyFilters());
                    // Apply filters in case default values are already selected
                    applyFilters();
                    return; // Stop the loop when the dealer is found -> prevent create a new instance of ObservablesList
                }
            }
        }
    }
    //Handle user to select option to filter
    private void applyFilters() {
        if (currentDealer == null) return;

        String selectedType = typeFilterComboBox.getValue();
        String selectedMake = makeFilterComboBox.getValue();
        String selectedStatus = statusFilterComboBox.getValue();

        List<Vehicle> filtered = new ArrayList<>();
        for (Vehicle v : currentDealer.getVehicles()) {
            boolean matchesType = (selectedType == null || selectedType.isEmpty() || v.getType().equals(selectedType));
            boolean matchesMake = (selectedMake == null || selectedMake.isEmpty() || v.getMake().equals(selectedMake));
            boolean matchesStatus = (selectedStatus == null || selectedStatus.isEmpty() || v.getStatus().equals(selectedStatus));

            if (matchesType && matchesMake && matchesStatus) {
                filtered.add(v);
            }
        }

        dealersOL = FXCollections.observableArrayList(filtered);
        vehicleTable.setItems(dealersOL);
    }


    @FXML
    void handleAddVehicle(ActionEvent event) {
        handleUpdateVehicle(event);
    }

    // Handle update vehicle
    @FXML
    void handleUpdateVehicle(ActionEvent event) {
        Vehicle vehicle = null;
        String dialogTitle = "";
        DialogMode mode;

        if (event.getSource().equals(updateButton)) {
            mode = DialogMode.UPDATE;
            dialogTitle = "Update Vehicle";
            vehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        } else if (event.getSource().equals(addButton)) {
            mode = DialogMode.ADD;
            dialogTitle = "Add Vehicle";
            vehicle = new Vehicle();
        }
        else {
            return;
        }
        try {
            // Load the fxml file and create a new popup dialog
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/hellofx/VehicleDialog.fxml"));
            DialogPane vehicleDialogPane = fxmlLoader.load();

            // Get the vehicle controller
            VehicleDialogController vehicleDialogController = fxmlLoader.getController();

            vehicleDialogController.setVehicle(vehicle);

            // Create a dialog button type
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(vehicleDialogPane);
            dialog.setTitle(dialogTitle);
            // Option button to handle event
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            // When user click OK
            if (clickedButton.get() == ButtonType.OK) {
                System.out.println("User clicked OK");
                if (mode == DialogMode.ADD) {
                    if (currentDealer != null) {
                        currentDealer.getVehicles().add(vehicle);
                        dealersOL.add(vehicle);
                    } else {
                        System.out.println("No dealer selected.");
                        showAlertMessage(Alert.AlertType.WARNING, "No dealer selected", "Please select dealer before adding new vehicle");
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle when user delete vehicle
    @FXML
    void handleDeleteVehicle(ActionEvent event) {
        // Select vehicle from the table
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            showAlertMessage(Alert.AlertType.CONFIRMATION, "Deleting vehicle", "Are you sure you want to delete selected vehicle?");
            currentDealer.getVehicles().remove(selectedVehicle);
            dealersOL.remove(selectedVehicle);
            // Clear the selection
            vehicleTable.getSelectionModel().clearSelection();
            System.out.printf("Deleted: %s\n", selectedVehicle);
        } else {
            System.out.println("No vehicle selected to delete.");
        }
    }

    // Handle when user select rent button
    @FXML
    void handleRentVehicle(ActionEvent event) {
        // Select vehicle from the table
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        // If the vehicle is not sport and not null
        if (selectedVehicle != null && !selectedVehicle.getType().equalsIgnoreCase("sports car")) {
            if (selectedVehicle.getStatus().equalsIgnoreCase("Available")) {
                selectedVehicle.setStatus("Rented");
                vehicleTable.refresh();
                System.out.println("Vehicle rented: " + selectedVehicle);
            } else {
                System.out.println("This vehicle is not available for rented");
            }
        } else {
            System.out.println("Can not rent sport car.");
            showAlertMessage(Alert.AlertType.ERROR, "Car type error", "A sport car can not be rented");
        }

    }

    @FXML
    void handleReturnVehicle(ActionEvent event) {
        // Select vehicle from the table
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            if (selectedVehicle.getStatus().equalsIgnoreCase("Rented")) {
                selectedVehicle.setStatus("Available");
                vehicleTable.refresh();
                System.out.println("Vehicle returned: " + selectedVehicle);
            } else {
                System.out.println("This vehicle is not currently rented.");
                showAlertMessage(Alert.AlertType.ERROR,"Error", "This vehicle is not currently rented");
            }
        } else {
            System.out.println("No vehicle selected to return.");
        }
    }


    @FXML
    void handleTransferVehicle(ActionEvent event) {
        // Get the selected vehicle
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            // Create a ChoiceDialog to select the target dealer
            List<String> dealerNames = new ArrayList<>();
            for (Dealer dealer : dealers) {
                dealerNames.add(dealer.getName());
            }

            ChoiceDialog<String> dealerDialog = new ChoiceDialog<>(dealerNames.get(0), dealerNames);
            dealerDialog.setTitle("Select Target Dealer");
            dealerDialog.setHeaderText("Select the dealer to transfer the vehicle to:");
            dealerDialog.setContentText("Dealer:");

            Optional<String> result = dealerDialog.showAndWait();

            if (result.isPresent()) {
                String targetDealerName = result.get();

                // Find the target dealer by name
                Dealer targetDealer = null;
                for (Dealer dealer : dealers) {
                    if (dealer.getName().equals(targetDealerName)) {
                        targetDealer = dealer;
                        break;
                    }
                }

                if (targetDealer == null) {
                    System.out.println("Target dealer not found.");
                    return; // Exit if the dealer was not found
                }

                // Remove the vehicle from the current dealer and add it to the target dealer
                if (currentDealer != null) {
                    currentDealer.getVehicles().remove(selectedVehicle);
                    targetDealer.getVehicles().add(selectedVehicle);

                    // Update the observable list and refresh the table
                    dealersOL.remove(selectedVehicle);

                    // Update the table to show the target dealer's updated list
                    ObservableList<Vehicle> targetDealerOL = FXCollections.observableArrayList(targetDealer.getVehicles());
                    vehicleTable.setItems(targetDealerOL);

                    // Refresh the vehicle table to reflect changes
                    vehicleTable.refresh();

                    // Inform the user
                    System.out.println("Vehicle transferred to: " + targetDealer.getName());
                }
            }
        } else {
            System.out.println("No vehicle selected to transfer.");
        }
    }

    // Handle add new dealer
    @FXML
    public void handleAddDealer(ActionEvent event) {
        Dealer newDealer = new Dealer();

        try {
            // Load the fxml file and create a new popup dialog
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/hellofx/DealerDialog.fxml"));
            DialogPane vehicleDialogPane = fxmlLoader.load();

            // Get the vehicle controller
            DealerDialogController dealerDialogController = fxmlLoader.getController();

            dealerDialogController.setDealer(newDealer);

            // Create a dialog button type
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(vehicleDialogPane);
            dialog.setTitle("Add Dealer");

            // Option button to handle event
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            // When user click OK
            if (clickedButton.get() == ButtonType.OK) {
                System.out.println("User clicked OK");
                // If the user enter all information and there is no dealer yet
                if (newDealer != null && dealers != null) {
                    // Add the new dealer to the list of dealers

                    dealers.add(newDealer);
                    System.out.println("New dealer added: " + newDealer);

                    // Update the ChoiceBox
                    List<String> dealerNames = new ArrayList<>();
                    for (Dealer dealer : dealers) {
                        dealerNames.add(dealer.getName());
                    }
                    ObservableList<String> dealerNameOL = FXCollections.observableArrayList(dealerNames);
                    dealerChoiceBox.setItems(dealerNameOL);

                    // Select the newly added dealer in the ChoiceBox
                    dealerChoiceBox.setValue(newDealer.getName());
                }
                else {
                    System.out.println("Please import file first");
                    showAlertMessage(Alert.AlertType.ERROR, "Error adding new dealer", "Please import new file before adding new dealer");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
