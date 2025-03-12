package com.example.vehicletracking;

public class Vehicle {
    private String type;
    private String id;
    private String price;
    private String make;
    private String model;

    public Vehicle(String type, String id, String price, String make, String model) {
        this.type = type;
        this.id = id;
        this.price = price;
        this.make = make;
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}
