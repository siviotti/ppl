package br.net.buzu.pplimpl.metadata


import br.net.buzu.pplspec.exception.MetadataParseException
import br.net.buzu.pplspec.lang.NO_NAME_START
import br.net.buzu.pplspec.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*


/**
 * Unit Test domainOf MetadataParser
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class MetadataParserTest {


    @Test
    fun testParse() {
        val metadata = parseMetadata(pplStringOf("(name:S20#0-1)"))
        assertMetadata(metadata, "name", Subtype.STRING, 20, 0, 1)
    }

    @Test
    fun testParseSingle() {
        val metadata = parseMetadata(NAME, genericCreateMetadata, IndexSequence())
        assertMetadata(metadata, "name", Subtype.STRING, 20, 0, 1)
    }

    private fun assertMetadata(metadata: Metadata, name: String, subtype: Subtype, size: Int, minOccurs: Int,
                               maxOccurs: Int) {
        assertEquals(name, metadata.name())
        assertEquals(subtype, metadata.info().subtype)
        assertEquals(size, metadata.info().size)
        assertEquals(minOccurs, metadata.info().minOccurs)
        assertEquals(maxOccurs, metadata.info().maxOccurs)
    }

    @Test
    fun testParseComplex() {
        val metadata = parseMetadata( PERSON, genericCreateMetadata, IndexSequence())
        assertEquals(3, metadata.children<Metadata>().size)
        assertMetadata(metadata.children<Metadata>()[0], "name", Subtype.STRING, 20, 0, 1)
        assertMetadata(metadata.children<Metadata>()[1], "age", Subtype.INTEGER, 2, 0, 1)
        assertMetadata(metadata.children<Metadata>()[2], "city", Subtype.STRING, 5, 0, 1)
        // treeIndex 0, 1, 2, 3
        assertEquals(0, metadata.info().index)
        assertEquals(1, metadata.children<Metadata>()[0].info().index)
        assertEquals(2, metadata.children<Metadata>()[1].info().index)
        assertEquals(3, metadata.children<Metadata>()[2].info().index)
    }

    @Test
    fun testParseExtension() {
        val metadata = parseMetadata( EXT, genericCreateMetadata, IndexSequence())
        assertMetadata(metadata, "color", Subtype.STRING, 10, 0, 1)
        val domain = domainOf("black", "white", "red")
        assertEquals(domain.items(), metadata.info().domain.items())
    }

    @Test
    fun testEmptyMetadata() {
        val metadada = parseMetadata(pplStringOf("()")) as StaticMetadata
        assertEquals(NO_NAME_START + "0", metadada.name())
        assertEquals(Subtype.STRING, metadada.info().subtype)
        assertEquals(Subtype.CHAR.fixedSize(), metadada.info().size)
        assertEquals(Subtype.CHAR.fixedSize(), metadada.serialMaxSize())
        assertEquals(0, metadada.info().scale)
        assertEquals(0, metadada.info().minOccurs)
        assertEquals(1, metadada.info().maxOccurs)
        //assertEquals("S0", ShortMetadataCoder.INSTANCE.code(metadada));
    }

    @Test
    fun testEmptyLayout() {
        val metadada = parseMetadata(pplStringOf("(X:(;;))")) as StaticMetadata
        //assertEquals(ComplexStaticMetadada.class, metadada.getClass());
        assertEquals("X", metadada.name())
        assertEquals(Subtype.OBJ, metadada.info().subtype)
        assertEquals(0, metadada.info().size) // 2 x 0
        assertEquals(0, metadada.serialMaxSize())
        assertEquals(0, metadada.info().minOccurs)
        assertEquals(1, metadada.info().maxOccurs)
        assertFalse(metadada.info().isExtended)
        // assertEquals("C0", Dialect.VERBOSE.serialize(metadada));
    }

    @Test
    fun testParseNameErrorTooLong() {
        assertThrows(MetadataParseException::class.java) { parseName(PplNode("name5678901234567890123456789012345678901234567890xyz", "", "", ""), 0) }
    }

    @Test
    fun testParseNameErrorInvalidChar() {
        assertThrows(MetadataParseException::class.java) { parseName(PplNode("name@xyz", "", "", ""), 0) }
    }

    @Test
    fun testParseSubtype() {
        assertEquals(Subtype.STRING, parseSubtype(PplNode("", "", "", "")))
        assertEquals(Subtype.STRING, parseSubtype(PplNode("", "S", "", "")))
        assertEquals(Subtype.STRING, parseSubtype(NAME))
        assertEquals(Subtype.INTEGER, parseSubtype(AGE))
    }

    @Test
    fun testParseSubtypeError() {
        assertThrows(IllegalArgumentException::class.java) {
            val metadata = parseMetadata(pplStringOf("(name:z20#0-1)"))
            assertMetadata(metadata, "name", Subtype.STRING, 20, 0, 1)
        }
    }

    @Test
    fun testParseDomain() {
        val domain = parseDomain(EXT)
        assertEquals("black", domain.items()[0].value())
        assertEquals("white", domain.items()[1].value())
        assertEquals("red", domain.items()[2].value())

        val node1 = PplNode("color", "S", "10", "0-1", "[black,white,red]", "red", "K I test")
        val domain1 = parseDomain(node1)
        assertEquals(domain, domain1)

        val node2 = PplNode("color", "S", "10", "0-1", "[\"bl'ack\",'wh,i\"te',red]", "red", "K I test")
        val domain2 = parseDomain(node2)
        assertEquals("bl'ack", domain2.items()[0].value())
        assertEquals("wh,i\"te", domain2.items()[1].value())
        assertEquals("red", domain2.items()[2].value())

        val nodeError = PplNode("color", "S", "10", "0-1", "['black','white,'red']", "red", "K I test")
        try {
            parseDomain(nodeError)
            fail<Any>()
        } catch (e: MetadataParseException) {
        }
    }

    @Test
    fun testParseComplexDomain() {
        parseDomain(COMPLEX_EXT)
    }

    companion object {

        internal var NAME = PplNode("name", "S", "20", "0-1", "")
        internal var AGE = PplNode("age", "I", "2", "", "")
        internal var CITY = PplNode("city", "S", "5", "", "")
        internal var CHILDREN: MutableList<PplNode> = ArrayList()

        init {
            CHILDREN.add(NAME)
            CHILDREN.add(AGE)
            CHILDREN.add(CITY)
        }

        internal var PERSON = PplNode("", "", "", "", "", "", "", CHILDREN)
        internal var EXT = PplNode("color", "S", "10", "0-1", "['black','white','red']", "red", "K I test")
        internal var COMPLEX_EXT = PplNode("color", "S", "10", "0-1", "['1=black','2=white','3=red']", "3",
                "K I test")
    }
}
