package org.example.hellofx

import Controller.DealerDialogController
import Controller.MenuBarController
import Controller.VehicleDialogController
import Controller.VehicleTable
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
import java.net.URL
import java.util.*
import kotlin.io.path.createTempFile

class MainController : Initializable {

    enum class DialogMode { ADD, UPDATE }

    // --- FXML fields ---
    @FXML private lateinit var imageView: ImageView
    @FXML private lateinit var scenePane: AnchorPane
    @FXML private lateinit var outputLabel: Label

    @FXML private lateinit var myLabel: Label
    @FXML private lateinit var dealerChoiceBox: ChoiceBox<String>
    @FXML private lateinit var updateButton: Button
    @FXML private lateinit var addButton: Button
    @FXML private lateinit var deleteButton: Button
    @FXML private lateinit var transferButton: Button
    @FXML private lateinit var rentButton: Button
    @FXML private lateinit var returnButton: Button
    @FXML private lateinit var statusFilterBox: ChoiceBox<String>
    @FXML private lateinit var searchField: TextField

    @FXML private lateinit var vehicleTable: TableView<Vehicle>
    @FXML private lateinit var typeCol: TableColumn<Vehicle, String>
    @FXML private lateinit var idCol: TableColumn<Vehicle, String>
    @FXML private lateinit var makeCol: TableColumn<Vehicle, String>
    @FXML private lateinit var modelCol: TableColumn<Vehicle, String>
    @FXML private lateinit var priceCol: TableColumn<Vehicle, String>
    @FXML private lateinit var statusCol: TableColumn<Vehicle, String>

    // --- internal state ---
    private var dealers: MutableList<Dealer> = mutableListOf()
    private var currentDealer: Dealer? = null
    private var dealersOL: ObservableList<Vehicle> = FXCollections.observableArrayList()

    override fun initialize(url: URL?, rb: ResourceBundle?) {
        // 1) Configure the dealer dropdown
        dealerChoiceBox.converter = object : StringConverter<String>() {
            override fun toString(s: String?) = s ?: "Select Dealer"
            override fun fromString(s: String) = null
        }
        dealerChoiceBox.setOnAction { handleSelectDealer(null) }

        // 2) Build the vehicle table columns & placeholder
        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol)

        // 3) Disable action buttons until a row is selected
        val sel = vehicleTable.selectionModel.selectedItemProperty()
        updateButton.disableProperty().bind(Bindings.isNull(sel))
        deleteButton.disableProperty().bind(Bindings.isNull(sel))
        transferButton.disableProperty().bind(Bindings.isNull(sel))
        rentButton.disableProperty().bind(Bindings.isNull(sel))
        returnButton.disableProperty().bind(Bindings.isNull(sel))

        // 4) Tooltip on the “hide columns” menu button
        vehicleTable.skinProperty().addListener { _, _, newSkin ->
            if (newSkin != null) {
                val btn = vehicleTable.lookup(".show-hide-columns-button")
                if (btn is Button) Tooltip.install(btn, Tooltip("Filter Columns"))
            }
        }

        // 5) Status‐filter dropdown
        statusFilterBox.items = FXCollections.observableArrayList(
            "All", "Available", "Rented", "Sports Car", "SUV", "Sedan", "Truck"
        )
        statusFilterBox.value = "All"
        statusFilterBox.setOnAction {
            val filtered = when (statusFilterBox.value) {
                "All"       -> dealersOL
                "Available" -> dealersOL.filter { it.status.equals("Available", true) }
                "Rented"    -> dealersOL.filter { it.status.equals("Rented", true) }
                else        -> dealersOL.filter { it.type.equals(statusFilterBox.value, true) }
            }
            vehicleTable.items = FXCollections.observableArrayList(filtered)
        }

        // 6) Live search box
        searchField.textProperty().addListener { _, _, query ->
            filterVehicles(query)
        }

        // 7) Image update on selection
        vehicleTable.selectionModel.selectedItemProperty().addListener { _, _, v ->
            if (v != null) showVehicleImage(v.type)
        }

        // --- Now that UI is ready, auto-load Dealers.xml from classpath ---
        javaClass.getResourceAsStream("/Dealer.xml")?.use { stream ->
            // Copy to temp so our existing loader can read it
            val tempFile = createTempFile(suffix = ".xml").toFile().apply {
                deleteOnExit()
            }
            stream.copyTo(tempFile.outputStream())

            // Load into our singleton & populate dropdown
            dealers = DealerSingleton.getDealers(tempFile.absolutePath).toMutableList()
            val names = dealers.map { it.name }
            dealerChoiceBox.items = FXCollections.observableArrayList(names)

            // Auto-select first dealer and show its vehicles
            if (dealers.isNotEmpty()) {
                dealerChoiceBox.value = names.first()
                handleSelectDealer(null)
            }
        }
    }

    private fun showVehicleImage(vehicleType: String) {
        val imageName = when (vehicleType.lowercase()) {
            "suv"        -> "images/suv.png"
            "sedan"      -> "images/sedan.png"
            "sports car" -> "images/sports_car.png"
            "truck","pickup" -> "images/truck.png"
            else         -> "default.png"
        }
        try {
            val img = Image(javaClass.getResourceAsStream("/$imageName"))
            imageView.image = if (img.isError) null else img
        } catch (e: Exception) {
            println("Failed to load $imageName: ${e.message}")
            imageView.image = null
        }
    }

    @FXML fun handleOpenFile(event: ActionEvent?) {
        val path = MenuBarController().openFile(outputLabel)
        dealers = DealerSingleton.getDealers(path).toMutableList()
        dealerChoiceBox.items = FXCollections.observableArrayList(dealers.map { it.name })
    }

    @FXML fun handleSaveFile(event: ActionEvent?) {
        MenuBarController().saveFile(outputLabel, dealers)
    }

    @FXML fun handleCloseApp(event: ActionEvent?) {
        MenuBarController().exit(scenePane)
    }

    @FXML fun handleAbout(event: ActionEvent?) {
        Alert(Alert.AlertType.INFORMATION).apply {
            title = "About"
            headerText = "Dealership Management System"
            contentText = "This application allows users to manage vehicle inventory and dealer operations."
        }.showAndWait()
    }

    @FXML fun handleSelectDealer(event: ActionEvent?) {
        dealerChoiceBox.value?.let { name ->
            myLabel.text = name
            dealers.find { it.name == name }?.let { dealer ->
                currentDealer = dealer
                dealersOL = FXCollections.observableArrayList(dealer.vehicles)
                vehicleTable.items = dealersOL
            }
        }
    }

    @FXML fun handleAddVehicle(event: ActionEvent)    = handleVehicleDialog(event, DialogMode.ADD)
    @FXML fun handleUpdateVehicle(event: ActionEvent) = handleVehicleDialog(event, DialogMode.UPDATE)

    private fun handleVehicleDialog(event: ActionEvent, mode: DialogMode) {
        val (title, vehicle) = when (mode) {
            DialogMode.ADD    -> "Add Vehicle" to Vehicle()
            DialogMode.UPDATE -> vehicleTable.selectionModel.selectedItem?.let { "Update Vehicle" to it }
                ?: return
        }
        try {
            val loader = FXMLLoader(javaClass.getResource("/hellofx/VehicleDialog.fxml"))
            val pane   = loader.load<DialogPane>()
            val ctrl   = loader.getController<VehicleDialogController>()
            ctrl.setVehicle(vehicle)

            Dialog<ButtonType>().apply {
                dialogPane = pane
                this.title = title
            }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                if (mode == DialogMode.ADD && currentDealer != null) {
                    currentDealer!!.vehicles.add(vehicle)
                    dealersOL.add(vehicle)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @FXML fun handleDeleteVehicle(event: ActionEvent?) {
        val sel = vehicleTable.selectionModel.selectedItem ?: return
        if (currentDealer != null) {
            Alert(Alert.AlertType.CONFIRMATION).apply {
                title = "Delete Vehicle"
                headerText = "Are you sure you want to delete this vehicle?"
                contentText = "This action cannot be undone."
            }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                currentDealer!!.vehicles.remove(sel)
                dealersOL.remove(sel)
            }
        }
    }

    @FXML fun handleTransferVehicle(event: ActionEvent?) {
        val sel = vehicleTable.selectionModel.selectedItem ?: return
        val names = dealers.map { it.name }
        ChoiceDialog(names.first(), names).apply {
            title = "Select Target Dealer"
            headerText = "Choose a dealer to transfer the vehicle to:"
        }.showAndWait().ifPresent { target ->
            dealers.find { it.name == target }?.let { dest ->
                currentDealer?.vehicles?.remove(sel)
                dest.vehicles.add(sel)
                dealersOL.remove(sel)
                vehicleTable.items = FXCollections.observableArrayList(dest.vehicles)
                vehicleTable.refresh()
            }
        }
    }

    @FXML fun handleRentVehicle(event: ActionEvent?) {
        vehicleTable.selectionModel.selectedItem?.takeIf { !it.type.equals("sports car", true) }?.also { sel ->
            if (sel.status.equals("Available", true)) {
                sel.status = "Rented"
                vehicleTable.refresh()
            }
        }
    }

    @FXML fun handleReturnVehicle(event: ActionEvent?) {
        vehicleTable.selectionModel.selectedItem?.also { sel ->
            if (sel.status.equals("Rented", true)) {
                sel.status = "Available"
                vehicleTable.refresh()
            }
        }
    }

    @FXML fun handleAddDealer(event: ActionEvent?) {
        val newDealer = Dealer()
        try {
            val loader = FXMLLoader(javaClass.getResource("/hellofx/DealerDialog.fxml"))
            val pane   = loader.load<DialogPane>()
            val ctrl   = loader.getController<DealerDialogController>()
            ctrl.setDealer(newDealer)

            Dialog<ButtonType>().apply {
                dialogPane = pane
                title = "Add Dealer"
            }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                dealers.add(newDealer)
                dealerChoiceBox.items = FXCollections.observableArrayList(dealers.map { it.name })
                dealerChoiceBox.value = newDealer.name
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun filterVehicles(query: String) {
        val list = if (query.isBlank()) {
            dealersOL
        } else {
            dealersOL.filter { v ->
                v.id.contains(query, true) ||
                        v.make.contains(query, true) ||
                        v.model.contains(query, true) ||
                        v.type.contains(query, true)
            }
        }
        vehicleTable.items = FXCollections.observableArrayList(list)
    }
}
