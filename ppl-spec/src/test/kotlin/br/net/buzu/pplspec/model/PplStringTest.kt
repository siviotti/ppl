package br.net.buzu.pplspec.model

import br.net.buzu.pplspec.exception.PplParseException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * PplString unit test
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class PplStringTest {

    @Test
    fun testEmpty() {
        assertThrows(IllegalArgumentException::class.java){
            PplString("")
        }
    }

    @Test
    fun testOpenSub() {
        assertThrows(PplParseException::class.java){
            PplString("(")
        }
    }

    @Test
    fun testNoMetadata() {
        assertThrows(PplParseException::class.java){
            PplString("Ladybug   015Paris")
        }
    }

    @Test
    fun testEmptyMetadata() {
        val pplString = PplString("()Ladybug   015Paris")
        assertEquals("()", pplString.metadata)
        assertEquals("Ladybug   015Paris", pplString.payload)
    }

    @Test
    fun testInvalidMetadata() {
        val pplString = PplString("(%&?)Ladybug   015Paris")
        assertEquals("(%&?)", pplString.metadata)
        assertEquals("Ladybug   015Paris", pplString.payload)
    }

    @Test
    fun testUnclosedMetadata() {
        assertThrows(PplParseException::class.java){
            PplString("(name: S10; age: I3; city:S5 Ladybug   015Paris")
        }
    }

    @Test
    fun testEmptyPayload() {
        val pplString = PplString("(name: S10; age: I3; city:S5)")
        assertEquals("(name: S10; age: I3; city:S5)", pplString.metadata)
        assertEquals("", pplString.payload)
    }

    @Test
    fun test() {
        val pplString1 = PplString("(name: S10; age: I3; city:S5)Ladybug   015Paris")
        assertEquals("(name: S10; age: I3; city:S5)", pplString1.metadata)
        assertEquals("Ladybug   015Paris", pplString1.payload)

        val pplString2 = PplString("(name: S10; age: I3; city:S5)Catnoir   015Paris")
        assertFalse(pplString1 == pplString2)
        val pplString3 = PplString("(name: S7; age: I3; city:S5)Ladybug015Paris")
        assertFalse(pplString1 == pplString3)
    }

    @Test
    fun testEqualsHashodeToString() {
        val pplString1 = PplString("(name: S10; age: I3; city:S5)Ladybug   015Paris")
        val pplString2 = PplString("(name: S10; age: I3; city:S5)Ladybug   015Paris")
        assertFalse(pplString1 == null)
        assertTrue(pplString1 == pplString1)
        assertEquals(pplString1, pplString2)
        assertEquals(pplString1.hashCode().toLong(), pplString2.hashCode().toLong())
        assertEquals(pplString1.toString(), pplString2.toString())
        assertEquals("(name: S10; age: I3; city:S5)Ladybug   015Paris", pplString1.toString())
    }


}
