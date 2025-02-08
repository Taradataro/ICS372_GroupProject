package org.example;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!");

    }
}

// Dealer class
class Dealership {
    String dealershipId;
    boolean enabled;
    List<Vehicle> vehicle;

    public Dealership(String dealershipId, boolean enabled) {
        this.dealershipId = dealershipId;
        this.enabled = enabled;
        this.vehicle = new ArrayList<>();
    }

    // 6. Software adding incoming vehicles to a dealer that has enabled receiving vehicles.
    public void addVehicle(Vehicle vehicle) {
        if (enabled) {
            vehicle.add(vehicle);
            System.out.println("Vehicle have been added to dealer." + dealershipId);
        } else {
            System.out.println("Dealer" + dealershipId + "is not enabled to receive vehicle.");
        }
    }

}