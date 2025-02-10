package com.trackingcompany.main;

/**
 *
 * @author ahlam
 */
public class Pickup extends Vehicle{
    //Represents a Pick up. This class extends the Vehicle class and inherits its properties
    public Pickup(String vehicleId, String manufacture, String model, String acquisitionDate, double price) {
        super(vehicleId, manufacture, model, acquisitionDate, price);
    }
}
