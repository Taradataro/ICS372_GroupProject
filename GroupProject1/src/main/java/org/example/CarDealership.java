package org.example;
import java.util.ArrayList;
import java.util.List;

public class CarDealership {
    private final String dealershipId;
    private boolean isAcquiringVehicles;
    private final List<Vehicle> inventory;

    public CarDealership(String dealershipId) {
        this.dealershipId = dealershipId;
        this.isAcquiringVehicles = true;
        this.inventory = new ArrayList<>();
    }

    // Add a vehicle to the inventory
    public void addVehicle(Vehicle vehicle) {
        if (!isAcquiringVehicles) {
            System.out.println("Currently disabled for dealership, unable to add vehicle: " + dealershipId);
            return;
        }

        if (vehicle == null) {
            System.out.println("Adding null vehicle is not allowed.");
            return;
        }

        inventory.add(vehicle);
        System.out.println("Vehicle added to dealership " + dealershipId + ": " + vehicle);
    }

    public void enableVehicleAcquisition() {
        if (isAcquiringVehicles) {
            System.out.println("Vehicle acquisition has already been enabled for dealership: " + dealershipId);
            return;
        }
        isAcquiringVehicles = true;
        System.out.println("Vehicle acquisition enabled for dealership: " + dealershipId);
    }


    public void disableVehicleAcquisition() {
        if (!isAcquiringVehicles) {
            System.out.println("Vehicle acquisition is already disabled for dealership: " + dealershipId);
            return;
        }
        isAcquiringVehicles = false;
        System.out.println("Vehicle acquisition disabled for dealership: " + dealershipId);
    }


    public List<Vehicle> getInventory() {
        return new ArrayList<>(inventory);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Car Dealership ID: ").append(dealershipId).append("\n");
        sb.append("Is Acquiring Vehicles: ").append(isAcquiringVehicles ? "Yes" : "No").append("\n");
        sb.append("Inventory:\n");
        for (Vehicle vehicle : inventory) {
            sb.append("  - ").append(vehicle).append("\n");
        }
        return sb.toString();
    }
}