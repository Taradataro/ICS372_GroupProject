package org.example.hellofx

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

class Vehicle {
        private val idProp: StringProperty
        private val typeProp: StringProperty
        private val makeProp: StringProperty
        private val modelProp: StringProperty
        private val priceProp: StringProperty
        private var status: String

        constructor() : this("", "", "", "", "")
        constructor(
                type: String,
                id: String,
                price: String,
                make: String,
                model: String
        ) {
                this.typeProp = SimpleStringProperty(type)
                this.idProp   = SimpleStringProperty(id)
                this.priceProp= SimpleStringProperty(price)
                this.makeProp = SimpleStringProperty(make)
                this.modelProp= SimpleStringProperty(model)
                this.status   = "Available"
        }
        private constructor(
                typeProp: StringProperty,
                idProp:   StringProperty,
                priceProp:StringProperty,
                makeProp: StringProperty,
                modelProp:StringProperty,
                status:   String
        ) {
                this.typeProp = typeProp
                this.idProp   = idProp
                this.priceProp= priceProp
                this.makeProp = makeProp
                this.modelProp= modelProp
                this.status   = status
        }

        fun idProperty(): StringProperty    = idProp
        fun typeProperty(): StringProperty  = typeProp
        fun makeProperty(): StringProperty  = makeProp
        fun modelProperty(): StringProperty = modelProp
        fun priceProperty(): StringProperty = priceProp

        var id:    String
                get() = idProp.get()
                set(v) = idProp.set(v)
        var type:  String
                get() = typeProp.get()
                set(v) = typeProp.set(v)
        var make:  String
                get() = makeProp.get()
                set(v) = makeProp.set(v)
        var model: String
                get() = modelProp.get()
                set(v) = modelProp.set(v)
        var price: String
                get() = priceProp.get()
                set(v) = priceProp.set(v)

        fun getStatus(): String = status
        fun setStatus(s: String) { status = s }

        override fun toString() =
                "Vehicle(id=$id, type=$type, make=$make, model=$model, price=$price, status=$status)"
}
