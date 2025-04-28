package org.example.hellofx

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

class Dealer {
    private val idProp: StringProperty
    private val nameProp: StringProperty
    private val vehicles = mutableListOf<Vehicle>()

    constructor() : this("", "")
    constructor(id: String, name: String) {
        this.idProp = SimpleStringProperty(id)
        this.nameProp = SimpleStringProperty(name)
    }
    private constructor(idProp: StringProperty, nameProp: StringProperty) {
        this.idProp = idProp
        this.nameProp = nameProp
    }

    fun idProperty(): StringProperty = idProp
    fun nameProperty(): StringProperty = nameProp

    var id: String
        get() = idProp.get()
        set(v) = idProp.set(v)

    var name: String
        get() = nameProp.get()
        set(v) = nameProp.set(v)

    fun getVehicles(): List<Vehicle> = vehicles
    fun addVehicle(v: Vehicle) = vehicles.add(v)
    fun removeVehicle(v: Vehicle) = vehicles.remove(v)
    fun setVehicles(list: List<Vehicle>) {
        vehicles.clear()
        vehicles.addAll(list)
    }

    override fun toString() = buildString {
        append("Dealer ID: ").append(id).append("\n")
        append("Name: ").append(name).append("\n")
        append("Vehicles:\n")
        vehicles.forEach {
            append("  - ${it.make} ${it.model} (${it.type})\n")
        }
    }
}
