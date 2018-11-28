package br.net.buzu.pplspec.model

import br.net.buzu.pplspec.exception.PplParseException
import br.net.buzu.pplspec.lang.Syntax
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

/**
 * PplString unit test
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class PplStringTest {

    @Ignore // impossible assign (null)
    @Test(expected = IllegalArgumentException::class)
    fun testNull() {
        //PplString(null)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEmpty() {
        PplString("")
    }

    @Test(expected = PplParseException::class)
    fun testOpenSub() {
        PplString("(")
    }

    @Ignore // impossilble assign (null)
    @Test(expected = IllegalArgumentException::class)
    fun testNullContext() {
        //PplString("(zzz)abc", null)
    }

    @Test(expected = PplParseException::class)
    fun testNoMetadata() {
        PplString("Ladybug   015Paris")
    }

    @Test
    fun testEmptyMetadata() {
        val syntax = Syntax()
        val pplString = PplString("()Ladybug   015Paris", syntax)
        assertEquals("()", pplString.metadata)
        assertEquals("Ladybug   015Paris", pplString.payload)
        assertEquals(syntax, pplString.syntax)
    }

    @Test
    fun testInvalidMetadata() {
        val pplString = PplString("(%&?)Ladybug   015Paris")
        assertEquals("(%&?)", pplString.metadata)
        assertEquals("Ladybug   015Paris", pplString.payload)
    }

    @Test(expected = PplParseException::class)
    fun testUnclosedMetadata() {
        PplString("(name: S10; age: I3; city:S5 Ladybug   015Paris")
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
        assertEquals(Syntax::class.java, pplString1.syntax.javaClass)

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
