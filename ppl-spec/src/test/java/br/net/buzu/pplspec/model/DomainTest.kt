package br.net.buzu.pplspec.model

import br.net.buzu.pplspec.lang.Syntax
import org.junit.Assert.*
import org.junit.Test

/**
 * @author Douglas Siviotti
 * @since 1.0
 */
class DomainTest {

    @Test
    fun testAnonymousSimple() {
        val domain = domainOf(WHITE, BLACK, RED)
        assertEquals(3, domain.items().size.toLong())
        assertEquals(Syntax.EMPTY, domain.name())
        assertEquals(WHITE, domain.items()[0].value())
        assertEquals(BLACK, domain.items()[1].value())
        assertEquals(RED, domain.items()[2].value())
        assertEquals("[\"white\",\"black\",\"red\"]", domain.asSerial())
        assertTrue(domain.containsValue(WHITE))
        assertTrue(domain.containsValue(BLACK))
        assertTrue(domain.containsValue(RED))
        assertFalse(domain.containsValue(BLUE))
    }

    @Test
    fun testAnonymousLabel() {
        val domain = domainOf(_01_WHITE, _02_BLACK, _03_RED)
        assertEquals(3, domain.items().size.toLong())
        assertEquals(Syntax.EMPTY, domain.name())
        assertEquals(_01_WHITE, domain.items()[0].asSerial())
        assertEquals(_02_BLACK, domain.items()[1].asSerial())
        assertEquals(_03_RED, domain.items()[2].asSerial())
        assertEquals(WHITE, domain.items()[0].label())
        assertEquals(BLACK, domain.items()[1].label())
        assertEquals(RED, domain.items()[2].label())
        assertEquals("01", domain.items()[0].value())
        assertEquals("02", domain.items()[1].value())
        assertEquals("03", domain.items()[2].value())
        assertEquals(1, domain.items()[0].intValue().toLong())
        assertEquals(2, domain.items()[1].intValue().toLong())
        assertEquals(3, domain.items()[2].intValue().toLong())
        assertEquals("[\"01=white\",\"02=black\",\"03=red\"]", domain.asSerial())
        assertTrue(domain.containsValue("01"))
        assertTrue(domain.containsValue("02"))
        assertTrue(domain.containsValue("03"))
        assertFalse(domain.containsValue(WHITE))

    }

    @Test
    fun testEmptyOf() {
        val domain = domainOf()
        assertEquals(0, domain.items().size.toLong())
        assertEquals(Syntax.EMPTY, domain.name())
        assertEquals(Syntax.EMPTY, domain.asSerial())
        assertEquals(Syntax.EMPTY, domain.toString())
    }

    @Test
    fun testNamed() {
        val domain1 = Domain.create(DOMAIN_NAME1, Domain.list(WHITE, BLACK, RED))
        val clone = Domain.create(DOMAIN_NAME1, Domain.list(WHITE, BLACK, RED))
        val domain2 = Domain.create(DOMAIN_NAME2, Domain.list(WHITE, BLACK, RED))
        val domain3 = Domain.create(DOMAIN_NAME1, Domain.list(WHITE, BLACK))
        assertEquals(3, domain1.items().size.toLong())
        assertEquals(DOMAIN_NAME1, domain1.name())
        assertEquals(WHITE, domain1.items()[0].value())
        assertEquals(BLACK, domain1.items()[1].value())
        assertEquals(RED, domain1.items()[2].value())
        // Object
        assertEquals(domain1, clone)
        assertEquals(clone, domain1)
        assertFalse(domain1 == null)
        assertTrue(domain1 == domain1)
        //assertFalse(domain1 == "abc")
        assertTrue(domain1.hashCode() == clone.hashCode())

        assertFalse(domain1 == domain2)
        assertFalse(domain1.name() == domain2.name())
        assertTrue(domain1.name() == domain3.name())
        assertFalse(domain1 == domain3)
        assertFalse(domain1.hashCode() == domain2.hashCode())
        assertFalse(domain1.toString() == domain2.toString())
        assertTrue(domain1.equalsItems(domain2))
        assertFalse(domain1.equalsItems(domain3))

    }

    companion object {

        private val BLUE = "Blue"
        private val _01_WHITE = "01=white"
        private val _02_BLACK = "02=black"
        private val _03_RED = "03=red"
        private val DOMAIN_NAME1 = "domainName1"
        private val DOMAIN_NAME2 = "domainName2"
        private val WHITE = "white"
        private val BLACK = "black"
        private val RED = "red"
    }


}

