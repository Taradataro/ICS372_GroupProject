package org.example.dealershipapplication;

import java.time.LocalDate;

public class Vehicle {
    private String id;
    private String make;
    private String model;
    private LocalDate acquisitionDate;
    private double price;
    private String type;
    private boolean loaned;

    public Vehicle(String id, String make, String model,
                   LocalDate acquisitionDate, double price, String type) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.acquisitionDate = acquisitionDate;
        this.price = price;
        this.type = type;
        this.loaned = false;
    }

    public String getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public LocalDate getAcquisitionDate() { return acquisitionDate; }
    public double getPrice() { return price; }
    public String getType() { return type; }
    public boolean isLoaned() { return loaned; }

    public void setLoaned(boolean loaned) {
        // Sports cars are not allowed to be loaned
        if (!"sports car".equalsIgnoreCase(type)) {
            this.loaned = loaned;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s (%s) - $%.2f - %s",
                make, model, type, price, loaned ? "Loaned" : "Available");
    }
}
