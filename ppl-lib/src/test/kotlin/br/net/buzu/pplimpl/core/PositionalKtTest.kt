package br.net.buzu.pplimpl.core

import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.loadMetadata
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


internal class PositionalKtTest {

    @Test
    fun testPositionalParse() {
        val metaType = readMetaType(Order::class.java)
        val pplString = pplStringOf(Order.PPL_STRING)
        val metadata = loadMetadata(Order.INSTANCE, metaType) as StaticMetadata
        val order: Order = positionalParse(pplString.payload, metadata, metaType) as Order
        assertEquals(Order.INSTANCE, order)

    }

    @Test
    fun testPositionalSerialize(){
        val metaType = readMetaType(Order::class.java)
        val pplString = pplStringOf(Order.PPL_STRING)
        val metadata = loadMetadata(Order.INSTANCE, metaType) as StaticMetadata
        val ppl: String = positionalSerialize(Order.INSTANCE, metadata, metaType)
        assertEquals(pplString.payload, ppl)
    }
}