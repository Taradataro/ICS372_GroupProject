package org.example.hellofx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vehicle {
    private StringProperty id;
    private StringProperty type;
    private StringProperty make;
    private StringProperty model;
    private StringProperty price;
    private String status;


    // Default constructor
    public Vehicle() {
        this("","", "", "", "");
    }

    public Vehicle(StringProperty id, StringProperty type, StringProperty model, StringProperty make, StringProperty price, String status) {
        this.id = id;
        this.type = type;
        this.model = model;
        this.make = make;
        this.price = price;
        this.status = status;
    }

    // Property Getter method
    public StringProperty idProperty() {return id;}
    public StringProperty typeProperty() {return type;}
    public StringProperty makeProperty() {return make;}
    public StringProperty modelProperty() {return model;}
    public StringProperty priceProperty() {return price;}

    // Constructor with type, id, price, make, and model
    public Vehicle(String type, String id, String price, String make, String model) {
        this.id = new SimpleStringProperty(id);
        this.type = new SimpleStringProperty(type);
        this.make = new SimpleStringProperty(make);
        this.model = new SimpleStringProperty(model);
        this.price = new SimpleStringProperty(price);
        this.status = "Available";
    }

    // Getters and Setters for each property
    public String getType() { return type.get(); }
    public String getId() {
        return id.get();
    }
    public String getPrice() {
        return price.get();
    }
    public String getMake() {
        return make.get();
    }
    public String getModel() {
        return model.get();
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + getId() +
                ", type=" + getType() +
                ", make=" + getMake() +
                ", model=" + getModel() +
                ", price=" + getPrice() +
                '\'' +
                '}';
    }
}
