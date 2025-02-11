package org.example;

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

    // Add a vehicle to the dealership if enabled
    public void addVehicle(Vehicle vehicle) {
        if (enabled) {
            vehicles.add(vehicle);
            System.out.println("Vehicle has been added to dealer " + dealershipId + ".");
        } else {
            System.out.println("Dealer " + dealershipId + " is not enabled to receive vehicles.");
        }
    }

    // Enable dealership to receive vehicles
    public void enableReceiving() {
        enabled = true;
        System.out.println("Dealer " + dealershipId + " is now enabled to receive vehicles.");
    }

    // Disable dealership from receiving new vehicles
    public void disableReceiving() {
        enabled = false;
        System.out.println("Dealer " + dealershipId + " is now disabled and can't receive vehicles.");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getDealershipId() {
        return dealershipId;
    }

    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }

    // Print dealership inventory with formatted vehicle details
    public void printCurrentVehicles() {
        System.out.println("Current inventory of vehicles for dealer " + dealershipId + ":");
        System.out.println("-----------------------------------------");
        for (Vehicle vehicle : vehicles) {
            System.out.println("Vehicle ID: " + vehicle.getVehicleId());
            System.out.println("Manufacturer: " + vehicle.getManufacture());
            System.out.println("Model: " + vehicle.getModel());
            System.out.println("Price: " + vehicle.getPrice());
            System.out.println("Acquisition Date: " + vehicle.getAcquisitionDate());
            System.out.println("Vehicle Type: " + vehicle.getVehicleType());
            System.out.println("-----------------------------------------");
        }
    }
}