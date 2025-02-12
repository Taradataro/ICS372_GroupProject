// Main.java
package org.example;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Use Path object for more flexible file paths
        String filePath = Paths.get("src", "main", "java", "org", "example", "testFile.json").toAbsolutePath().toString();

        // Create a dealership and enable vehicle acquisition
        Dealership dealership = new Dealership("77338", true);

        // Read and add vehicles from JSON file (Step 4: now captures metadata)
        JsonHelper.readAndAddVehicleJson(filePath, dealership);

        // Print current vehicles
        dealership.printCurrentVehicles();

        // Disable the dealership from receiving new vehicles
        dealership.disableReceiving();

        // Print the current vehicles again
        dealership.printCurrentVehicles();

        // Export dealership data to JSON (includes metadata if available)
        JsonHelper.exportDealershipToJson(dealership, "userEnable.json");

        // Step 5: Process admin commands interactively for adding vehicles,
        // enabling/disabling dealer acquisition.
        AdminConsole adminConsole = new AdminConsole(dealership);
        adminConsole.adminCommands();
    }
}
