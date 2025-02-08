package org.example;

public class Vehicle {
    private String vehicleId;
    private String manufacture;
    private String model;
    private String acquisitionDate;
    private double price;

    public Vehicle(String vehicleId, String manufacture, String model, String acquisitionDate, double price) {
        this.vehicleId = vehicleId;
        this.manufacture = manufacture;
        this.model = model;
        this.acquisitionDate = acquisitionDate;
        this.price = price;
    }

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

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", model='" + model + '\'' +
                ", acquisitionDate='" + acquisitionDate + '\'' +
                ", price=" + price +
                '}';
    }

}

