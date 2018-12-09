package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.PplString
import br.net.buzu.pplspec.model.pplStringOf
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Douglas Siviotti
 */
class BasicMetadataTest {

    @Test
    fun test() {
        val pplString = pplStringOf("(name:S10;age:I3;city:S10)")
        val bm1 = parse(pplString)
        val bm2 = parse(pplString)

        assertTrue(bm1 == bm2)
        assertTrue(bm2 == bm1)
        assertTrue(bm2.hashCode() == bm1.hashCode())
        assertTrue(bm2.toString() == bm1.toString())
        assertTrue(bm2.toTree(0) == bm1.toTree(0))
        assertTrue(bm1 == bm1)
        assertFalse(bm1 == null)
        val o = "abc"
        assertFalse(bm1.equals(o))
    }

}
