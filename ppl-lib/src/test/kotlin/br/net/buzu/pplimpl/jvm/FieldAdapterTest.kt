package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.util.asTree
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

internal class FieldAdapterTest {

    @Test
    fun testRead() {
        val fieldAdapter=readMetaType(Order::class.java, genericSkip)
        println(asTree(fieldAdapter, { it.children }))
    }
}