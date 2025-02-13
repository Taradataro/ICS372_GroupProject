package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class jsonExport {

    // Method to export dealership data to a JSON file
    public static void exportDealershipToJson(Dealership dealership, String filePath) {
        JSONObject dealershipJson = new JSONObject();
        JSONArray vehiclesJsonArray = new JSONArray();

        for (Vehicle vehicle : dealership.getVehicles()) {
            JSONObject vehicleJson = new JSONObject();
            vehicleJson.put("dealership_id", dealership.getDealershipId());
            //vehicleJson.put("vehicle_type", vehicle.getVehicleType());
            vehicleJson.put("vehicle_manufacturer", vehicle.getManufacture());
            vehicleJson.put("vehicle_model", vehicle.getModel());
            vehicleJson.put("vehicle_id", vehicle.getVehicleId());
            vehicleJson.put("price", vehicle.getPrice());
            vehicleJson.put("acquisition_date", vehicle.getAcquisitionDate());

            // 4: metadata
            if (vehicle.getMetadata() != null && !vehicle.getMetadata().isEmpty()) {
                vehicleJson.put("metadata", vehicle.getMetadata());
            }

            vehiclesJsonArray.add(vehicleJson);
        }

        dealershipJson.put("car_inventory", vehiclesJsonArray);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(dealershipJson.toJSONString());
            System.out.println("Dealership data exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting dealership to JSON: " + e.getMessage());
        }
    }
}
