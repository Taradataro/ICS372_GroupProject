package org.example;

import java.util.ArrayList;
import java.util.List;

public class Dealership {
    private String dealershipId;
    private boolean enabled;
    private List<Vehicle> vehicles;

    public Dealership(String dealershipId, boolean enabled) {
        this.dealershipId = dealershipId;
        this.enabled = enabled;
        this.vehicles = new ArrayList<>();
    }

    // 6. Add vehicle if dealership is enabled
    public void addVehicle(Vehicle vehicle) {
        if (enabled) {
            vehicles.add(vehicle);
            System.out.println("Vehicle has been added to dealer " + dealershipId + ".");
        } else {
            System.out.println("Dealer " + dealershipId + " is not enabled to receive vehicles.");
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

    // Get list of vehicles
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    // Get dealership status
    public boolean isEnabled() {
        return enabled;
    }

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
