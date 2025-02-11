package org.example;

import java.time.LocalDate;

public class SUV extends Vehicle {
    public SUV(String vehicleId, String manufacture, String model, LocalDate acquisitionDate, double price) {
        super(vehicleId, manufacture, model, acquisitionDate, price, "suv");
    }
}
