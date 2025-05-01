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
import org.example.hellofx.Dealer
import org.example.hellofx.DealerSingleton
import org.example.hellofx.Vehicle
import java.net.URL
import java.util.*

class MainController : Initializable {

    enum class DialogMode { ADD, UPDATE }

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

    private var dealers: MutableList<Dealer> = mutableListOf()
    private var currentDealer: Dealer? = null
    private var dealersOL: ObservableList<Vehicle> = FXCollections.observableArrayList()

    override fun initialize(url: URL, resourceBundle: ResourceBundle?) {
        dealerChoiceBox.converter = object : StringConverter<String>() {
            override fun toString(s: String?) = s ?: "Select Dealer"
            override fun fromString(s: String) = null
        }
        dealerChoiceBox.setOnAction { handleSelectDealer(null) }

        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol)

        deleteButton.disableProperty().bind(Bindings.isNull(vehicleTable.selectionModel.selectedItemProperty()))
        updateButton.disableProperty().bind(Bindings.isNull(vehicleTable.selectionModel.selectedItemProperty()))
        transferButton.disableProperty().bind(Bindings.isNull(vehicleTable.selectionModel.selectedItemProperty()))
        rentButton.disableProperty().bind(Bindings.isNull(vehicleTable.selectionModel.selectedItemProperty()))
        returnButton.disableProperty().bind(Bindings.isNull(vehicleTable.selectionModel.selectedItemProperty()))

        vehicleTable.skinProperty().addListener { _, _, newSkin ->
            if (newSkin != null) {
                val mb = vehicleTable.lookup(".show-hide-columns-button")
                if (mb is Button) Tooltip.install(mb, Tooltip("Filter Columns"))
            }
        }

        statusFilterBox.items = FXCollections.observableArrayList(
            "All", "Available", "Rented", "Sports Car", "SUV", "Sedan", "Truck"
        )
        statusFilterBox.value = "All"
        statusFilterBox.setOnAction {
            val selStatus = statusFilterBox.value
            vehicleTable.items = when (selStatus) {
                "All" -> dealersOL
                "Available", "Rented" -> FXCollections.observableArrayList(
                    dealersOL.filter { it.status.equals(selStatus, true) }
                )
                else -> FXCollections.observableArrayList(
                    dealersOL.filter { it.type.equals(selStatus, true) }
                )
            }
        }

        searchField.textProperty().addListener { _, _, query ->
            filterVehicles(query)
        }

        vehicleTable.selectionModel.selectedItemProperty().addListener { _, _, v ->
            if (v != null) showVehicleImage(v.type)
        }
    }

    private fun showVehicleImage(vehicleType: String) {
        val imageName = when (vehicleType.lowercase()) {
            "suv" -> "images/suv.png"
            "sedan" -> "images/sedan.png"
            "sports car" -> "images/sports_car.png"
            "truck", "pickup" -> "images/truck.png"
            else -> "default.png"
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
        val name = dealerChoiceBox.value
        myLabel.text = name
        dealers.find { it.name == name }?.let {
            currentDealer = it
            dealersOL = FXCollections.observableArrayList(it.vehicles)
            vehicleTable.items = dealersOL
        }
    }

    @FXML fun handleAddVehicle(event: ActionEvent) = handleVehicleDialog(event, DialogMode.ADD)
    @FXML fun handleUpdateVehicle(event: ActionEvent) = handleVehicleDialog(event, DialogMode.UPDATE)

    private fun handleVehicleDialog(event: ActionEvent, mode: DialogMode) {
        val (title, vehicle) = when (mode) {
            DialogMode.ADD -> "Add Vehicle" to Vehicle()
            DialogMode.UPDATE -> {
                val sel = vehicleTable.selectionModel.selectedItem ?: return
                "Update Vehicle" to sel
            }
        }
        try {
            val loader = FXMLLoader(javaClass.getResource("/hellofx/VehicleDialog.fxml"))
            val pane = loader.load<DialogPane>()
            val ctrl = loader.getController<VehicleDialogController>()
            ctrl.setVehicle(vehicle)

            Dialog<ButtonType>().apply {
                dialogPane = pane
                this.title = title
            }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                if (mode == DialogMode.ADD && currentDealer != null) {
                    currentDealer!!.vehicles.add(vehicle)
                    dealersOL.add(vehicle)
                } else if (mode == DialogMode.ADD) {
                    showAlert(Alert.AlertType.WARNING, "No dealer selected", "Please select a dealer first")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @FXML fun handleDeleteVehicle(event: ActionEvent?) {
        val sel = vehicleTable.selectionModel.selectedItem
        if (sel != null && currentDealer != null) {
            Alert(Alert.AlertType.CONFIRMATION).apply {
                title = "Delete Vehicle"
                headerText = "Are you sure you want to delete?"
                contentText = "This cannot be undone."
            }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                currentDealer!!.vehicles.remove(sel)
                dealersOL.remove(sel)
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No selection", "Select a vehicle first")
        }
    }

    @FXML fun handleTransferVehicle(event: ActionEvent?) {
        val sel = vehicleTable.selectionModel.selectedItem ?: return
        val names = dealers.map { it.name }
        ChoiceDialog(names.first(), names).apply {
            title = "Select Target Dealer"
            headerText = "Choose dealer:"
            contentText = "Dealer:"
        }.showAndWait().filter { true }.ifPresent { targetName ->
            val target = dealers.find { it.name == targetName } ?: return@ifPresent
            currentDealer?.vehicles?.remove(sel)
            target.vehicles.add(sel)
            dealersOL.remove(sel)
            vehicleTable.items = FXCollections.observableArrayList(target.vehicles)
            vehicleTable.refresh()
        }
    }

    @FXML fun handleRentVehicle(event: ActionEvent?) {
        val sel = vehicleTable.selectionModel.selectedItem ?: return
        if (sel.type.equals("sports car", true)) {
            showAlert(Alert.AlertType.ERROR, "Car type error", "A sports car cannot be rented")
            return
        }
        if (sel.status.equals("Available", true)) {
            sel.status = "Rented"
            vehicleTable.refresh()
        }
    }

    @FXML fun handleReturnVehicle(event: ActionEvent?) {
        val sel = vehicleTable.selectionModel.selectedItem ?: return
        if (sel.status.equals("Rented", true)) {
            sel.status = "Available"
            vehicleTable.refresh()
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "This vehicle is not rented")
        }
    }

    @FXML fun handleAddDealer(event: ActionEvent?) {
        val newDealer = Dealer()
        try {
            val loader = FXMLLoader(javaClass.getResource("/hellofx/DealerDialog.fxml"))
            val pane = loader.load<DialogPane>()
            val ctrl = loader.getController<DealerDialogController>()
            ctrl.setDealer(newDealer)

            Dialog<ButtonType>().apply {
                dialogPane = pane
                title = "Add Dealer"
            }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                dealers.add(newDealer)
                val names = dealers.map { it.name }
                dealerChoiceBox.items = FXCollections.observableArrayList(names)
                dealerChoiceBox.value = newDealer.name
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAlert(type: Alert.AlertType, header: String, content: String) {
        Alert(type).apply {
            title = "Information"
            headerText = header
            contentText = content
        }.showAndWait()
    }

    private fun filterVehicles(query: String) {
        val list = if (query.isBlank()) dealersOL
        else dealersOL.filter { v ->
            v.id.contains(query, true) ||
                    v.make.contains(query, true) ||
                    v.model.contains(query, true) ||
                    v.type.contains(query, true)
        }
        vehicleTable.items = FXCollections.observableArrayList(list)
    }
}