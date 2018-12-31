package br.net.buzu.pplimpl

import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

internal class PplApiTest {

    @Test
    fun testSimpleFromPpl() {
        val order: Order = fromPpl(Order.PPL_STRING, Order::class.java) as Order
    }

    @Test
    fun testSimpleToPpl() {
        val ppl = toPpl(Order.INSTANCE)
    }

}