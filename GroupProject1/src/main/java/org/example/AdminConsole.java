package org.example;

import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.Scanner;

public class AdminConsole {
    private Dealership dealership;
    private Scanner scanner;

    // Constructor
    public AdminConsole(Dealership dealership) {
        this.dealership = dealership;
        this.scanner = new Scanner(System.in);
    }

    // 5: Method to process admin commands
    public void adminCommands() {
        while (true) {
            System.out.println("\n--- Admin Command Menu ---");
            System.out.println("1. Add incoming vehicle");
            System.out.println("2. Enable dealer vehicle acquisition");
            System.out.println("3. Disable dealer vehicle acquisition");
            System.out.println("4. Export dealership data to JSON");
            System.out.println("5. Exit");
            System.out.print("Enter command number: ");
            // Get user input
            String choice = scanner.nextLine();
            // Select command base on the user input
            switch (choice) {
                case "1":
                    addVehicle();
                    break;
                case "2":
                    // Enable dealer vehicle acquisition
                    dealership.enableReceiving();
                    break;
                case "3":
                    // Disable dealer vehicle acquisition
                    dealership.disableReceiving();
                    break;
                case "4":
                    JsonHelper.exportDealershipToJson(dealership, "output.json");
                    break;
                case "5":
                    System.out.println("Exiting admin command menu.");
                    return;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private void addVehicle() {
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

        // Add vehicle in json file when we add new vehicle
        JsonHelper.exportDealershipToJson(dealership, "output.json");
    }
}
