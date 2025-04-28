package org.example.hellofx

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
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

    // ImageView to show vehicle images
    @FXML lateinit var imageView: ImageView

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
    @FXML lateinit var statusFilterBox: ChoiceBox<String>

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

    override fun initialize(url: URL, resourceBundle: ResourceBundle?) {
        dealerChoiceBox.converter = object : StringConverter<String>() {
            override fun toString(s: String?): String = s ?: "Select Dealer"
            override fun fromString(s: String): String? = null
        }

        dealerChoiceBox.setOnAction { handleSelectDealer(it) }

        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol)

        val selectedItem = vehicleTable.selectionModel.selectedItemProperty()
        deleteButton.disableProperty().bind(Bindings.isNull(selectedItem))
        updateButton.disableProperty().bind(Bindings.isNull(selectedItem))
        transferButton.disableProperty().bind(Bindings.isNull(selectedItem))
        rentButton.disableProperty().bind(Bindings.isNull(selectedItem))
        returnButton.disableProperty().bind(Bindings.isNull(selectedItem))

        vehicleTable.skinProperty().addListener { _, _, newSkin ->
            if (newSkin != null) {
                val menuButton = vehicleTable.lookup(".show-hide-columns-button")
                if (menuButton is Button) {
                    Tooltip.install(menuButton, Tooltip("Filter Columns"))
                }
            }
        }

        statusFilterBox.items = FXCollections.observableArrayList(
            "All", "Available", "Rented", "Sports Car", "SUV", "Sedan", "Truck"
        )
        statusFilterBox.value = "All"

        statusFilterBox.setOnAction {
            val selected = statusFilterBox.value
            vehicleTable.items = when (selected) {
                "All" -> dealersOL
                "Available", "Rented" ->
                    FXCollections.observableArrayList(dealersOL.filter {
                        it.status.equals(selected, ignoreCase = true)
                    })
                else ->
                    FXCollections.observableArrayList(dealersOL.filter {
                        it.type.equals(selected, ignoreCase = true)
                    })
            }
        }

        // Image update on vehicle selection
        vehicleTable.selectionModel.selectedItemProperty().addListener { _, _, newVehicle ->
            if (newVehicle != null) {
                showVehicleImage(newVehicle.type)
            }
        }
    }

    private fun showVehicleImage(vehicleType: String) {
        // Convert vehicle type to match your image file names
        val imageName = when (vehicleType.lowercase()) {
            "suv" -> "images/suv.png"
            "sedan" -> "images/sedan.png"
            "sports car" -> "images/sports_car.png"
            "truck", "pickup" -> "images/truck.png"  // Added "pickup" as an alias
            else -> "default.png"
        }

        try {
            // Load from resources folder (src/main/resources)
            val image = Image(javaClass.getResourceAsStream("/$imageName"))

            if (image.isError) {
                println("Error loading image: ${image.exception.message}")
                imageView.image = null  // Clear image view if error occurs
            } else {
                imageView.image = image
            }
        } catch (e: Exception) {
            println("Failed to load image for $vehicleType ($imageName). Error: ${e.message}")
            imageView.image = null
        }
    }

    @FXML
    fun handleOpenFile(event: ActionEvent?) {
        val filePath = MenuBarController().openFile(outputLabel)
        dealers = DealerSingleton.getDealers(filePath).toMutableList()
        val namesList = dealers.map { it.name }
        dealerChoiceBox.items = FXCollections.observableArrayList(namesList)
    }

    @FXML
    fun handleSaveFile(event: ActionEvent?) {
        MenuBarController().saveFile(outputLabel, dealers)
    }

    @FXML
    fun handleCloseApp(event: ActionEvent?) {
        MenuBarController().exit(scenePane)
    }

    @FXML
    fun handleAbout(event: ActionEvent?) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "About"
        alert.headerText = "Dealership Management System"
        alert.contentText = "This application allows users to manage vehicle inventory and dealer operations. If you need help, we can't help you. YOYO."
        alert.showAndWait()
    }

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
                if (mode == DialogMode.ADD && currentDealer != null) {
                    currentDealer!!.vehicles.add(vehicle)
                    dealersOL.add(vehicle)
                } else if (currentDealer == null) {
                    showAlertMessage(Alert.AlertType.WARNING, "No dealer selected", "Please select dealer before adding new vehicle")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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
            }
        } else {
            showAlertMessage(Alert.AlertType.WARNING, "No selection", "Please select a vehicle to delete.")
        }
    }

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
                }
            }
        }
    }

    @FXML
    fun handleRentVehicle(event: ActionEvent?) {
        val selected = vehicleTable.selectionModel.selectedItem
        if (selected != null && !selected.type.equals("sports car", ignoreCase = true)) {
            if (selected.status.equals("Available", ignoreCase = true)) {
                selected.status = "Rented"
                vehicleTable.refresh()
            }
        } else {
            showAlertMessage(Alert.AlertType.ERROR, "Car type error", "A sport car cannot be rented")
        }
    }

    @FXML
    fun handleReturnVehicle(event: ActionEvent?) {
        val selected = vehicleTable.selectionModel.selectedItem
        if (selected != null) {
            if (selected.status.equals("Rented", ignoreCase = true)) {
                selected.status = "Available"
                vehicleTable.refresh()
            } else {
                showAlertMessage(Alert.AlertType.ERROR, "Error", "This vehicle is not currently rented")
            }
        }
    }

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
                if (dealers.isNotEmpty()) {
                    dealers.add(newDealer)
                    val names = dealers.map { it.name }
                    dealerChoiceBox.items = FXCollections.observableArrayList(names)
                    dealerChoiceBox.value = newDealer.name
                } else {
                    showAlertMessage(Alert.AlertType.ERROR, "Error adding new dealer", "Please import new file before adding new dealer")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAlertMessage(alertType: Alert.AlertType, headerText: String, contentText: String) {
        val alert = Alert(alertType)
        alert.title = "Information"
        alert.headerText = headerText
        alert.contentText = contentText
        alert.showAndWait()
    }
}
