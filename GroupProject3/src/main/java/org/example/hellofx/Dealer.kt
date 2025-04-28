package org.example.hellofx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    // Fields
    private StringProperty id;
    private StringProperty name;
    private List<Vehicle> vehicles;


    public StringProperty idProperty() {return id;}
    public StringProperty nameProperty() {return name;}

    // Default constructor
    public Dealer() {
        this("", "");
    }
    public Dealer(StringProperty id, StringProperty name) {
        this.id = id;
        this.name = name;
        this.vehicles = new ArrayList<>();
    }

    public Dealer(String id, String name) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.vehicles = new ArrayList<>();
    }
    // Setter for vehicles
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    // Getter and setter methods
    public String getId() {
        return id.get();
    }
    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {return name.get();}
    public void setName(String name) {
        this.name.set(name);
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dealer ID: ").append(id.get()).append("\n");
        sb.append("Name: ").append(name.get()).append("\n");
        sb.append("Vehicles:\n");
        for (Vehicle vehicle : vehicles) {
            sb.append("  - ").append(vehicle.getMake()).append(" ").append(vehicle.getModel())
                    .append(" (").append(vehicle.getType()).append(")").append("\n");
        }
        return sb.toString();
    }

}
