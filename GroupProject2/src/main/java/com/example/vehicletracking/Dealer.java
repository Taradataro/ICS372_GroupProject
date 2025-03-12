package com.example.vehicletracking;

import java.util.List;

public class Dealer {
    private String name;
    private String id;
    private List<Vehicle> vehicles;

    public Dealer(String name, String id, List<Vehicle> vehicles) {
        this.name = name;
        this.id = id;
        this.vehicles = vehicles;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}

