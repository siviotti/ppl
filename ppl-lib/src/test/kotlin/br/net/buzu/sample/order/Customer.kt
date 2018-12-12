package br.net.buzu.sample.order

import java.util.ArrayList

class Customer {

    var name: String? = null
    private var addresses: MutableList<Address>? = ArrayList()
    var phones: List<String>? = null

    constructor(name: String, addresses: MutableList<Address>, phones: List<String>) : super() {
        this.name = name
        this.addresses = addresses
        this.phones = phones
    }

    constructor() {}

    fun getAddresses(): List<Address>? {
        return addresses
    }

    fun setAddresses(addresses: MutableList<Address>) {
        this.addresses = addresses
    }

    fun addAddress(address: Address) {
        this.addresses!!.add(address)
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (addresses == null) 0 else addresses!!.hashCode()
        result = prime * result + if (name == null) 0 else name!!.hashCode()
        result = prime * result + if (phones == null) 0 else phones!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as Customer?
        if (addresses == null) {
            if (other!!.addresses != null)
                return false
        } else if (addresses != other!!.addresses)
            return false
        if (name == null) {
            if (other.name != null)
                return false
        } else if (name != other.name)
            return false
        return if (phones == null) {
            other.phones == null
        } else
            phones == other.phones
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(" name=").append(name).append("\n")
        sb.append(" addresses=").append(addresses).append("\n")
        sb.append(" phones=").append(phones)
        return sb.toString()
    }

}
