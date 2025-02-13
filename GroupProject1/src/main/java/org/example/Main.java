// Main.java
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
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Use Path object for more flexible file paths
        String filePath = Paths.get("src", "main", "java", "org", "example", "testFile.json").toAbsolutePath().toString();

        // Create a dealership and enable vehicle acquisition
        Dealership dealership = new Dealership("77338", true);

        // Read and add vehicles from JSON file (Step 4: now captures metadata)
        readAndAddVehiclesFromJson(filePath, dealership);

        // Print current vehicles
        dealership.printCurrentVehicles();

        // Disable the dealership from receiving new vehicles
        dealership.disableReceiving();

        // Print the current vehicles again
        dealership.printCurrentVehicles();

        // Export dealership data to JSON (includes metadata if available)
        jsonExport.exportDealershipToJson(dealership, "userEnable.json");

        // ----------------------------------------------------------------
        // Step 5: Process admin commands interactively for adding vehicles,
        // enabling/disabling dealer acquisition.
        processAdminCommands(dealership);
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
                // Cast price to double (if needed)
                double price = ((Number) vehicleJson.get("price")).doubleValue();
                long acquisitionDateLong = (long) vehicleJson.get("acquisition_date");
                String vehicleType = (String) vehicleJson.get("vehicle_type");

                // Convert acquisition date from timestamp to LocalDate
                LocalDate acquisitionDate = convertTimestampToDate(acquisitionDateLong);

                // ***** STEP 4: Extract and store additional metadata *****
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

                // Create a new Vehicle and set its metadata
                Vehicle vehicle = new Vehicle(vehicleId, manufacturer, model, acquisitionDate, price, vehicleType);
                vehicle.setMetadata(metadata);
                dealership.addVehicle(vehicle);
            }

        } catch (IOException | ParseException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }

    // Helper method to convert timestamp to LocalDate
    private static LocalDate convertTimestampToDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // 5: New method to process admin commands
    private static void processAdminCommands(Dealership dealership) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Admin Command Menu ---");
            System.out.println("1. Add incoming vehicle");
            System.out.println("2. Enable dealer vehicle acquisition");
            System.out.println("3. Disable dealer vehicle acquisition");
            System.out.println("4. Exit");
            System.out.print("Enter command number: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                // Add incoming vehicle
                System.out.print("Enter vehicle id: ");
                String id = scanner.nextLine();
                System.out.print("Enter manufacturer: ");
                String manu = scanner.nextLine();
                System.out.print("Enter model: ");
                String model = scanner.nextLine();
                System.out.print("Enter acquisition date (yyyy-MM-dd): ");
                String dateStr = scanner.nextLine();
                LocalDate date = LocalDate.parse(dateStr);
                System.out.print("Enter price: ");
                double price = Double.parseDouble(scanner.nextLine());
                System.out.print("Enter vehicle type: ");
                String type = scanner.nextLine();

                // Prompt for metadata entries
                JSONObject metadata = new JSONObject();
                System.out.print("Enter number of metadata entries (0 if none): ");
                int metaCount = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < metaCount; i++) {
                    System.out.print("Enter metadata key: ");
                    String key = scanner.nextLine();
                    System.out.print("Enter metadata value: ");
                    String value = scanner.nextLine();
                    metadata.put(key, value);
                }

                Vehicle vehicle = new Vehicle(id, manu, model, date, price, type);
                vehicle.setMetadata(metadata);
                dealership.addVehicle(vehicle);

            } else if (choice.equals("2")) {
                // Enable dealer vehicle acquisition
                dealership.enableReceiving();
            } else if (choice.equals("3")) {
                // Disable dealer vehicle acquisition
                dealership.disableReceiving();
            } else if (choice.equals("4")) {
                System.out.println("Exiting admin command menu.");
                break;
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }

    }
}
