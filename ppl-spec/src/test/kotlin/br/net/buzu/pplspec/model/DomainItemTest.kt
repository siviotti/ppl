package br.net.buzu.pplspec.model

import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class DomainItemTest {

    @Test
    @Ignore // Kotlin not null
    fun testNull() {
        val item = domainItemOf(null!!)
        assertEquals(null, item)
    }

    @Test
    fun testBlank() {
        val item = domainItemOf("")
        assertEquals("", item.value())
        assertEquals(null, item.label())
    }

    @Test
    fun testValueLabel() {
        val item = domainItemOf("01=abc")
        assertEquals("01", item.value())
        assertEquals("abc", item.label())
    }

    @Test
    fun testOnlyValue() {
        val item = domainItemOf("345")
        assertEquals("345", item.value())
        assertEquals(null, item.label())
    }

    @Test(expected = NullPointerException::class)
    @Ignore // Kotlin not null
    fun testNullPointerException() {
        //DomainItem(null)
    }

    @Test
    fun testObjectMethods() {
        val item1 = domainItemOf("01=abc")
        val item2 = domainItemOf("01=abc")
        val item3 = domainItemOf("01=xyz")
        val item4 = domainItemOf("02=xyz")
        assertEquals(item1, item2)
        assertEquals(item2, item1)
        assertFalse(item1 == null)
        //assertFalse(item1 == "abc")
        assertTrue(item1 == item1)
        assertEquals(item1.hashCode().toLong(), item2.hashCode().toLong())
        assertEquals(item1.toString(), item2.toString())
        assertFalse(item1 == item3)
        assertFalse(item1 == item4)

        assertEquals(0, item1.compareTo(item2).toLong())
        assertEquals(0, item1.compareTo(item3).toLong())
        assertEquals(-1, item1.compareTo(item4).toLong())
        assertEquals(1, item4.compareTo(item1).toLong())
    }

}
