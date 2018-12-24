package br.net.buzu.pplimpl.payload

import br.net.buzu.java.model.StaticMetadata
import br.net.buzu.java.model.pplStringOf
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.parseMetadata
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

internal class ParsingKtTest {

    @Test
    fun testPArsePayload(){
        val pplString = pplStringOf(Order.PPL_STRING)
        val fieldAdapter = readMetaType(Order::class.java)
        val metadata: StaticMetadata = parseMetadata(pplString) as StaticMetadata
        println(metadata.toTree(0))
        parsePayload(pplString.payload, metadata, fieldAdapter)
    }
}