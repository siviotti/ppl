package br.net.buzu.pplimpl.payload

import br.net.buzu.java.model.Metadata
import br.net.buzu.java.model.StaticMetadata
import br.net.buzu.java.model.pplStringOf
import br.net.buzu.pplimpl.jvm.readFieldAdapter
import br.net.buzu.pplimpl.metadata.parseMetadata
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ParsingKtTest {

    @Test
    fun testPArsePayload(){
        val pplString = pplStringOf(Order.PPL_STRING)
        val fieldAdapter = readFieldAdapter(Order::class.java)
        val metadata: StaticMetadata = parseMetadata(pplString) as StaticMetadata
        println(metadata.toTree(0))
        parsePayload(pplString.payload, metadata, fieldAdapter)
    }
}