package org.example;

import java.time.LocalDate;
import org.json.simple.JSONObject;

public class Vehicle {
    private String vehicleId;
    private String manufacture;
    private String model;
    private LocalDate acquisitionDate; // Changed to LocalDate
    private double price;
    private String vehicleType;
    private JSONObject metadata;


    // Constructor that accepts LocalDate for acquisitionDate
    public Vehicle(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price, String vehicleType) {
        this.vehicleId = vehicleId;
        this.manufacture = manufacture;
        this.model = model;
        this.acquisitionDate = acquisitionDate;
        this.price = price;
        this.vehicleType = vehicleType;
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

    // 4: Getter and setter for metadata:
    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    //toString method that return the vehicles object
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
                metaStr +
                '}';
    }
}

