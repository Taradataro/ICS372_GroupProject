package com.example.vehicletracking;

import java.util.List;
import java.util.ArrayList;

public class Dealer {
    private String name;
    private List<Vehicle> vehicles;

    public Dealer(String name) {
        this.name = name;
        this.vehicles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
