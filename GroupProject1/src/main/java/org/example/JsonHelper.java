package org.example;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class JsonHelper {
    public static void readAndAddVehicleJson(String filepath, Dealership dealership) {
        // Create a jsonParser object to read json file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(filepath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            // Read json file from car_inventory and store in Json array
            JSONArray carInventory = (JSONArray) jsonObject.get("car_inventory");

            for (Object obj : carInventory) {
                JSONObject vehicleJson = (JSONObject) obj;
                Vehicle vehicle = readData(vehicleJson);
                dealership.addVehicle(vehicle);
            }

        } catch (IOException | ParseException e) {
            System.err.println("Error reading Json file: " + e.getMessage());
        }
    }

    // This method will read data vehicle from Json file
    private static Vehicle readData(JSONObject vehicleJson) {
        String vehicleId = (String) vehicleJson.get("vehicle_id");
        String manufacturer = (String) vehicleJson.get("vehicle_manufacturer");
        String model = (String) vehicleJson.get("vehicle_model");
        double price = ((Number) vehicleJson.get("price")).doubleValue();
        long acquisitionDateLong = (long) vehicleJson.get("acquisition_date");
        String vehicleType = (String) vehicleJson.get("vehicle_type");

        // Convert acquisition date from timestamp to LocalDate
        LocalDate acquisitionDate = Instant.ofEpochMilli(acquisitionDateLong)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Vehicle vehicle = new Vehicle(vehicleId, manufacturer, model, acquisitionDate, price, vehicleType);

        // 4: Extract and store additional metadata
        JSONObject metadata = new JSONObject();
        // Iterate over all keys and add any extra keys to metadata
        for (Object keyObj : vehicleJson.keySet()) {
            String key = (String) keyObj;
            if (!key.equals("dealership_id") && !key.equals("vehicle_type") &&
                    !key.equals("vehicle_manufacturer") && !key.equals("vehicle_model") &&
                    !key.equals("vehicle_id") && !key.equals("price") &&
                    !key.equals("acquisition_date")) {
                metadata.put(key, vehicleJson.get(key));
            }
        }
        vehicle.setMetadata(metadata);
        return vehicle;
    }

    // Method to export dealership data to a JSON file
    public static void exportDealershipToJson(Dealership dealership, String filePath) {
        JSONObject dealershipJson = new JSONObject();
        JSONArray vehiclesJsonArray = new JSONArray();

        for (Vehicle vehicle : dealership.getVehicles()) {
            JSONObject vehicleJson = new JSONObject();
            vehicleJson.put("dealership_id", dealership.getDealershipId());
            vehicleJson.put("vehicle_id", vehicle.getVehicleId());
            vehicleJson.put("vehicle_manufacturer", vehicle.getManufacture());
            vehicleJson.put("vehicle_model", vehicle.getModel());
            vehicleJson.put("vehicle_type", vehicle.getVehicleType());
            vehicleJson.put("price", vehicle.getPrice());
            vehicleJson.put("acquisition_date", vehicle.getAcquisitionDate());

            // 4: get metadata
            if (vehicle.getMetadata() != null && !vehicle.getMetadata().isEmpty()) {
                vehicleJson.put("metadata", vehicle.getMetadata());
            }

            vehiclesJsonArray.add(vehicleJson);
        }

        dealershipJson.put("car_inventory", vehiclesJsonArray);

        // Export output to new json file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(prettyPrintJson(dealershipJson));
            System.out.println("Dealership data exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting dealership to JSON: " + e.getMessage());
        }
    }

    // Pretty print since without it would print one line format
    private static String prettyPrintJson(JSONObject jsonObject) {
        try {
            // Convert JSON simple to org json object
            org.json.JSONObject jsonPretty = new org.json.JSONObject(new JSONTokener(jsonObject.toJSONString()));
            return jsonPretty.toString(4); // creating 4 indent spaces
        } catch (JSONException e) {
            return jsonObject.toJSONString();
        }
    }
}
