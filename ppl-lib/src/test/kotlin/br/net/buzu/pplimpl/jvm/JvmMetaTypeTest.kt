package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.metadata.loadMetadata
import br.net.buzu.pplimpl.metadata.parseMetadata
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class JvmMetaTypeTest {

    @Test
    fun testNodeCount() {
    }

    @Test
    fun testParse(){
        val pplString = pplStringOf(Order.PPL_STRING)
        val metaType = readMetaType(Order::class.java)
        val metadata = parseMetadata(pplString) as StaticMetadata
        println(metadata.toTree(0))
        val order =  metaType.parse(pplString.payload, metadata) as Order
        println(order)
    }

    @Test
    fun testSerialize(){
        val metaType = readMetaType(Order::class.java)
        val metadata = loadMetadata(Order.INSTANCE, metaType) as StaticMetadata
        val s2 = metaType.serialize(Order.INSTANCE, metadata)
        println(s2)
    }
}