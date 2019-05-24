package br.net.buzu.pplspec.lang

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.text.ParseException

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class SyntaxTest {


    @Test
    fun testIgnoreChar() {
        assertTrue(pplIsIgnored(' '))
        assertTrue(pplIsIgnored('\n'))
        assertTrue(pplIsIgnored('\t'))

        assertFalse(pplIsIgnored('A'))
        assertFalse(pplIsIgnored('_'))
        assertFalse(pplIsIgnored('2'))
    }

    @Test
    fun testValidFirstCharMetaName() {
        assertTrue(pplIsValidFirstCharMetaName('A'))
        assertTrue(pplIsValidFirstCharMetaName('a'))
        assertTrue(pplIsValidFirstCharMetaName('_'))

        assertFalse(pplIsValidFirstCharMetaName('-'))
        assertFalse(pplIsValidFirstCharMetaName('2'))

    }

    @Test
    fun testValidCharMetaName() {
        assertTrue(pplIsValidCharMetaName('A'))
        assertTrue(pplIsValidCharMetaName('a'))
        assertTrue(pplIsValidCharMetaName('_'))
        assertTrue(pplIsValidCharMetaName('-'))
        assertTrue(pplIsValidCharMetaName('-'))
        assertTrue(pplIsValidCharMetaName('2'))

        assertFalse(pplIsValidCharMetaName('+'))
        assertFalse(pplIsValidCharMetaName('|'))

    }

    @Test
    fun testValidMetaName() {
        assertTrue(pplIsValidMetaName("abc"))
        assertTrue(pplIsValidMetaName("camelCase"))
        assertTrue(pplIsValidMetaName("CAPS_CONST"))
        assertTrue(pplIsValidMetaName("name-ok-20_X"))

        assertFalse(pplIsValidMetaName(null))
        assertFalse(pplIsValidMetaName(""))
        assertFalse(pplIsValidMetaName("2abc"))
        assertFalse(pplIsValidMetaName("abc&def"))
    }

    @Test
    fun testPerformance() {
        val name = "name-test_ABC-25"
        val repeat = 1000000
        for (i in 0 until repeat) {
            NAME_PATTERN.matcher(name).matches()
        }
        for (i in 0 until repeat) {
            pplIsValidMetaName(name)
        }
    }

    @Test
    fun testRegex() {
        // Name
        assertTrue(NAME_PATTERN.matcher("abc:").matches())
        assertTrue(NAME_PATTERN.matcher("_abc:").matches())
        assertTrue(NAME_PATTERN.matcher("abcXpto25:").matches())
        assertTrue(NAME_PATTERN.matcher("Abc-Xpto_2:").matches())

        assertFalse(NAME_PATTERN.matcher("").matches())
        assertFalse(NAME_PATTERN.matcher(":").matches())
        assertFalse(NAME_PATTERN.matcher("abc").matches())
        assertFalse(NAME_PATTERN.matcher("2abcXpto25:").matches())
        assertFalse(NAME_PATTERN.matcher("-abcXpto25:").matches())
        assertFalse(NAME_PATTERN.matcher("abc def:").matches())

        // Type
        assertTrue(TYPE_PATTERN.matcher("C").matches())
        assertTrue(TYPE_PATTERN.matcher("String").matches())
        assertTrue(TYPE_PATTERN.matcher("STR(etc * ç #2)").matches())

        assertFalse(TYPE_PATTERN.matcher("").matches())
        assertFalse(TYPE_PATTERN.matcher(" C").matches())
        assertFalse(TYPE_PATTERN.matcher("VARCHAR2").matches())
        assertFalse(TYPE_PATTERN.matcher("STR(etc * ç #2").matches())

        // Size
        assertTrue(SIZE_PATTERN.matcher("25").matches())
        assertTrue(SIZE_PATTERN.matcher("10,2").matches())

        assertFalse(SIZE_PATTERN.matcher("").matches())
        assertFalse(SIZE_PATTERN.matcher("25 ").matches())
        assertFalse(SIZE_PATTERN.matcher(" 25").matches())
        assertFalse(SIZE_PATTERN.matcher("10.2").matches())
        assertFalse(SIZE_PATTERN.matcher("10,2,2").matches())

        // Occurs
        assertTrue(OCCURS_PATTERN.matcher("25").matches())
        assertTrue(OCCURS_PATTERN.matcher("2-10").matches())

        assertFalse(OCCURS_PATTERN.matcher("").matches())
        assertFalse(OCCURS_PATTERN.matcher("25 ").matches())
        assertFalse(OCCURS_PATTERN.matcher(" 25").matches())
        assertFalse(OCCURS_PATTERN.matcher("2,10").matches())
        assertFalse(OCCURS_PATTERN.matcher("2:10").matches())

        // Meta
        assertTrue(META_PATTERN.matcher(";").matches())
        assertTrue(META_PATTERN.matcher("phone:S9#0-2;").matches())
        assertTrue(META_PATTERN.matcher("birthday:D{yyyy-mm-dd};").matches())

    }

    @Test
    @Throws(ParseException::class)
    fun testEndIndex() {
        assertEquals(3, pplBlockEndIndex("a{b}c", 1, '{', '}').toLong())
        assertEquals(28, pplBlockEndIndex("a( teste ( str ' xpto ) 'b)c) abc", 1, '(', ')').toLong())
        assertEquals(28, pplBlockEndIndex("a( teste ( str \" xpto ) \"b)c) abc", 1, '(', ')').toLong())
        assertEquals(24, pplBlockEndIndex("test(inside')'(go)\"x'y\"z)out", 4, '(', ')').toLong())
        // 1 2 X 2 1

        val e = assertThrows(ParseException::class.java){
            assertEquals(3, pplBlockEndIndex("a{b}c", 1, '(', ')').toLong())
        }
        val message = e.message ?: ""
        assertTrue(message.startsWith(BLOCK_ERROR_INVALID_OPEN_CHAR))

        assertEquals(12, pplBlockEndIndex("xyz (abc(JJ)) def", 4, '(', ')').toLong())
        assertEquals(12, pplBlockEndIndex("2[34[4[5]ax]]", 1, '[', ']').toLong())

        val e2 = assertThrows(ParseException::class.java){
            pplBlockEndIndex("2[34[4[5]ax]", 1, '[', ']')
        }
        val message2 = e2.message ?: ""
        assertTrue(message2.startsWith(BLOCK_ERROR))


        val e3 = assertThrows(ParseException::class.java){
            pplBlockEndIndex("2[34[4[5]ax]", 1, '[', ']')
        }
        val message3 = e3.message ?: ""
        assertTrue(message3.startsWith(BLOCK_ERROR))


        val e4 = assertThrows(ParseException::class.java){
            pplBlockEndIndex("abc ( 'def) xyz", 4, '(', ')')
        }
        val message4 = e4.message ?: ""
        assertTrue(message4.startsWith(UNTERMINATED_STRING))

    }

    @Test
    @Throws(ParseException::class)
    fun testStringEndIndex() {
        assertEquals(5, pplNextStringDelimiter("'1234'", 0, '\'').toLong())
        assertEquals(8, pplNextStringDelimiter("test 'ok' test", 5, '\'').toLong())
        assertEquals(16, pplNextStringDelimiter("test 'ok \"inner\"' test", 5, '\'').toLong())
    }

    @Test
    @Throws(ParseException::class)
    fun testStringExtract() {
        assertEquals("'1234'", pplExtractString("'1234'", 0, '\''))
        assertEquals("'ok'", pplExtractString("test 'ok' test", 5, '\''))
        assertEquals("'ok \"inner\"'", pplExtractString("test 'ok \"inner\"' test", 5, '\''))
    }

    @Test
    @Throws(ParseException::class)
    fun testIsString() {
        assertTrue(pplIsString("'123'"))
        assertTrue(pplIsString("\"123\""))
        assertTrue(pplIsString("'123 \" 321'"))

        assertFalse(pplIsString("'123"))
        assertFalse(pplIsString(" '123'"))
        assertFalse(pplIsString("'123' "))
        assertFalse(pplIsString("123'"))
        assertFalse(pplIsString("'123\""))
        assertFalse(pplIsString("\"123'"))
    }

    @Test
    @Throws(ParseException::class)
    fun testFirstChar() {
        assertEquals(0, pplFirstChar("", 0).toLong())
        assertEquals(2, pplFirstChar("   ", 0).toLong())
        assertEquals(3, pplFirstChar("   color:", 0).toLong())
        assertEquals(3, pplFirstChar("\n\n\ncolor:", 0).toLong())
        assertEquals(3, pplFirstChar("\t\t\tcolor:", 0).toLong())
        assertEquals(3, pplFirstChar("\n \tcolor:", 0).toLong())
    }

    @Test
    fun testLastNumberIndex() {
        assertEquals(10, pplLastNumberIndex("NAME:STR20", 8, DECIMAL_SEP).toLong())
        assertEquals(13, pplLastNumberIndex("NAME:STR20,02", 8, DECIMAL_SEP).toLong())
        assertEquals(13, pplLastNumberIndex("NAME:STR20,02*", 8, DECIMAL_SEP).toLong())
        assertEquals(12, pplLastNumberIndex("NAME:STR20,2,3", 8, DECIMAL_SEP).toLong())
        assertEquals(4, pplLastNumberIndex("#1-5", 1, OCCURS_RANGE).toLong())
        assertEquals(14, pplLastNumberIndex("name:STR20#1-5", 11, OCCURS_RANGE).toLong())
        assertEquals(14, pplLastNumberIndex("name:STR20#1-5xpto", 11, OCCURS_RANGE).toLong())
        assertEquals(14, pplLastNumberIndex("name:STR20#1-5-2", 11, OCCURS_RANGE).toLong())
        assertEquals(15, pplLastNumberIndex("name:STR20#1-55", 11, OCCURS_RANGE).toLong())

    }


    @Test
    fun testAsPpl() {
        val metadata = "name:S7;age:I2"
        val metadata2 = "(name:S7;age:I2)"
        val payload = "Ladybug15"
        assertEquals("($metadata)$payload", pplToString(metadata, payload))
        assertEquals("($metadata)$payload", pplToString(metadata2, payload))
    }

}
