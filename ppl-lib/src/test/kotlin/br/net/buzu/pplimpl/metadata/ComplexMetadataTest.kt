package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.lang.EMPTY
import br.net.buzu.pplspec.model.*
import java.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test



/**
 * @author Douglas Siviotti
 */
class ComplexMetadataTest {

    @Test
    fun test() {
        val m1 = SimpleMetadataTest.createSample("atomic", Subtype.CHAR, 10, 0, 1)
        val m2 = SimpleMetadataTest.createSample("array", Subtype.CHAR, 10, 1, 2)
        val children = ArrayList<Metadata>()
        children.add(m1)
        children.add(m2)
        val c1 = createSample("complex", Subtype.OBJ, 0, 0, 0, 1, Domain.EMPTY, EMPTY, EMPTY, children)

        assertEquals(m1, c1.children<Metadata>()[0])
        assertEquals(m2, c1.children<Metadata>()[1])
        //assertEquals(30, c1.serialMaxSize()); // m1 = 10 m2 = 20

        // Basic Methods
        val c2 = createSample("complex", Subtype.OBJ, 0, 0, 0, 1, Domain.EMPTY, EMPTY, EMPTY, children)

        assertTrue(c1 == c2)
        assertTrue(c1.hashCode() == c2.hashCode())
        assertTrue(c1.toString() == c2.toString())
        assertTrue(c2 == c1)
        assertTrue(c1 == c1)
        assertFalse(c1 == null)
        val o = "abc"
        assertFalse(c1.equals(o))
    }

    companion object {

        fun createSample(name: String, subtype: Subtype, size: Int, scale: Int, minOccurs: Int,
                         maxOccurs: Int, domain: Domain, defaultValue: String, tags: String, children: List<Metadata>?): ComplexMetadata {
            val metaInfo = MetaInfo(31, name, subtype, size, 0, minOccurs, maxOccurs, domain, defaultValue, tags)
            return ComplexMetadata(metaInfo, children!!)

        }

        internal fun createSample(name: String, subtype: Subtype, size: Int, minOccurs: Int, maxOccurs: Int): ComplexMetadata {
            val domain = domainOf("white", "black", "red")
            return createSample(name, subtype, size, 0, minOccurs, maxOccurs, domain, "red", "XYZ", null)

        }
    }

}
