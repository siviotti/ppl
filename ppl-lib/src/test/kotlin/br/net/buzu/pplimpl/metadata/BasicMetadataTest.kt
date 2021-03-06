package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.model.pplStringOf
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * @author Douglas Siviotti
 */
class BasicMetadataTest {

    @Test
    fun test() {
        val pplString = pplStringOf("(name:S10;age:I3;city:S10)")
        val bm1 = parseMetadata(pplString)
        val bm2 = parseMetadata(pplString)

        assertTrue(bm1 == bm2)
        assertTrue(bm2 == bm1)
        assertTrue(bm2.hashCode() == bm1.hashCode())
        assertTrue(bm2.toString() == bm1.toString())
        assertTrue(bm1 == bm1)
        assertFalse(bm1 == null)
        val o = "abc"
        assertFalse(bm1.equals(o))
    }

}
