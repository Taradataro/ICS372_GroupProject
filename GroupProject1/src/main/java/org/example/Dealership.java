package org.example;

import java.util.ArrayList;
import java.util.List;

public class Dealership {
    private String dealershipId;
    private boolean enabled; // enabled receiving vehicle
    private List<Vehicle> vehicles;

    // Constructor
    public Dealership(String dealershipId, boolean enabled) {
        this.dealershipId = dealershipId;
        this.enabled = enabled;
        this.vehicles = new ArrayList<>();
    }

    // 6. Add vehicle if dealership has enabled receiving vehicles
    public void addVehicle(Vehicle vehicle) {
        if (enabled) {
            vehicles.add(vehicle);
            System.out.println("Vehicle " + vehicle.getVehicleId() + " has been added to dealer " + dealershipId + ".");
        } else {
            System.out.println("Dealer " + dealershipId + " is not accepting new vehicles.");
        }
    }

    // 5: New method to enable receiving vehicles :
    public void enableReceiving() {
        if (enabled) {
            System.out.println("Dealer " + dealershipId + " is already enabled to receive vehicles.");
        } else {
            enabled = true;
            System.out.println("Dealer " + dealershipId + " is now enabled to receive vehicles.");
        }
    }

    // 7. Disable receiving vehicles
    public void disableReceiving() {
        if (!enabled) {
            System.out.println("Dealer " + dealershipId + " is already disabled and can't receive vehicles.");
        } else {
            enabled = false;
            System.out.println("Dealer " + dealershipId + " is now disabled and can't receive vehicles.");
        }
    }

    // Method to print current vehicles in inventory
    public void printCurrentVehicles() {
        System.out.println("\n\n--- Current Vehicle Inventory ---\"");
        // If there is no vehicle
        if (vehicles.isEmpty()) {
            System.out.println("\nNo vehicles in inventory.");
        } else {
            for (Vehicle vehicle : vehicles) {
                vehicle.displayVehicleInfo();
            }
        }
    }

    // Get list of vehicles
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    // Getter for dealershipId
    public String getDealershipId() {
        return dealershipId;
    }

    // Override toString to display dealership info
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dealership ID: ").append(dealershipId).append("\n");
        sb.append("Receiving Vehicles: ").append(enabled ? "Yes" : "No").append("\n");
        sb.append("Inventory:\n");
        for (Vehicle vehicle : vehicles) {
            sb.append("  - ").append(vehicle).append("\n");
        }
        return sb.toString();
    }
}

