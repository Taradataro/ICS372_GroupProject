package com.trackingcompany.main;

/**
 *
 * @author 
 */
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Vehicle {

    static Vehicle create(String type, String id, String maker, String model, LocalDate now, double price) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private String vehicleId;
    private String manufacture;
    private String model;
    private LocalDate acquisitionDate; // Changed to LocalDate
    private double price;
    private String vehicleType;
    private Map<String, Object> metadata;

    // Constructor that accepts LocalDate for acquisitionDate
    public Vehicle(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price, String vehicleType) {
        this.vehicleId = vehicleId;
        this.manufacture = manufacture;
        this.model = model;
        this.acquisitionDate = acquisitionDate;
        this.price = price;
        this.vehicleType = vehicleType;
        this.metadata = new HashMap<>();
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
    // Step 4: Method to add metadata
    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    // Step 4: Getter for metadata
    public Map<String, Object> getMetadata() {
        return metadata;
    }


    //toString method that return the vehicles object
    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", model='" + model + '\'' +
                ", acquisitionDate=" + acquisitionDate +
                ", price=" + price +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }
}
