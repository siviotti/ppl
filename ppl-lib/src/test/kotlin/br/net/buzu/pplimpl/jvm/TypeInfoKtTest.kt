package br.net.buzu.pplimpl.jvm

import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TypeInfoKtTest {

    @Test
    fun testRead() {
        val typeInfo=read(Order::class.java, genericSkip)
        println(typeInfo.toTree(0))
    }
}