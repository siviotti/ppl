package br.net.buzu.sample.order

class Address {

    var street: String? = null
    var city: String? = null
    var zip: String? = null
    var type: AddressType? = null

    constructor() {

    }

    @JvmOverloads
    constructor(street: String, city: String, zip: String, type: AddressType? = null) : super() {
        this.street = street
        this.city = city
        this.zip = zip
        this.type = type
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (city == null) 0 else city!!.hashCode()
        result = prime * result + if (street == null) 0 else street!!.hashCode()
        result = prime * result + if (type == null) 0 else type!!.hashCode()
        result = prime * result + if (zip == null) 0 else zip!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as Address?
        if (city == null) {
            if (other!!.city != null)
                return false
        } else if (city != other!!.city)
            return false
        if (street == null) {
            if (other.street != null)
                return false
        } else if (street != other.street)
            return false
        if (type != other.type)
            return false
        return if (zip == null) {
            other.zip == null
        } else
            zip == other.zip
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(street).append("-").append(city).append("-").append(zip).append(":").append(type)
        return sb.toString()
    }

}
