package br.net.buzu.sample.order

import java.time.LocalDate
import java.util.ArrayList

/**
 * Sample complex object
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class Order {

    var number: String? = null
    var customer: Customer? = null
    var date: LocalDate? = null
    var products: List<Product>? = ArrayList()
    var status: Status? = null
    var isCanceled: Boolean = false

    constructor()

    constructor(number: String?, customer: Customer?, date: LocalDate?, products: List<Product>?, status: Status?,
                canceled: Boolean = false) : super() {
        this.number = number
        this.customer = customer
        this.date = date
        this.products = products
        this.status = status
        this.isCanceled = canceled
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (isCanceled) 1231 else 1237
        result = prime * result + if (customer == null) 0 else customer!!.hashCode()
        result = prime * result + if (date == null) 0 else date!!.hashCode()
        result = prime * result + if (number == null) 0 else number!!.hashCode()
        result = prime * result + if (products == null) 0 else products!!.hashCode()
        result = prime * result + if (status == null) 0 else status!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as Order?
        if (isCanceled != other!!.isCanceled)
            return false
        if (customer == null) {
            if (other.customer != null)
                return false
        } else if (customer != other.customer)
            return false
        if (date == null) {
            if (other.date != null)
                return false
        } else if (date != other.date)
            return false
        if (number == null) {
            if (other.number != null)
                return false
        } else if (number != other.number)
            return false
        if (products == null) {
            if (other.products != null)
                return false
        } else if (products != other.products)
            return false
        return status == other.status
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("number=").append(number).append("\n")
        sb.append("customer\n").append(customer).append("\n")
        sb.append("date=").append(date).append("\n")
        sb.append("products=").append(products).append("\n")
        sb.append("status=").append(status).append("\n")
        sb.append("canceled=").append(isCanceled)
        return sb.toString()
    }


}
