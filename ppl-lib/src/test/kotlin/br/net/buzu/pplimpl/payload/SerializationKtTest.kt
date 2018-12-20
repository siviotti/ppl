package br.net.buzu.pplimpl.payload

import br.net.buzu.java.model.StaticMetadata
import br.net.buzu.java.model.pplStringOf
import br.net.buzu.pplimpl.jvm.loadMetadata
import br.net.buzu.pplimpl.jvm.readFieldAdapter
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SerializationKtTest {

    @Test
    fun testSerializePayload() {
        val fieldAdapter = readFieldAdapter(Order::class.java)
        val metadata = loadMetadata(Order.INSTANCE, fieldAdapter)
        val t0 = System.currentTimeMillis()
        for (i in 0..100000){
            serializePayload(Order.INSTANCE, metadata as StaticMetadata, fieldAdapter)
        }
        val t1 = System.currentTimeMillis()
        println ("time: " + (t1-t0))
        val s = serializePayload(Order.INSTANCE, metadata as StaticMetadata, fieldAdapter)
        println(s)
        val pplString = pplStringOf(Order.PPL_STRING)
        assertEquals(pplString.payload, s)
    }
}