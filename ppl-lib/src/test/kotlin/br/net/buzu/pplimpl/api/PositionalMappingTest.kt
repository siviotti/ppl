/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
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
            //parseByApi(pplString.payload, metadata, metaType)
            //parseByMapper(pplString.payload, metadata, metaType)
            //parseByCreatedMapper(pplString.payload, mapper)
        }
        val t1 = System.currentTimeMillis()

        println (" time: ${(t1-t0)}")
    }

    fun parseByApi(payload: String, metadata: StaticMetadata, metaType: MetaType) {
        positionalParse(payload, metadata, metaType)
    }


    fun parseByMapper(payload: String, metadata: StaticMetadata, metaType: MetaType) {
        positionalMapperOf(metadata, metaType).parse(payload)
    }

    fun parseByCreatedMapper(payload: String, positionalMapper: PositionalMapper) {
        positionalMapper.parse(payload)
    }

}