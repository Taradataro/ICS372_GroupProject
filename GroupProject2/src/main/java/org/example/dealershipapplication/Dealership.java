package org.example.dealershipapplication;

import java.util.ArrayList;
import java.util.List;

public class Dealership {
    private String id;
    private String name;
    private boolean receivingEnabled;
    private List<Vehicle> vehicles;

    public Dealership(String id, boolean receivingEnabled) {
        this.id = id;
        this.receivingEnabled = receivingEnabled;
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        if (receivingEnabled) {
            vehicles.add(vehicle);
            System.out.println("Vehicle added: " + vehicle);
        } else {
            System.out.println("Cannot add vehicle; receiving is disabled.");
        }
    }

    public void loanVehicle(String vehicleId) {
        vehicles.stream()
                .filter(v -> v.getId().equals(vehicleId))
                .findFirst()
                .ifPresent(v -> {
                    v.setLoaned(true);
                    System.out.println("Vehicle loaned: " + v);
                });
    }

    public void returnVehicle(String vehicleId) {
        vehicles.stream()
                .filter(v -> v.getId().equals(vehicleId))
                .findFirst()
                .ifPresent(v -> {
                    v.setLoaned(false);
                    System.out.println("Vehicle returned: " + v);
                });
    }

    /**
     * Remove a vehicle from this dealership by ID.
     */
    public void removeVehicle(String vehicleId) {
        vehicles.removeIf(v -> v.getId().equals(vehicleId));
    }

    public void printCurrentVehicles() {
        System.out.println("\nCurrent Inventory for " + (name != null ? name : "Unknown") + ":");
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles in inventory.");
            return;
        }
        vehicles.forEach(System.out::println);
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isReceivingEnabled() { return receivingEnabled; }
    public List<Vehicle> getVehicles() { return new ArrayList<>(vehicles); }

    public void setDealerName(String name) { this.name = name; }
    public void enableReceiving() { this.receivingEnabled = true; }
    public void disableReceiving() { this.receivingEnabled = false; }
}
