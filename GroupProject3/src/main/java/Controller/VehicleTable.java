package Controller;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.hellofx.Vehicle;


public class VehicleTable {

    public static void buildTable(TableView<Vehicle> vehicleTable,
                                  TableColumn<Vehicle, String> idCol,
                                  TableColumn<Vehicle, String> typeCol,
                                  TableColumn<Vehicle, String> makeCol,
                                  TableColumn<Vehicle, String> modelCol,
                                  TableColumn<Vehicle, String> priceCol,
                                  TableColumn<Vehicle, String> statusCol) {
        // Set the perfect space for all the column with no extra space
        vehicleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Set placeholder message
        Label noDataLabel = new Label("No vehicles in inventory");
        noDataLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 16px;");
        vehicleTable.setPlaceholder(noDataLabel);

        // Set the column to an associate value
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    }
}
