package br.net.buzu.pplimpl.jvm

import br.net.buzu.lang.EMPTY
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Subtype
import br.net.buzu.model.ValueMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class JvmValueMapperTest {

    val metaInfo = MetaInfo(0, EMPTY, Subtype.STRING, 0, 0, 0, 0)

    @Test
    fun testCharMapper() {
        assertEquals("123", CharMapper.toValue("123", metaInfo))
        assertEquals(" 123", CharMapper.toValue(" 123", metaInfo))
        assertEquals("123 ", CharMapper.toValue("123 ", metaInfo))
        assertEquals(" 123 ", CharMapper.toValue(" 123 ", metaInfo))
        assertEquals("", CharMapper.toValue(" 123 ", metaInfo))

        assertEquals("123", CharMapper.toText("123"))
        assertEquals(" 123", CharMapper.toText(" 123"))
        assertEquals("123 ", CharMapper.toText("123 "))
        assertEquals(" 123 ", CharMapper.toText(" 123 "))
    }

    @Test
    fun testStringMapper() {
        assertEquals("123", StringMapper.toValue("123", metaInfo))
        assertEquals("123", StringMapper.toValue(" 123", metaInfo))
        assertEquals("123", StringMapper.toValue("123 ", metaInfo))
        assertEquals("123", StringMapper.toValue(" 123 ", metaInfo))
        assertEquals("123", StringMapper.toText("123"))
        assertEquals(" 123", StringMapper.toText(" 123"))
        assertEquals("123 ", StringMapper.toText("123 "))
        assertEquals(" 123 ", StringMapper.toText(" 123 "))
    }

    @Test
    fun testNumberMapper() {

    }

    @Test
    fun testIntegerMapper() {
        assertEquals(123, IntegerMapper.toValue("123", metaInfo))
        assertEquals("123", IntegerMapper.toText(123))
    }

    @Test
    fun testLongMapper() {
        assertEquals(123L, LongMapper.toValue("123", metaInfo))
        assertEquals("123", LongMapper.toText(123L))
    }

}