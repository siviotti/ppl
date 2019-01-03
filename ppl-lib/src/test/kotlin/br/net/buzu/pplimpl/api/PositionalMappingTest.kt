package br.net.buzu.pplimpl.api

import br.net.buzu.api.PositionalMapper
import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.core.positionalParse
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.parseMetadata
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled

internal class PositionalMappingTest {

    @Test
    fun testMapperRoundTrip(){
        val pplString = pplStringOf(Order.PPL_STRING)
        val metaType = readMetaType(Order::class.java)
        val metadata = parseMetadata(pplString) as StaticMetadata
        val mapper= positionalMapperOf(metadata, metaType)
        // parsing
        val order =  mapper.parse(pplString.payload) as Order
        assertEquals(Order.INSTANCE, order)
        // serialization
        val payload = mapper.serialize(order)
        assertEquals(pplString.payload, payload)
    }

    @Disabled
    @Test
    fun testPerformance(){
        val pplString = pplStringOf(Order.PPL_STRING)
        val metaType = readMetaType(Order::class.java)
        val metadata = parseMetadata(pplString) as StaticMetadata
        val mapper = positionalMapperOf(metadata, metaType)

        val t0 = System.currentTimeMillis()
        for (i in 1..1000000){
            parseByApi(pplString.payload, metadata, metaType)
            //parseByMetaType(pplString.payload, metadata, metaType)
            //parseByMapper(pplString.payload, metadata, metaType)
            //parseByCreatedMapper(pplString.payload, mapper)
        }
        val t1 = System.currentTimeMillis()

        println (" time: ${(t1-t0)}")
    }

    fun parseByApi(payload: String, metadata: StaticMetadata, metaType: MetaType) {
        positionalParse(payload, metadata, metaType)
    }

    fun parseByMetaType(payload: String, metadata: StaticMetadata, metaType: MetaType) {
        metaType.parse(payload, metadata)
    }

    fun parseByMapper(payload: String, metadata: StaticMetadata, metaType: MetaType) {
        positionalMapperOf(metadata, metaType).parse(payload)
    }

    fun parseByCreatedMapper(payload: String, positionalMapper: PositionalMapper) {
        positionalMapper.parse(payload)
    }

}