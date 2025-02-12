package org.example;

import java.time.LocalDate;
import org.json.simple.JSONObject;

public class Vehicle {
    // Instance fields
    private String vehicleId;
    private String manufacturer;
    private String model;
    private LocalDate acquisitionDate; // Changed to LocalDate
    private double price;
    private String vehicleType;
    private JSONObject metadata;


    // Constructor that accepts LocalDate for acquisitionDate
    public Vehicle(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price, String vehicleType) {
        this.vehicleId = vehicleId;
        this.manufacturer = manufacture;
        this.model = model;
        this.acquisitionDate = acquisitionDate;
        this.price = price;
        this.vehicleType = vehicleType;
        this.metadata = new JSONObject();
    }

    // Overloading constructor
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
        return manufacturer;
    }

    public void setManufacture(String manufacture) {
        this.manufacturer = manufacture;
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

    // Method for display Vehicle information
    public void displayVehicleInfo() {
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Manufacturer: " + manufacturer);
        System.out.println("Model: " + model);
        System.out.println("Price: " + price);
        System.out.println("Acquisition Date: " + acquisitionDate);
        System.out.println("Vehicle Type: " + vehicleType);
        if (!metadata.isEmpty()) {
            System.out.println("Metadata: " + metadata.toJSONString());
        }
        System.out.println("-----------------------------------------");
    }

    //toString method that return the vehicles object
    @Override
    public String toString() {
        String metaStr = (metadata != null && !metadata.isEmpty()) ? ", metadata=" + metadata : "";
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", manufacture='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", acquisitionDate=" + acquisitionDate +
                ", price=" + price +
                ", vehicleType='" + vehicleType + '\'' +
                metaStr +
                '}';
    }
}

