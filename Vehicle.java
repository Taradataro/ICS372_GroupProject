package org.example;

import java.time.LocalDate;

public class Vehicle {
    private String vehicleId;
    private String manufacture;
    private String model;
    private LocalDate acquisitionDate;
    private double price;
    private String vehicleType;

    public Vehicle(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price, String vehicleType) {
        this.vehicleId = vehicleId;
        this.manufacture = manufacture;
        this.model = model;
        this.acquisitionDate = acquisitionDate;
        this.price = price;
        this.vehicleType = vehicleType;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getManufacture() {
        return manufacture;
    }

    public String getModel() {
        return model;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public double getPrice() {
        return price;
    }

    public String getVehicleType() {
        return vehicleType;
    }
}
