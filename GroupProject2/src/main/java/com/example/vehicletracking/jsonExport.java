package com.example.vehicletracking;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDate;
import org.json.JSONException;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class jsonExport {

    // Export dealership data to JSON
    public static void exportDealershipToJson(Dealership dealership, String filePath) {
        JSONObject dealershipJson = new JSONObject();
        JSONArray vehiclesJsonArray = new JSONArray();

        for (Vehicle vehicle : dealership.getVehicles()) {
            JSONObject vehicleJson = new JSONObject();
            vehicleJson.put("dealership_id", dealership.getDealershipId());
            vehicleJson.put("vehicle_manufacturer", vehicle.getManufacture());
            vehicleJson.put("vehicle_model", vehicle.getModel());
            vehicleJson.put("vehicle_id", vehicle.getVehicleId());
            vehicleJson.put("price", vehicle.getPrice());
            vehicleJson.put("acquisition_date", vehicle.getAcquisitionDate().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000);
            vehicleJson.put("vehicle_type", vehicle.getVehicleType());
            vehicleJson.put("is_loaned", vehicle.isLoaned());

            if (vehicle.getMetadata() != null && !vehicle.getMetadata().isEmpty()) {
                vehicleJson.put("metadata", vehicle.getMetadata());
            }

            vehiclesJsonArray.add(vehicleJson);
        }

        dealershipJson.put("car_inventory", vehiclesJsonArray);

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(prettyPrintJson(dealershipJson));
            System.out.println("Dealership data exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting dealership to JSON: " + e.getMessage());
        }
    }

    // Load dealership data from JSON
    public static Dealership loadDealershipFromJson(String filePath) {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            String dealershipId = (String) jsonObject.get("dealership_id");
            boolean enabled = (boolean) jsonObject.get("enabled");
            Dealership dealership = new Dealership(dealershipId, enabled);

            JSONArray vehiclesJsonArray = (JSONArray) jsonObject.get("car_inventory");
            for (Object obj : vehiclesJsonArray) {
                JSONObject vehicleJson = (JSONObject) obj;
                String vehicleId = (String) vehicleJson.get("vehicle_id");
                String manufacturer = (String) vehicleJson.get("vehicle_manufacturer");
                String model = (String) vehicleJson.get("vehicle_model");
                double price = ((Number) vehicleJson.get("price")).doubleValue();
                long acquisitionDateLong = (long) vehicleJson.get("acquisition_date");
                LocalDate acquisitionDate = Instant.ofEpochMilli(acquisitionDateLong)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                String vehicleType = (String) vehicleJson.get("vehicle_type");
                boolean isLoaned = (boolean) vehicleJson.get("is_loaned");

                JSONObject metadata = (JSONObject) vehicleJson.get("metadata");

                Vehicle vehicle = new Vehicle(vehicleId, manufacturer, model, acquisitionDate, price, vehicleType);
                vehicle.setMetadata(metadata);
                vehicle.setLoaned(isLoaned);
                dealership.addVehicle(vehicle);
            }
            return dealership;
        } catch (IOException | ParseException e) {
            System.err.println("Error loading dealership from JSON: " + e.getMessage());
            return null;
        }
    }

    // Pretty print JSON
    private static String prettyPrintJson(JSONObject jsonObject) {
        try {
            org.json.JSONObject jsonPretty = new org.json.JSONObject(new JSONTokener(jsonObject.toJSONString()));
            return jsonPretty.toString(4);
        } catch (JSONException e) {
            return jsonObject.toJSONString();
        }
    }
}