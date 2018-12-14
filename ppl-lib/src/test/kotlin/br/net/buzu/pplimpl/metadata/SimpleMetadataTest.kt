package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


/**
 * @author Douglas Siviotti
 */
class SimpleMetadataTest {

    @Test
    fun testAtomic() {
        val m1 = createSample("item", Subtype.CHAR, 10, 0, 1) as SimpleStaticMetadata

        assertEquals(Kind.ATOMIC, m1.kind())
        assertEquals(false, m1.info().isRequired)
        assertEquals(10, m1.serialMaxSize()) // 1 x 10
    }

    @Test
    fun testArray() {
        val m2 = createSample("list", Subtype.CHAR, 10, 1, 2) as SimpleStaticMetadata

        assertEquals(Kind.ARRAY, m2.kind())
        assertEquals(true, m2.info().isRequired)
        assertEquals(20, m2.serialMaxSize()) // 2 x 10
    }

    @Test
    fun testBasicMethods() {
        val m1 = createSample("item", Subtype.CHAR, 10, 0, 1)
        val m2 = createSample("item", Subtype.CHAR, 10, 0, 1)

        assertTrue(m1 == m2)
        assertTrue(m1.hashCode() == m2.hashCode())
        assertTrue(m1.toString() == m2.toString())
        assertTrue(m2 == m1)
        assertTrue(m1 == m1)
        assertFalse(m1 == null)
    }

    companion object {

        fun createSample(name: String, subtype: Subtype, size: Int, scale: Int, minOccurs: Int,
                         maxOccurs: Int, domain: Domain, defaultValue: String, tags: String): SimpleMetadata {
            val metaInfo = MetaInfo("", 31, name, subtype, size, 0, minOccurs, maxOccurs, domain, defaultValue, tags)
            return genericCreateMetadata (metaInfo, listOf()) as SimpleMetadata

        }

        internal fun createSample(name: String, subtype: Subtype, size: Int, minOccurs: Int, maxOccurs: Int): SimpleMetadata {
            val domain = domainOf("white", "black", "red")
            return createSample(name, subtype, size, 0, minOccurs, maxOccurs, domain, "red", "XYZ")

        }
    }

}
