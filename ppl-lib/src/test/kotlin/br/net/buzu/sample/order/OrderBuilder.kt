package br.net.buzu.sample.order

import java.time.LocalDate
import java.util.ArrayList
import java.util.Arrays

/**
 * Test Data Builder for Order.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class OrderBuilder {

    var number: String? = null
        private set
    var customer: Customer? = null
        private set
    var date: LocalDate? = null
        private set
    private val products = ArrayList<Product>()
    var status: Status? = null
        private set
    var isCanceled: Boolean = false
        private set

    fun build(): Order {
        return Order(number, customer, date, products, status, isCanceled)
    }

    fun number(number: String): OrderBuilder {
        this.number = number
        return this
    }

    fun customer(customer: Customer): OrderBuilder {
        this.customer = customer
        return this
    }

    fun customer(name: String, vararg phones: String): OrderBuilder {
        this.customer = Customer(name, mutableListOf(), Arrays.asList(*phones))
        return this
    }

    fun billing(street: String, city: String, zip: String): OrderBuilder {
        if (customer == null) {
            throw IllegalArgumentException("customer is null. call 'customer' method")
        }
        this.customer!!.addAddress(Address(street, city, zip, AddressType.BILLING))
        return this
    }

    fun delivery(street: String, city: String, zip: String): OrderBuilder {
        if (customer == null) {
            throw IllegalArgumentException("customer is null. call 'customer' method")
        }
        this.customer!!.addAddress(Address(street, city, zip, AddressType.DELIVERY))
        return this
    }

    fun date(date: LocalDate): OrderBuilder {
        this.date = date
        return this
    }

    fun getProducts(): List<Product> {
        return products
    }

    fun products(products: List<Product>): OrderBuilder {
        this.products.addAll(products)
        return this
    }

    fun product(description: String, price: Double): OrderBuilder {
        products.add(Product(description, price))
        return this
    }

    fun status(status: Status): OrderBuilder {
        this.status = status
        return this
    }

    fun canceled(canceled: Boolean): OrderBuilder {
        this.isCanceled = canceled
        return this
    }
}
