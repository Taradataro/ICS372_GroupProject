package org.example;

import java.time.LocalDate;

public class SportsCar extends Vehicle {
    public SportsCar(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price) {
        super(vehicleId, manufacture, model, acquisitionDate, price, "sports car");
    }
}
