
package com.trackingcompany.main;

/**
 *
 * @author 
 */

import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.nio.file.Paths;
import java.time.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Dealership dealer1 = new Dealership("77338", true);
        Dealership dealer2 = new Dealership("12233", true);
        loadInitialData(dealer1, "testFile.json");
        loadInitialData(dealer2, "testFile1.json");

        // STEP 5: Command-line interface
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== Dealer System ===");
                System.out.println("1. Add Vehicle");
                System.out.println("2. Toggle Acquisition");
                System.out.println("3. Export Data");
                System.out.println("4. Exit");
                System.out.print("Choose: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                if (choice == 4) break;

                Dealership target = selectDealer(scanner, dealer1, dealer2);
                if (target == null) continue;

                switch (choice) {
                    case 1 -> addVehicle(scanner, target);
                    case 2 -> toggleAcquisition(scanner, target);
                    case 3 -> exportData(scanner, target);
                    default -> System.out.println("Invalid choice!");
                }
            }
        }
    }

    private static Dealership selectDealer(Scanner scanner, Dealership... dealers) {
        System.out.print("Enter Dealer ID: ");
        String id = scanner.nextLine();
        for (Dealership dealer : dealers) {
            if (dealer.getDealershipId().equals(id)) return dealer;
        }
        System.out.println("Dealer not found!");
        return null;
    }

    private static void addVehicle(Scanner scanner, Dealership dealer) {
        System.out.print("Vehicle ID: ");
        String id = scanner.nextLine();
        System.out.print("Manufacturer: ");
        String maker = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Type (SUV/Sedan/Pickup/Sports Car): ");
        String type = scanner.nextLine();

        Vehicle vehicle = Vehicle.create(type, id, maker, model, LocalDate.now(), price);
        dealer.addVehicle(vehicle); // STEP 5: Add vehicle command
    }

    private static void toggleAcquisition(Scanner scanner, Dealership dealer) {
        System.out.print("Enable (Y/N): ");
        boolean enable = scanner.nextLine().equalsIgnoreCase("Y");
        if (enable) dealer.enableAcquisition(); // STEP 5: Toggle command
        else dealer.disableAcquisition();
    }

    private static void exportData(Scanner scanner, Dealership dealer) {
        System.out.print("Output file: ");
        String path = scanner.nextLine();
        jsonExport.export(dealer, path); // STEP 8: Export command
    }

    // STEP 1: Load JSON data with metadata
    private static void loadInitialData(Dealership dealer, String filename) {
        try (FileReader reader = new FileReader(Paths.get("src", "main", "java", "org", "example", filename).toString())) {
            JSONArray inventory = (JSONArray) new JSONParser().parse(reader);
            inventory.forEach(item -> {
                JSONObject data = (JSONObject) item;
                Vehicle vehicle = Vehicle.create(
                    (String) data.get("vehicle_type"),
                    (String) data.get("vehicle_id"),
                    (String) data.get("vehicle_manufacturer"),
                    (String) data.get("vehicle_model"),
                    Instant.ofEpochMilli((Long) data.get("acquisition_date"))
                           .atZone(ZoneId.systemDefault()).toLocalDate(),
                    ((Number) data.get("price")).doubleValue()
                );
                // STEP 4: Load metadata
                JSONObject metadata = (JSONObject) data.get("metadata");
                if (metadata != null) metadata.forEach((k, v) -> 
                    vehicle.addMetadata((String) k, v)
                );
                dealer.addVehicle(vehicle);
            });
        } catch (IOException | ParseException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}
