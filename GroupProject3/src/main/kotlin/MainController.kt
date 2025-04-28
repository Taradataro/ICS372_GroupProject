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

    enum class DialogMode { ADD, UPDATE }

    // Image view for vehicles
    @FXML lateinit var imageView: ImageView

    // Main panes and labels
    @FXML lateinit var scenePane: AnchorPane
    @FXML lateinit var outputLabel: Label

    // Dealer controls
    @FXML lateinit var myLabel: Label
    @FXML lateinit var dealerChoiceBox: ChoiceBox<String>
    @FXML lateinit var updateButton: Button
    @FXML lateinit var addButton: Button
    @FXML lateinit var deleteButton: Button
    @FXML lateinit var transferButton: Button
    @FXML lateinit var rentButton: Button
    @FXML lateinit var returnButton: Button
    @FXML lateinit var statusFilterBox: ChoiceBox<String>

    // Search controls
    @FXML lateinit var searchTypeBox: ChoiceBox<String>
    @FXML lateinit var searchField: TextField
    @FXML lateinit var searchButton: Button
    @FXML lateinit var clearSearchButton: Button

    // Vehicle table and columns
    @FXML lateinit var vehicleTable: TableView<Vehicle>
    @FXML lateinit var idCol: TableColumn<Vehicle, String>
    @FXML lateinit var typeCol: TableColumn<Vehicle, String>
    @FXML lateinit var makeCol: TableColumn<Vehicle, String>
    @FXML lateinit var modelCol: TableColumn<Vehicle, String>
    @FXML lateinit var priceCol: TableColumn<Vehicle, String>
    @FXML lateinit var statusCol: TableColumn<Vehicle, String>

    private var dealers: MutableList<Dealer> = mutableListOf()
    private var currentDealer: Dealer? = null
    private var dealersOL: ObservableList<Vehicle> = FXCollections.observableArrayList()

    override fun initialize(url: URL, resourceBundle: ResourceBundle?) {
        // Dealer choice setup
        dealerChoiceBox.converter = object : StringConverter<String>() {
            override fun toString(s: String?) = s ?: "Select Dealer"
            override fun fromString(s: String) = null
        }
        dealerChoiceBox.setOnAction { handleSelectDealer(null) }

        // Vehicle table setup
        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol)
        val selProp = vehicleTable.selectionModel.selectedItemProperty()
        listOf(deleteButton, updateButton, transferButton, rentButton, returnButton)
            .forEach { it.disableProperty().bind(Bindings.isNull(selProp)) }

        vehicleTable.skinProperty().addListener { _, _, newSkin ->
            if (newSkin != null) {
                (vehicleTable.lookup(".show-hide-columns-button") as? Button)?.let {
                    Tooltip.install(it, Tooltip("Filter Columns"))
                }
            }
        }

        // Status filter
        statusFilterBox.items = FXCollections.observableArrayList(
            "All", "Available", "Rented", "Sports Car", "SUV", "Sedan", "Truck"
        )
        statusFilterBox.value = "All"
        statusFilterBox.setOnAction { filterByStatus() }

        // Update image on selection
        vehicleTable.selectionModel.selectedItemProperty().addListener { _, _, v ->
            v?.let { showVehicleImage(it.type) }
        }

        // Search setup
        searchTypeBox.items = FXCollections.observableArrayList("Dealer", "Vehicle")
        searchTypeBox.value = "Dealer"
        // Remove placeholder text behind search field
        searchField.promptText = ""
        searchField.setOnAction { handleSearch(null) }
        clearSearchButton.setOnAction { handleClearSearch(null) }
    }

    private fun filterByStatus() {
        val sel = statusFilterBox.value
        vehicleTable.items = when (sel) {
            "All" -> dealersOL
            "Available", "Rented" -> FXCollections.observableArrayList(
                dealersOL.filter { it.getStatus().equals(sel, ignoreCase = true) }
            )
            else -> FXCollections.observableArrayList(
                dealersOL.filter { it.type.equals(sel, ignoreCase = true) }
            )
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
            val stream = javaClass.getResourceAsStream("/$imageName")
            val image = stream?.let { Image(it) }
            if (image == null || image.isError) {
                imageView.image = null
            } else {
                imageView.image = image
            }
        } catch (e: Exception) {
            imageView.image = null
        }
    }

    @FXML
    fun handleOpenFile(event: ActionEvent?) {
        val path = MenuBarController().openFile(outputLabel)
        dealers = DealerSingleton.getDealers(path).toMutableList()
        dealerChoiceBox.items = FXCollections.observableArrayList(dealers.map { it.name })
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
        Alert(Alert.AlertType.INFORMATION).apply {
            title = "About"
            headerText = "Dealership Management System"
            contentText = "This application allows users to manage vehicle inventory and dealer operations."
        }.showAndWait()
    }

    @FXML
    fun handleSelectDealer(event: ActionEvent?) {
        dealerChoiceBox.value?.let { name ->
            myLabel.text = name
            dealers.find { it.name == name }?.let {
                currentDealer = it
                dealersOL = FXCollections.observableArrayList(it.getVehicles())
                vehicleTable.items = dealersOL
            }
        }
    }

    @FXML
    fun handleAddVehicle(event: ActionEvent) = handleUpdateVehicle(event)

    @FXML
    fun handleUpdateVehicle(event: ActionEvent) {
        val mode = if (event.source == updateButton) DialogMode.UPDATE else DialogMode.ADD
        val title = if (mode == DialogMode.UPDATE) "Update Vehicle" else "Add Vehicle"
        val vehicle = if (mode == DialogMode.UPDATE) vehicleTable.selectionModel.selectedItem else Vehicle()
        try {
            FXMLLoader(javaClass.getResource("/hellofx/VehicleDialog.fxml")).apply {
                val pane = load<DialogPane>()
                getController<VehicleDialogController>().setVehicle(vehicle)
                Dialog<ButtonType>().apply {
                    dialogPane = pane
                    this.title = title
                }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                    if (mode == DialogMode.ADD && currentDealer != null) {
                        currentDealer!!.addVehicle(vehicle)
                        dealersOL.add(vehicle)
                    } else if (currentDealer == null) showAlert("Warning", "Please select a dealer first.")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @FXML
    fun handleDeleteVehicle(event: ActionEvent?) {
        vehicleTable.selectionModel.selectedItem?.let { sel ->
            if (currentDealer != null) {
                Alert(Alert.AlertType.CONFIRMATION).apply {
                    title = "Delete Vehicle"
                    headerText = "Confirm deletion"
                    contentText = "This cannot be undone."
                }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                    currentDealer!!.removeVehicle(sel)
                    dealersOL.remove(sel)
                }
            }
        } ?: showAlert("Warning", "Please select a vehicle to delete.")
    }

    @FXML
    fun handleTransferVehicle(event: ActionEvent?) {
        vehicleTable.selectionModel.selectedItem?.let { sel ->
            ChoiceDialog(dealers.map { it.name }.first(), dealers.map { it.name }).apply {
                title = "Transfer Vehicle"
                headerText = "Choose target dealer"
                contentText = "Dealer:"
            }.showAndWait().ifPresent { name ->
                dealers.find { it.name == name }?.let { target ->
                    currentDealer?.removeVehicle(sel)
                    target.addVehicle(sel)
                    dealersOL.remove(sel)
                    vehicleTable.items = FXCollections.observableArrayList(target.getVehicles())
                }
            }
        }
    }

    @FXML
    fun handleRentVehicle(event: ActionEvent?) {
        vehicleTable.selectionModel.selectedItem?.let { sel ->
            if (sel.type.equals("sports car", true)) showAlert("Error", "Sports cars cannot be rented.")
            else if (sel.getStatus().equals("Available", true)) {
                sel.setStatus("Rented")
                vehicleTable.refresh()
            }
        }
    }

    @FXML
    fun handleReturnVehicle(event: ActionEvent?) {
        vehicleTable.selectionModel.selectedItem?.let { sel ->
            if (sel.getStatus().equals("Rented", true)) {
                sel.setStatus("Available")
                vehicleTable.refresh()
            } else showAlert("Error", "This vehicle is not currently rented.")
        }
    }

    @FXML
    fun handleAddDealer(event: ActionEvent?) {
        val newDealer = Dealer()
        try {
            FXMLLoader(javaClass.getResource("/hellofx/DealerDialog.fxml")).apply {
                val pane = load<DialogPane>()
                getController<DealerDialogController>().setDealer(newDealer)
                Dialog<ButtonType>().apply {
                    dialogPane = pane
                    title = "Add Dealer"
                }.showAndWait().filter { it == ButtonType.OK }.ifPresent {
                    dealers.add(newDealer)
                    dealerChoiceBox.items = FXCollections.observableArrayList(dealers.map { it.name })
                    dealerChoiceBox.value = newDealer.name
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @FXML
    fun handleSearch(event: ActionEvent?) {
        val query = searchField.text.trim().lowercase()
        if (query.isEmpty()) {
            showAlert("Warning", "Please type something to search.")
            return
        }
        when (searchTypeBox.value) {
            "Dealer" -> {
                val matches = dealers.map { it.name }
                    .filter { it.lowercase().contains(query) }
                dealerChoiceBox.items = FXCollections.observableArrayList(matches)
                if (matches.isNotEmpty()) {
                    dealerChoiceBox.value = matches.first()
                    handleSelectDealer(null)
                }
            }
            "Vehicle" -> {
                if (currentDealer == null) {
                    showAlert("Warning", "Please select a dealer before searching vehicles.")
                    return
                }
                val filtered = dealersOL.filter { v ->
                    listOf(v.id, v.make, v.model, v.type, v.getStatus())
                        .any { it.lowercase().contains(query) }
                }
                vehicleTable.items = FXCollections.observableArrayList(filtered)
            }
        }
    }

    @FXML
    fun handleClearSearch(event: ActionEvent?) {
        searchField.clear()
        dealerChoiceBox.items = FXCollections.observableArrayList(dealers.map { it.name })
        currentDealer?.let {
            dealersOL = FXCollections.observableArrayList(it.getVehicles())
            vehicleTable.items = dealersOL
        }
    }

    private fun showAlert(type: String, msg: String) {
        Alert(Alert.AlertType.INFORMATION).apply {
            headerText = type
            contentText = msg
        }.showAndWait()
    }
}
