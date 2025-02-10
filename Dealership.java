
package com.trackingcompany.main;

/**
 *
 * @author 
 */
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


    // Step 5: Enable vehicle acquisition
    public void enableReceiving() {
        if (enabled) {
            System.out.println("Dealer " + dealershipId + " is already enabled.");
        } else {
            enabled = true;
            System.out.println("Dealer " + dealershipId + " is now enabled.");
        }
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

    // Method to print current vehicles
    public void printCurrentVehicles() {
       

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles in inventory.");
        } else {
            //System.out.println("-----------------------------------------");
            System.out.println("Current inventory of vehicles:");
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

    void enableAcquisition() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void disableAcquisition() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
