package br.net.buzu.pplimpl.jvm

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TypeTreeKtTest {

    @Test
    fun testGetValueSize() {
        assertEquals(20, getValueSize("12345678901234567890"))
        assertEquals(2, getValueSize(30))
        assertEquals(4, getValueSize(true))
        assertEquals(5, getValueSize(false))
    }

}