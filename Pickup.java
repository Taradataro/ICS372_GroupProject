package org.example;

import java.time.LocalDate;

public class Pickup extends Vehicle {
    public Pickup(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price) {
        super(vehicleId, manufacture, model, acquisitionDate, price, "pickup");
    }
}
