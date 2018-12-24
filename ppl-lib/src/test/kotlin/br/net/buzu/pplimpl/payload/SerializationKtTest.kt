package br.net.buzu.pplimpl.payload

import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.metadata.loadMetadata
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SerializationKtTest {

    @Test
    fun testSerializePayload() {
        val metaType = readMetaType(Order::class.java)
        val metadata = loadMetadata(Order.INSTANCE, metaType) as StaticMetadata
        val s2 = metaType.serialize(Order.INSTANCE, metadata)
        println(s2)
    }
}