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
    var canceled: Boolean = false

    constructor()

    constructor(number: String?, customer: Customer?, date: LocalDate?, products: List<Product>?, status: Status?,
                canceled: Boolean = false) : super() {
        this.number = number
        this.customer = customer
        this.date = date
        this.products = products
        this.status = status
        this.canceled = canceled
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (canceled) 1231 else 1237
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
        if (canceled != other!!.canceled)
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
        sb.append("canceled=").append(canceled)
        return sb.toString()
    }

    companion object {

        val BILING = Address("Champs Elysee 10", "Paris", "75008", AddressType.BILLING)
        val DELIVERY = Address("Baker Street 221b", "London", "NW1 6XE", AddressType.DELIVERY)
        val ADDRESSES: MutableList<Address> = mutableListOf(BILING, DELIVERY)
        val PHONE1 = "11111111"
        val PHONE2 = "22222222"
        val PHONES: MutableList<String> = mutableListOf(PHONE1, PHONE2)
        val CUSTOMER_NAME = "Ladybug"
        val CUSTOMER: Customer = Customer(CUSTOMER_NAME, ADDRESSES, PHONES)
        // Products

        val PRODUCT1 = Product("Book", 45.99)
        val PRODUCT2 = Product("Notebook", 1200.00)
        val PRODUCT3 = Product("Clock", 25.52)
        val PRODUCT4 = Product("Software", 0.99)
        val PRODUCT5 = Product("Tablet", 500.00)
        val PRODUCTS: MutableList<Product> = mutableListOf(PRODUCT1, PRODUCT2, PRODUCT3, PRODUCT4, PRODUCT5)
        // Order
        val ORDER_NUMBER = "1234567890"
        val ORDER_DATE = LocalDate.of(2017, 11, 30)
        val ORDER_STATUS = Status.OPENED
        val ORDER_CANCELED = false
        val INSTANCE: Order = Order(ORDER_NUMBER, CUSTOMER, ORDER_DATE, PRODUCTS, ORDER_STATUS, ORDER_CANCELED)
        val PPL_STRING = "(number:S10;customer:(name:S7;addresses:(street:S17;city:S6;zip:S7;jvmType:S8)#0-2;phones:S8#0-2);date:D;products:(description:S8;price:N6)#0-5;status:S6;canceled:B)1234567890LadybugChamps Elysee 10 Paris 75008  BILLING Baker Street 221bLondonNW1 6XEDELIVERY111111112222222220171130Book    045.99Notebook1200.0Clock   025.52Software000.99Tablet  0500.0OPENEDfalse"


    }
}
