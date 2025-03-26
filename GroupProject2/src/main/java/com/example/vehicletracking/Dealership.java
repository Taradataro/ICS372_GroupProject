package com.example.vehicletracking;

import java.util.ArrayList;
import java.util.List;

public class Dealership {
    private String dealershipId;
    private boolean enabled;
    private List<Vehicle> vehicles;

    // Constructor
    public Dealership(String dealershipId, boolean enabled) {
        this.dealershipId = dealershipId;
        this.enabled = enabled;
        this.vehicles = new ArrayList<>();
    }

    // Add vehicle if dealership is enabled
    public void addVehicle(Vehicle vehicle) {
        if (enabled) {
            vehicles.add(vehicle);
            System.out.println("Vehicle " + vehicle.getVehicleId() + " has been added to dealer " + dealershipId + ".");
        } else {
            System.out.println("Dealer " + dealershipId + " is not enabled to receive vehicles.");
        }
    }

    // Disable receiving vehicles
    public void disableReceiving() {
        if (!enabled) {
            System.out.println("Dealer " + dealershipId + " is already disabled and can't receive vehicles.");
        } else {
            enabled = false;
            System.out.println("Dealer " + dealershipId + " is now disabled and can't receive vehicles.");
        }
    }

    // Enable receiving vehicles
    public void enableReceiving() {
        if (enabled) {
            System.out.println("Dealer " + dealershipId + " is already enabled to receive vehicles.");
        } else {
            enabled = true;
            System.out.println("Dealer " + dealershipId + " is now enabled to receive vehicles.");
        }
    }

    // Loan a vehicle
    public void loanVehicle(String vehicleId) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                vehicle.setLoaned(true);
                System.out.println("Vehicle " + vehicleId + " has been loaned out.");
                return;
            }
        }
        System.out.println("Vehicle " + vehicleId + " not found.");
    }

    // Return a vehicle
    public void returnVehicle(String vehicleId) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                vehicle.setLoaned(false);
                System.out.println("Vehicle " + vehicleId + " has been returned.");
                return;
            }
        }
        System.out.println("Vehicle " + vehicleId + " not found.");
    }

    // Transfer a vehicle to another dealership
    public void transferVehicle(String vehicleId, Dealership targetDealership) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                if (targetDealership.isEnabled()) {
                    targetDealership.addVehicle(vehicle);
                    vehicles.remove(vehicle);
                    System.out.println("Vehicle " + vehicleId + " transferred to dealership " + targetDealership.getDealershipId());
                } else {
                    System.out.println("Target dealership is not enabled to receive vehicles.");
                }
                return;
            }
        }
        System.out.println("Vehicle " + vehicleId + " not found.");
    }

    // Get list of vehicles
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    // Get dealership status
    public boolean isEnabled() {
        return enabled;
    }

    // Getter for dealershipId
    public String getDealershipId() {
        return dealershipId;
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

    // Print current vehicles
    public void printCurrentVehicles() {
        if (vehicles.isEmpty()) {
            System.out.println("\nNo vehicles in inventory.");
        } else {
            System.out.println("\nCurrent inventory of vehicles:");
            for (Vehicle vehicle : vehicles) {
                System.out.println("Vehicle ID: " + vehicle.getVehicleId());
                System.out.println("Manufacturer: " + vehicle.getManufacture());
                System.out.println("Model: " + vehicle.getModel());
                System.out.println("Price: " + vehicle.getPrice());
                System.out.println("Acquisition Date: " + vehicle.getAcquisitionDate());
                System.out.println("Vehicle Type: " + vehicle.getVehicleType());
                System.out.println("Loaned: " + (vehicle.isLoaned() ? "Yes" : "No"));
                System.out.println("***************************************");
                if (vehicle.getMetadata() != null && !vehicle.getMetadata().isEmpty()) {
                    System.out.println("Metadata: " + vehicle.getMetadata());
                }
            }
        }
    }

    public void pickupVehicle(String vehicleId) {

    }
}