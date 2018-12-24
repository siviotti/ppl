package br.net.buzu.pplimpl.payload

import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.parseMetadata
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

internal class ParsingKtTest {

    @Test
    fun testPArsePayload(){
        val pplString = pplStringOf(Order.PPL_STRING)
        val metaType = readMetaType(Order::class.java)
        val metadata = parseMetadata(pplString) as StaticMetadata
        println(metadata.toTree(0))
        val order =  metaType.parse(pplString.payload, metadata) as Order

    }
}