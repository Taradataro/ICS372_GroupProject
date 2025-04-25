package org.example.hellofx

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.AnchorPane
import javafx.util.StringConverter
import Controller.MenuBarController
import Controller.VehicleTable
import Controller.VehicleDialogController
import Controller.DealerDialogController
import java.net.URL
import java.util.*

class MainController : Initializable {

    enum class DialogMode {
        ADD, UPDATE
    }

    // Pane
    @FXML lateinit var scenePane: AnchorPane
    @FXML lateinit var outputLabel: Label

    // Menu bar
    @FXML lateinit var myLabel: Label
    @FXML lateinit var dealerChoiceBox: ChoiceBox<String>
    @FXML lateinit var updateButton: Button
    @FXML lateinit var addButton: Button
    @FXML lateinit var deleteButton: Button
    @FXML lateinit var transferButton: Button
    @FXML lateinit var rentButton: Button
    @FXML lateinit var returnButton: Button

    // Table view
    @FXML lateinit var vehicleTable: TableView<Vehicle>
    @FXML lateinit var typeCol: TableColumn<Vehicle, String>
    @FXML lateinit var idCol: TableColumn<Vehicle, String>
    @FXML lateinit var makeCol: TableColumn<Vehicle, String>
    @FXML lateinit var modelCol: TableColumn<Vehicle, String>
    @FXML lateinit var priceCol: TableColumn<Vehicle, String>
    @FXML lateinit var statusCol: TableColumn<Vehicle, String>

    var dealers: MutableList<Dealer> = mutableListOf()
    private var currentDealer: Dealer? = null
    var dealersOL: ObservableList<Vehicle> = FXCollections.observableArrayList()

    // Auto called when the view is created
    override fun initialize(url: URL, resourceBundle: ResourceBundle?) {
        // Set default value for choice box
        dealerChoiceBox.converter = object : StringConverter<String>() {
            override fun toString(s: String?): String = s ?: "Select Dealer"
            override fun fromString(s: String): String? = null
        }

        // Handling when user selects a dealer -> load the table based on selection
        dealerChoiceBox.setOnAction { handleSelectDealer(it) }

        // Build the table
        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol)

        // If no vehicle is selected then disable update, delete, transfer, rent, return buttons
        val selectedItem = vehicleTable.selectionModel.selectedItemProperty()
        deleteButton.disableProperty().bind(Bindings.isNull(selectedItem))
        updateButton.disableProperty().bind(Bindings.isNull(selectedItem))
        transferButton.disableProperty().bind(Bindings.isNull(selectedItem))
        rentButton.disableProperty().bind(Bindings.isNull(selectedItem))
        returnButton.disableProperty().bind(Bindings.isNull(selectedItem))

        // Add tooltip to column menu button
        vehicleTable.skinProperty().addListener { _, _, newSkin ->
            if (newSkin != null) {
                val menuButton = vehicleTable.lookup(".show-hide-columns-button")
                if (menuButton is Button) {
                    Tooltip.install(menuButton, Tooltip("Filter Columns"))
                }
            }
        }
    }

    // Handle when user selects open file
    @FXML
    fun handleOpenFile(event: ActionEvent?) {
        println("Opening the file")
        val filePath = MenuBarController().openFile(outputLabel)
        dealers = DealerSingleton.getDealers(filePath).toMutableList()
        val namesList = dealers.map { it.name }
        dealerChoiceBox.items = FXCollections.observableArrayList(namesList)
    }

    // Handle when user clicks save option from menu
    @FXML
    fun handleSaveFile(event: ActionEvent?) {
        println("Saving the file")
        MenuBarController().saveFile(outputLabel, dealers)
    }

    // Handle when user selects close option from menu
    @FXML
    fun handleCloseApp(event: ActionEvent?) {
        println("Closing the file")
        MenuBarController().exit(scenePane)
    }
    //handle when user selects about option from menu
    @FXML
    fun handleAbout(event: ActionEvent?) {
        val alert = Alert(AlertType.INFORMATION)
        alert.title = "About"
        alert.headerText = "Dealership Management System"
        alert.contentText = "This application allows users to manage vehicle inventory and dealer operations. If you need help, we can't help you. YOYO "
        alert.showAndWait()
    }

    // Handle when user selects a dealer from the ChoiceBox
    @FXML
    fun handleSelectDealer(event: ActionEvent?) {
        val dealerName = dealerChoiceBox.value
        myLabel.text = dealerName
        for (dealer in dealers) {
            if (dealer.name == dealerName) {
                currentDealer = dealer
                dealersOL = FXCollections.observableArrayList(dealer.vehicles)
                vehicleTable.items = dealersOL
                return
            }
        }
    }

    @FXML
    fun handleAddVehicle(event: ActionEvent) {
        handleUpdateVehicle(event)
    }

    // Handle add/update vehicle
    @FXML
    fun handleUpdateVehicle(event: ActionEvent) {
        val mode: DialogMode
        val dialogTitle: String
        val vehicle: Vehicle

        if (event.source == updateButton) {
            mode = DialogMode.UPDATE
            dialogTitle = "Update Vehicle"
            vehicle = vehicleTable.selectionModel.selectedItem
        } else if (event.source == addButton) {
            mode = DialogMode.ADD
            dialogTitle = "Add Vehicle"
            vehicle = Vehicle()
        } else return

        try {
            val fxmlLoader = FXMLLoader(javaClass.getResource("/hellofx/VehicleDialog.fxml"))
            val dialogPane = fxmlLoader.load<DialogPane>()
            val controller = fxmlLoader.getController<VehicleDialogController>()
            controller.setVehicle(vehicle)

            val dialog = Dialog<ButtonType>()
            dialog.dialogPane = dialogPane
            dialog.title = dialogTitle

            val clickedButton = dialog.showAndWait()
            if (clickedButton.isPresent && clickedButton.get() == ButtonType.OK) {
                println("User clicked OK")
                if (mode == DialogMode.ADD && currentDealer != null) {
                    currentDealer!!.vehicles.add(vehicle)
                    dealersOL.add(vehicle)
                } else if (currentDealer == null) {
                    showAlertMessage(AlertType.WARNING, "No dealer selected", "Please select dealer before adding new vehicle")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Handle delete vehicle
    @FXML
    fun handleDeleteVehicle(event: ActionEvent?) {
        val selected = vehicleTable.selectionModel.selectedItem
        if (selected != null && currentDealer != null) {
            val confirmation = Alert(Alert.AlertType.CONFIRMATION)
            confirmation.title = "Delete Vehicle"
            confirmation.headerText = "Are you sure you want to delete selected vehicle?"
            confirmation.contentText = "This action cannot be undone."

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                currentDealer!!.vehicles.remove(selected)
                dealersOL.remove(selected)
                println("Vehicle deleted: \${selected.id}")
            }
        } else {
            showAlertMessage(AlertType.WARNING, "No selection", "Please select a vehicle to delete.")
        }
    }

    // Handle transfer vehicle
    @FXML
    fun handleTransferVehicle(event: ActionEvent?) {
        val selected = vehicleTable.selectionModel.selectedItem
        if (selected != null) {
            val dealerNames = dealers.map { it.name }
            val dialog = ChoiceDialog(dealerNames.first(), dealerNames)
            dialog.title = "Select Target Dealer"
            dialog.headerText = "Select the dealer to transfer the vehicle to:"
            dialog.contentText = "Dealer:"

            val result = dialog.showAndWait()
            if (result.isPresent) {
                val target = dealers.find { it.name == result.get() }
                if (target != null && currentDealer != null) {
                    currentDealer!!.vehicles.remove(selected)
                    target.vehicles.add(selected)
                    dealersOL.remove(selected)
                    vehicleTable.items = FXCollections.observableArrayList(target.vehicles)
                    vehicleTable.refresh()
                    println("Vehicle transferred to: \${target.name}")
                }
            }
        } else println("No vehicle selected to transfer.")
    }

    // Handle rent vehicle
    @FXML
    fun handleRentVehicle(event: ActionEvent?) {
        val selected = vehicleTable.selectionModel.selectedItem
        if (selected != null && !selected.type.equals("sports car", ignoreCase = true)) {
            if (selected.status.equals("Available", ignoreCase = true)) {
                selected.status = "Rented"
                vehicleTable.refresh()
                println("Vehicle rented: \$selected")
            } else println("This vehicle is not available for rent.")
        } else {
            println("Cannot rent sports car.")
            showAlertMessage(AlertType.ERROR, "Car type error", "A sport car cannot be rented")
        }
    }

    // Handle return vehicle
    @FXML
    fun handleReturnVehicle(event: ActionEvent?) {
        val selected = vehicleTable.selectionModel.selectedItem
        if (selected != null) {
            if (selected.status.equals("Rented", ignoreCase = true)) {
                selected.status = "Available"
                vehicleTable.refresh()
                println("Vehicle returned: \$selected")
            } else {
                println("This vehicle is not currently rented.")
                showAlertMessage(AlertType.ERROR, "Error", "This vehicle is not currently rented")
            }
        } else println("No vehicle selected to return.")
    }

    // Handle add new dealer
    @FXML
    fun handleAddDealer(event: ActionEvent?) {
        val newDealer = Dealer()
        try {
            val fxmlLoader = FXMLLoader(javaClass.getResource("/hellofx/DealerDialog.fxml"))
            val dialogPane = fxmlLoader.load<DialogPane>()
            val controller = fxmlLoader.getController<DealerDialogController>()
            controller.setDealer(newDealer)

            val dialog = Dialog<ButtonType>()
            dialog.dialogPane = dialogPane
            dialog.title = "Add Dealer"

            val clickedButton = dialog.showAndWait()
            if (clickedButton.get() == ButtonType.OK) {
                println("User clicked OK")
                if (dealers.isNotEmpty()) {
                    dealers.add(newDealer)
                    println("New dealer added: \$newDealer")
                    val names = dealers.map { it.name }
                    dealerChoiceBox.items = FXCollections.observableArrayList(names)
                    dealerChoiceBox.value = newDealer.name
                } else {
                    println("Please import file first")
                    showAlertMessage(AlertType.ERROR, "Error adding new dealer", "Please import new file before adding new dealer")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAlertMessage(alertType: AlertType, title: String, message: String) {
        val alert = Alert(alertType)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}