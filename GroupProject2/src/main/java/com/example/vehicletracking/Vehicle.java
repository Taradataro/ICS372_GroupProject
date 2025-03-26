package com.example.vehicletracking;

import java.time.LocalDate;
import org.json.simple.JSONObject;

public class Vehicle {
    private String vehicleId;
    private String manufacture;
    private String model;
    private LocalDate acquisitionDate;
    private double price;
    private String vehicleType;
    private JSONObject metadata;
    private boolean isLoaned;

    // Constructor
    public Vehicle(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price, String vehicleType) {
        this.vehicleId = vehicleId;
        this.manufacture = manufacture;
        this.model = model;
        this.acquisitionDate = acquisitionDate;
        this.price = price;
        this.vehicleType = vehicleType;
        this.isLoaned = false;
    }

    public Vehicle(String vehicleId, String manufacture, String model, String acquisitionDate, double price) {

    }

    // Getters and Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public boolean isLoaned() {
        return isLoaned;
    }

    public void setLoaned(boolean loaned) {
        if (this.vehicleType.equalsIgnoreCase("sports car")) {
            System.out.println("Sports cars cannot be loaned.");
        } else {
            this.isLoaned = loaned;
        }
    }

    @Override
    public String toString() {
        String metaStr = (metadata != null && !metadata.isEmpty()) ? ", metadata=" + metadata : "";
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", model='" + model + '\'' +
                ", acquisitionDate=" + acquisitionDate +
                ", price=" + price +
                ", vehicleType='" + vehicleType + '\'' +
                ", isLoaned=" + isLoaned +
                metaStr +
                '}';
    }
}