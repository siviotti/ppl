package br.net.buzu.pplimpl.jvm

import br.net.buzu.lib.asTree
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

internal class TypeInfoKtTest {

    @Test
    fun testRead() {
        val typeInfo=readTypeMapper(Order::class.java, genericSkip)
        println(asTree(typeInfo, {it.children}))
    }
}