package com.example.vehicletracking;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Load dealership data from JSON
        String filePath = Paths.get("src", "main", "java", "org", "example", "Dealers.xml").toAbsolutePath().toString();
        Dealership dealership = jsonExport.loadDealershipFromJson(filePath);

        // If no data is found, create a new dealership
        if (dealership == null) {
            dealership = new Dealership("77338", true);
            System.out.println("No existing dealership data found. Created a new dealership with ID 77338.");
        } else {
            System.out.println("Dealership data loaded successfully.");
        }

        // Launch the GUI
        System.out.println("Launching Dealership Management System GUI...");
        new DealershipGUI(dealership);
    }
}