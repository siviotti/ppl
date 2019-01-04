package br.net.buzu.pplimpl.api

import br.net.buzu.model.Dialect
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BasicPplMapperTest {

    private val mapper = BasicPplMapper(Dialect.DEFAULT)

    @Test
    fun testFromPpl() {
        assertEquals(Order.INSTANCE, mapper.fromPpl(Order.PPL_STRING, Order::class.java))
    }

    @Test
    fun testToPpl() {
        assertEquals(Order.PPL_STRING, mapper.toPpl(Order.INSTANCE))
    }
}