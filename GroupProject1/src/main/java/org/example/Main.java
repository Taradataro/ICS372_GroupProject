package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class Main {
    public static void main(String[] args) {
        // Use Path object for more flexible file paths
        String filePath = Paths.get("src", "main", "java", "org", "example", "testFile.json").toAbsolutePath().toString();

        // Create a dealership and enable vehicle acquisition
        Dealership dealership = new Dealership("77338", true);

        // Read and add vehicles from JSON file
        readAndAddVehiclesFromJson(filePath, dealership);

        // Print current vehicles
        dealership.printCurrentVehicles();

        // Disable the dealership from receiving new vehicles
        dealership.disableReceiving();

        // Print the current vehicles again
        dealership.printCurrentVehicles();

        // Export dealership data to JSON
        jsonExport.exportDealershipToJson(dealership, "userEnable.json");
    }

    private static void readAndAddVehiclesFromJson(String filePath, Dealership dealership) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse the JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            // Extract the car inventory array
            JSONArray carInventory = (JSONArray) jsonObject.get("car_inventory");

            // Print out the car inventory (for debugging or informational purposes)
            System.out.println(" ");
            System.out.println("Car Inventory from JSON:");
            for (Object obj : carInventory) {
                JSONObject vehicleJson = (JSONObject) obj;

                // Print each vehicle's details from the JSON
                System.out.println("Vehicle ID: " + vehicleJson.get("vehicle_id"));
                System.out.println("Manufacturer: " + vehicleJson.get("vehicle_manufacturer"));
                System.out.println("Model: " + vehicleJson.get("vehicle_model"));
                System.out.println("Price: " + vehicleJson.get("price"));
                System.out.println("Acquisition Date: " + vehicleJson.get("acquisition_date"));
                System.out.println("Vehicle Type: " + vehicleJson.get("vehicle_type"));
                System.out.println("-----------------------------------------");
            }

            // Iterate over each vehicle and add it to the dealership
            for (Object obj : carInventory) {
                JSONObject vehicleJson = (JSONObject) obj;

                // Extract the vehicle details
                String vehicleId = (String) vehicleJson.get("vehicle_id");
                String manufacturer = (String) vehicleJson.get("vehicle_manufacturer");
                String model = (String) vehicleJson.get("vehicle_model");
                long price = (long) vehicleJson.get("price");
                long acquisitionDateLong = (long) vehicleJson.get("acquisition_date");
                String vehicleType = (String) vehicleJson.get("vehicle_type");

                // Convert acquisition date from timestamp to LocalDate
                LocalDate acquisitionDate = convertTimestampToDate(acquisitionDateLong);

                // Create a new vehicle and add it to the dealership
                Vehicle vehicle = new Vehicle(vehicleId, manufacturer, model, acquisitionDate, price, vehicleType);
                dealership.addVehicle(vehicle);
            }

        } catch (IOException | ParseException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }

    // Helper method to convert timestamp to LocalDate
    private static LocalDate convertTimestampToDate(long timestamp) {
        // Convert the timestamp (in milliseconds) to an Instant, then to ZonedDateTime, and finally to LocalDate
        return Instant.ofEpochMilli(timestamp)  // Convert timestamp (milliseconds) to Instant
                .atZone(ZoneId.systemDefault())  // Convert Instant to ZonedDateTime using system's default timezone
                .toLocalDate();  // Extract the LocalDate part
    }

}



