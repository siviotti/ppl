package br.net.buzu.sample.order

class Product {

    var description: String? = null
    var price: Double = 0.toDouble()

    constructor() {

    }

    constructor(description: String, price: Double) : super() {
        this.description = description
        this.price = price
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (description == null) 0 else description!!.hashCode()
        val temp: Long
        temp = java.lang.Double.doubleToLongBits(price)
        result = prime * result + (temp xor temp.ushr(32)).toInt()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as Product?
        if (description == null) {
            if (other!!.description != null)
                return false
        } else if (description != other!!.description)
            return false
        return java.lang.Double.doubleToLongBits(price) == java.lang.Double.doubleToLongBits(other.price)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(description).append(" $").append(price)
        return sb.toString()
    }

}
