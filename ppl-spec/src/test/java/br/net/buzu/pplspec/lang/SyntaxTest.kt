package br.net.buzu.pplspec.lang

import org.junit.Assert.*
import org.junit.Test
import java.text.ParseException

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class SyntaxTest {

    private val syntax= Syntax()

    @Test
    fun testIgnoreChar() {
        assertTrue(syntax!!.isIgnored(' '))
        assertTrue(syntax!!.isIgnored('\n'))
        assertTrue(syntax!!.isIgnored('\t'))

        assertFalse(syntax!!.isIgnored('A'))
        assertFalse(syntax!!.isIgnored('_'))
        assertFalse(syntax!!.isIgnored('2'))
    }

    @Test
    fun testValidFirstCharMetaName() {
        assertTrue(syntax!!.isValidFirstCharMetaName('A'))
        assertTrue(syntax!!.isValidFirstCharMetaName('a'))
        assertTrue(syntax!!.isValidFirstCharMetaName('_'))

        assertFalse(syntax!!.isValidFirstCharMetaName('-'))
        assertFalse(syntax!!.isValidFirstCharMetaName('2'))

    }

    @Test
    fun testValidCharMetaName() {
        assertTrue(syntax!!.isValidCharMetaName('A'))
        assertTrue(syntax!!.isValidCharMetaName('a'))
        assertTrue(syntax!!.isValidCharMetaName('_'))
        assertTrue(syntax!!.isValidCharMetaName('-'))
        assertTrue(syntax!!.isValidCharMetaName('-'))
        assertTrue(syntax!!.isValidCharMetaName('2'))

        assertFalse(syntax!!.isValidCharMetaName('+'))
        assertFalse(syntax!!.isValidCharMetaName('|'))

    }

    @Test
    fun testValidMetaName() {
        assertTrue(syntax!!.isValidMetaName("abc"))
        assertTrue(syntax!!.isValidMetaName("camelCase"))
        assertTrue(syntax!!.isValidMetaName("CAPS_CONST"))
        assertTrue(syntax!!.isValidMetaName("name-ok-20_X"))

        assertFalse(syntax!!.isValidMetaName(null))
        assertFalse(syntax!!.isValidMetaName(""))
        assertFalse(syntax!!.isValidMetaName("2abc"))
        assertFalse(syntax!!.isValidMetaName("abc&def"))
    }

    @Test
    fun testPerformance() {
        val name = "name-test_ABC-25"
        val repeat = 1000000
        for (i in 0 until repeat) {
            Syntax.NAME_PATTERN.matcher(name).matches()
        }
        for (i in 0 until repeat) {
            syntax!!.isValidMetaName(name)
        }
    }

    @Test
    fun testRegex() {
        // Name
        assertTrue(Syntax.NAME_PATTERN.matcher("abc:").matches())
        assertTrue(Syntax.NAME_PATTERN.matcher("_abc:").matches())
        assertTrue(Syntax.NAME_PATTERN.matcher("abcXpto25:").matches())
        assertTrue(Syntax.NAME_PATTERN.matcher("Abc-Xpto_2:").matches())

        assertFalse(Syntax.NAME_PATTERN.matcher("").matches())
        assertFalse(Syntax.NAME_PATTERN.matcher(":").matches())
        assertFalse(Syntax.NAME_PATTERN.matcher("abc").matches())
        assertFalse(Syntax.NAME_PATTERN.matcher("2abcXpto25:").matches())
        assertFalse(Syntax.NAME_PATTERN.matcher("-abcXpto25:").matches())
        assertFalse(Syntax.NAME_PATTERN.matcher("abc def:").matches())

        // Type
        assertTrue(Syntax.TYPE_PATTERN.matcher("C").matches())
        assertTrue(Syntax.TYPE_PATTERN.matcher("String").matches())
        assertTrue(Syntax.TYPE_PATTERN.matcher("STR(etc * ç #2)").matches())

        assertFalse(Syntax.TYPE_PATTERN.matcher("").matches())
        assertFalse(Syntax.TYPE_PATTERN.matcher(" C").matches())
        assertFalse(Syntax.TYPE_PATTERN.matcher("VARCHAR2").matches())
        assertFalse(Syntax.TYPE_PATTERN.matcher("STR(etc * ç #2").matches())

        // Size
        assertTrue(Syntax.SIZE_PATTERN.matcher("25").matches())
        assertTrue(Syntax.SIZE_PATTERN.matcher("10,2").matches())

        assertFalse(Syntax.SIZE_PATTERN.matcher("").matches())
        assertFalse(Syntax.SIZE_PATTERN.matcher("25 ").matches())
        assertFalse(Syntax.SIZE_PATTERN.matcher(" 25").matches())
        assertFalse(Syntax.SIZE_PATTERN.matcher("10.2").matches())
        assertFalse(Syntax.SIZE_PATTERN.matcher("10,2,2").matches())

        // Occurs
        assertTrue(Syntax.OCCURS_PATTERN.matcher("25").matches())
        assertTrue(Syntax.OCCURS_PATTERN.matcher("2-10").matches())

        assertFalse(Syntax.OCCURS_PATTERN.matcher("").matches())
        assertFalse(Syntax.OCCURS_PATTERN.matcher("25 ").matches())
        assertFalse(Syntax.OCCURS_PATTERN.matcher(" 25").matches())
        assertFalse(Syntax.OCCURS_PATTERN.matcher("2,10").matches())
        assertFalse(Syntax.OCCURS_PATTERN.matcher("2:10").matches())

        // Meta
        assertTrue(Syntax.META_PATTERN.matcher(";").matches())
        assertTrue(Syntax.META_PATTERN.matcher("phone:S9#0-2;").matches())
        assertTrue(Syntax.META_PATTERN.matcher("birthday:D{yyyy-mm-dd};").matches())

    }

    @Test
    @Throws(ParseException::class)
    fun testEndIndex() {
        assertEquals(3, syntax!!.blockEndIndex("a{b}c", 1, '{', '}').toLong())
        assertEquals(28, syntax!!.blockEndIndex("a( teste ( str ' xpto ) 'b)c) abc", 1, '(', ')').toLong())
        assertEquals(28, syntax!!.blockEndIndex("a( teste ( str \" xpto ) \"b)c) abc", 1, '(', ')').toLong())
        assertEquals(24, syntax!!.blockEndIndex("test(inside')'(go)\"x'y\"z)out", 4, '(', ')').toLong())
        // 1 2 X 2 1

        try {
            assertEquals(3, syntax!!.blockEndIndex("a{b}c", 1, '(', ')').toLong())
            fail() // ( no lugar de {
        } catch (e: ParseException) {
            val message = e?.message ?: ""
            assertTrue(message.startsWith(Syntax.BLOCK_ERROR_INVALID_OPEN_CHAR))
        }

        assertEquals(12, syntax!!.blockEndIndex("xyz (abc(JJ)) def", 4, '(', ')').toLong())
        assertEquals(12, syntax!!.blockEndIndex("2[34[4[5]ax]]", 1, '[', ']').toLong())

        try {
            syntax!!.blockEndIndex("2[34[4[5]ax]", 1, '[', ']')
            fail() // abrem 3 e fecham 2
        } catch (e: ParseException) {
            val message = e?.message ?: ""
            assertTrue(message.startsWith(Syntax.BLOCK_ERROR))
        }

        try {
            syntax!!.blockEndIndex("2[34[4[5]ax]", 1, '[', ']')
            fail() // abrem 3 e fecham 2
        } catch (e: ParseException) {
            val message = e?.message ?: ""
            assertTrue(message.startsWith(Syntax.BLOCK_ERROR))
        }

        try {
            syntax!!.blockEndIndex("abc ( 'def) xyz", 4, '(', ')')
            fail() // opened String
        } catch (e: ParseException) {
            val message = e?.message ?: ""
            assertTrue(message.startsWith(Syntax.UNTERMINATED_STRING))
        }

    }

    @Test
    @Throws(ParseException::class)
    fun testStringEndIndex() {
        assertEquals(5, syntax!!.nextStringDelimeter("'1234'", 0, '\'').toLong())
        assertEquals(8, syntax!!.nextStringDelimeter("test 'ok' test", 5, '\'').toLong())
        assertEquals(16, syntax!!.nextStringDelimeter("test 'ok \"inner\"' test", 5, '\'').toLong())
    }

    @Test
    @Throws(ParseException::class)
    fun testStringExtract() {
        assertEquals("'1234'", syntax!!.extractString("'1234'", 0, '\''))
        assertEquals("'ok'", syntax!!.extractString("test 'ok' test", 5, '\''))
        assertEquals("'ok \"inner\"'", syntax!!.extractString("test 'ok \"inner\"' test", 5, '\''))
    }

    @Test
    @Throws(ParseException::class)
    fun testIsString() {
        assertTrue(syntax!!.isString("'123'"))
        assertTrue(syntax!!.isString("\"123\""))
        assertTrue(syntax!!.isString("'123 \" 321'"))

        assertFalse(syntax!!.isString("'123"))
        assertFalse(syntax!!.isString(" '123'"))
        assertFalse(syntax!!.isString("'123' "))
        assertFalse(syntax!!.isString("123'"))
        assertFalse(syntax!!.isString("'123\""))
        assertFalse(syntax!!.isString("\"123'"))
    }

    @Test
    @Throws(ParseException::class)
    fun testFirstChar() {
        assertEquals(0, syntax!!.firstChar("", 0).toLong())
        assertEquals(2, syntax!!.firstChar("   ", 0).toLong())
        assertEquals(3, syntax!!.firstChar("   color:", 0).toLong())
        assertEquals(3, syntax!!.firstChar("\n\n\ncolor:", 0).toLong())
        assertEquals(3, syntax!!.firstChar("\t\t\tcolor:", 0).toLong())
        assertEquals(3, syntax!!.firstChar("\n \tcolor:", 0).toLong())
    }

    @Test
    fun testLastNumberIndex() {
        assertEquals(10, syntax!!.lastNumberIndex("NAME:STR20", 8, DECIMAL_SEP).toLong())
        assertEquals(13, syntax!!.lastNumberIndex("NAME:STR20,02", 8, DECIMAL_SEP).toLong())
        assertEquals(13, syntax!!.lastNumberIndex("NAME:STR20,02*", 8, DECIMAL_SEP).toLong())
        assertEquals(12, syntax!!.lastNumberIndex("NAME:STR20,2,3", 8, DECIMAL_SEP).toLong())
        assertEquals(4, syntax!!.lastNumberIndex("#1-5", 1, OCCURS_RANGE).toLong())
        assertEquals(14, syntax!!.lastNumberIndex("name:STR20#1-5", 11, OCCURS_RANGE).toLong())
        assertEquals(14, syntax!!.lastNumberIndex("name:STR20#1-5xpto", 11, OCCURS_RANGE).toLong())
        assertEquals(14, syntax!!.lastNumberIndex("name:STR20#1-5-2", 11, OCCURS_RANGE).toLong())
        assertEquals(15, syntax!!.lastNumberIndex("name:STR20#1-55", 11, OCCURS_RANGE).toLong())

    }


    @Test
    fun testAsPpl() {
        val syntax = Syntax()
        val metadata = "name:S7;age:I2"
        val metadata2 = "(name:S7;age:I2)"
        val payload = "Ladybug15"
        assertEquals("($metadata)$payload", syntax.asPplString(metadata, payload))
        assertEquals("($metadata)$payload", syntax.asPplString(metadata2, payload))
    }

}
