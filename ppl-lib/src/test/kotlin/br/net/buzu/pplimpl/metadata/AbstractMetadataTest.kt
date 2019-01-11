package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Douglas Siviotti
 */
class AbstractMetadataTest {

    private fun createSample(name: String, subtype: Subtype, size: Int, minOccurs: Int, maxOccurs: Int): Metadata {
        val domain = domainOf("white", "black", "red")
        val metaInfo = MetaInfo(31, name, subtype, size, 0, minOccurs, maxOccurs, domain, "red", "XYZ")
        return GenericSimpleMetadata(Kind.ARRAY, metaInfo)

    }

    @Test
    fun test() {
        val domain = domainOf("Diamons", "white", "black", "red")
        val metaInfo = MetaInfo(31, "test", Subtype.STRING, 10, 0, 1, 2, domain, "red", "XYZ")

        val m1 = GenericSimpleMetadata(Kind.RECORD, metaInfo)

        assertEquals("test", m1.name())
        assertEquals(Subtype.STRING, m1.info().subtype)
        assertEquals(10, m1.info().size) // override MetaInfo
        assertEquals(0, m1.info().scale)
        assertEquals(1, m1.info().minOccurs)
        assertEquals(true, m1.info().isRequired)
        assertEquals(2, m1.info().maxOccurs) // override MetaInfo
    }

    @Test
    fun testEqualsHashcodeToString() {
        val m1 = createSample("test", Subtype.STRING, 10, 1, 5)
        val m2 = createSample("test", Subtype.STRING, 10, 1, 5)

        // assertEquals(m1, m2);
        assertTrue(m1 == m2)
        assertTrue(m1.hashCode() == m2.hashCode())
        assertTrue(m1.toString() == m2.toString())
        assertTrue(m2 == m1)
        assertTrue(m1 == m1)
        assertFalse(m1 == null)
    }

}

internal class GenericSimpleMetadata(kind: Kind, metaInfo: MetaInfo) : BasicMetadata(metaInfo) {

    override fun <T: Metadata> children(): List<T> {
        return listOf()
    }

    override fun hasChildren(): Boolean {
        // TODO Auto-generated method stub
        return false
    }


    override fun isStatic(): Boolean {
        return false
    }

}
