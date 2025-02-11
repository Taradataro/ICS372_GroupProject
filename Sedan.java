package org.example;

import java.time.LocalDate;

public class Sedan extends Vehicle {
    public Sedan(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price) {
        super(vehicleId, manufacture, model, acquisitionDate, price, "sedan");
    }
}
