package com.trackingcompany.main;

/**
 *
 * @author ahlam
 */
public class Sedan extends Vehicle {
    //Represents a Sedan. This class extends the Vehicle class and inherits its properties
    public Sedan(String vehicleId, String manufacture, String model, String acquisitionDate, double price) {
        super(vehicleId, manufacture, model, acquisitionDate, price);
    }
}