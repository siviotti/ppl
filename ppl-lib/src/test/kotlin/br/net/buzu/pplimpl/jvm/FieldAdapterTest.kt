package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.util.asTree
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

internal class FieldAdapterTest {

    @Test
    fun testRead() {
        val metaType=readMetaType(Order::class.java)
        println(asTree(metaType, { it.children }))
    }
}