package br.net.buzu.pplspec.model


import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.lang.EMPTY
import org.junit.Assert.*
import org.junit.Test


/**
 * MetaInfo Unit Test.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class MetaInfoTest {


    //@Test(expected = IllegalArgumentException::class)
    @Test(expected = KotlinNullPointerException::class)
    fun testMissingSubtype() {
        // null!! creates a KotlinNullPointerException
        MetaInfo(0, EMPTY, null!!, 0, 0, 0, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidOccurs() {
        MetaInfo(0, EMPTY, Subtype.STRING, 0, 0, 5, 0) // 5 > 0, Unbounded
        MetaInfo(0, EMPTY, Subtype.STRING, 0, 0, 5, 4) // 5 > 4, invalid
    }

    @Test
    fun testEmptyMetaInfo() {
        val metaInfo = MetaInfo( 0, EMPTY, Subtype.STRING, 0, 0, 0, 0)
        assertFalse(metaInfo.hasDomain())
        assertMetainfo(metaInfo, EMPTY, Subtype.STRING, 0, 0, 0, 0, EMPTY, Domain.EMPTY, EMPTY)
        //assertEquals("", metaInfo.id)
    }

    @Test
    fun testIncomplete() {
        val metaInfo1 = MetaInfo(0, EMPTY, Subtype.STRING, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                0, PplMetadata.EMPTY_INTEGER)
        assertMetainfo(metaInfo1, EMPTY, Subtype.STRING, PplMetadata.EMPTY_INTEGER, MetaInfo.NO_SCALE, 0,
                PplMetadata.EMPTY_INTEGER, EMPTY, Domain.EMPTY, EMPTY)
        assertFalse(metaInfo1.isComplete)
        assertFalse(metaInfo1.hasSize())
        assertFalse(metaInfo1.hasMaxOccurs())
        //
        val metaInfo2 = MetaInfo(0, EMPTY, Subtype.STRING, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                0, 5)
        assertMetainfo(metaInfo2, EMPTY, Subtype.STRING, PplMetadata.EMPTY_INTEGER, MetaInfo.NO_SCALE, 0, 5,
                EMPTY, Domain.EMPTY, EMPTY)
        assertFalse(metaInfo2.isComplete)
        assertFalse(metaInfo2.hasSize()) // PplMetadata.EMPTY_INTEGER
        assertTrue(metaInfo2.hasMaxOccurs()) // 5
        //
        val metaInfo3 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 0, PplMetadata.EMPTY_INTEGER)
        assertMetainfo(metaInfo3, EMPTY, Subtype.STRING, 10, 0, 0, PplMetadata.EMPTY_INTEGER, EMPTY,
                Domain.EMPTY, EMPTY)
        assertFalse(metaInfo3.isComplete)
        assertTrue(metaInfo3.hasSize()) // PplMetadata.EMPTY_INTEGER
        assertFalse(metaInfo3.hasMaxOccurs()) // 5
    }

    @Test
    fun testCompleteMetaInfo() {
        val metaInfo2 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 0, 5)
        assertMetainfo(metaInfo2, EMPTY, Subtype.STRING, 10, 0, 0, 5, EMPTY, Domain.EMPTY, EMPTY)
        assertTrue(metaInfo2.isComplete)
        assertTrue(metaInfo2.hasSize()) // 10
        assertTrue(metaInfo2.hasMaxOccurs()) // 5
    }

    @Test
    fun testRequired() {
        val metaInfo1 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 0, 5)
        assertFalse(metaInfo1.isRequired) // min = 0 - not required
        val metaInfo2 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 1, 5)
        assertTrue(metaInfo2.isRequired) // min = 1 = requered
        val metaInfo3 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 5, 5)
        assertTrue(metaInfo3.isRequired) // min = 5 = requered
    }

    @Test
    fun testMultiple() {
        val metaInfo1 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 0, 5)
        assertTrue(metaInfo1.isMultiple) // 5
        val metaInfo2 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 0, 1)
        assertFalse(metaInfo2.isMultiple) // 1
    }

    @Test
    fun testUnbounded() {
        val metaInfo1 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 0, 0)
        assertTrue(metaInfo1.isMultiple) // 5
        assertTrue(metaInfo1.isUnbounded) // 0 = many

        val metaInfo2 = MetaInfo(0, EMPTY, Subtype.STRING, 10, 0, 0, 1)
        assertFalse(metaInfo2.isMultiple) // 1
        assertFalse(metaInfo2.isUnbounded) // 1 = unique
    }

    @Test
    fun testDomain() {
        val domain = domainOf(*array)
        val metaInfo = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ")

        assertTrue(metaInfo.hasDomain())
        assertTrue(metaInfo.inDomain("Diamond"))
        assertTrue(metaInfo.inDomain("Heart"))
        assertTrue(metaInfo.inDomain("Club"))
        assertTrue(metaInfo.inDomain("Spade"))

        assertFalse(metaInfo.inDomain(null))
        assertFalse(metaInfo.inDomain(""))
        assertFalse(metaInfo.inDomain("xyz"))
        assertFalse(metaInfo.inDomain("club"))

        val other = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, EMPTY, EMPTY)
        assertFalse(other.hasDomain())
        assertTrue(other.inDomain("Diamond"))
        assertTrue(other.domain == Domain.EMPTY)
    }

    @Test
    fun testDefaultValue() {
        val metaInfo = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, "DefaultValue", "XYZ")
        assertEquals("DefaultValue", metaInfo.defaultValue)
    }

    @Test
    fun testFillChar() {
        val metaInfo = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, "DefaultValue", "XYZ!%")
        assertEquals('%', metaInfo.fillChar)
        val metaInfo2 = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, "DefaultValue", "XYZ!#!$")
        assertEquals('#', metaInfo2.fillChar)

    }

    @Test
    fun testNullChar() {
        val metaInfo = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, "DefaultValue", "XYZ?%")
        assertEquals('%', metaInfo.nullChar)
        assertEquals(' ', metaInfo.fillChar)
        val metaInfo2 = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, "DefaultValue", "XYZ!#?$")
        assertEquals('$', metaInfo2.nullChar)
        assertEquals('#', metaInfo2.fillChar)
    }

    @Test
    fun testTags() {
        val metaInfo = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, EMPTY, "XYZ")

        assertTrue(metaInfo.isTagPresent("X"))
        assertTrue(metaInfo.isTagPresent("XY"))
        assertTrue(metaInfo.isTagPresent("XYZ"))

        assertFalse(metaInfo.isTagPresent("XZ"))
        //assertFalse(metaInfo.isTagPresent(null))

        val other = MetaInfo( 31, "nameTest", Subtype.STRING, 100, 0, 1, 20, Domain.EMPTY, EMPTY, EMPTY)
        assertFalse(other.isTagPresent("X"))
        assertTrue(other.tags === EMPTY)
    }

    @Test
    fun testComplete() {
        val domain = domainOf(*array)
        val metaInfo = MetaInfo( 1, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ")
        assertMetainfo(metaInfo, "nameTest", Subtype.STRING, 100, 0, 1, 20, "Heart", domain, "XYZ")

        val copy = MetaInfo( 1, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ")
        val copy2 = MetaInfo( 1, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ")
        val copy3 = MetaInfo( 9, "NOT-EQUALS", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ")
        // equals, hascode and toString
        assertEquals(metaInfo, copy)
        assertNotSame(metaInfo, copy3)
        assertTrue(metaInfo == metaInfo) // reflexive
        assertTrue(metaInfo == copy && copy == metaInfo) // Symmetry
        assertTrue(copy == metaInfo && copy2 == metaInfo && copy == copy2) // Transitive
        assertTrue(metaInfo == copy && metaInfo == copy && metaInfo == copy) // Consistent
        assertFalse(metaInfo == null)
        // compareTo
        assertTrue(copy.compareTo(copy2) == 0)
        assertTrue(copy.compareTo(copy3) == -1)
        assertTrue(copy3.compareTo(copy) == 1)
        // hashcode
        assertEquals(metaInfo.hashCode().toLong(), copy.hashCode().toLong())
        assertEquals(metaInfo.toString(), copy.toString())
        // Boolean API
        assertTrue(metaInfo.isMultiple)
        assertTrue(metaInfo.isMultiple)
    }

    companion object {

        private val array = arrayOf("Diamond", "Heart", "Club", "Spade")

        fun assertMetainfo(metaInfo: MetaInfo, name: String?, subtype: Subtype, size: Int, scale: Int,
                           minOccurs: Int, maxOccurs: Int) {
            assertEquals(name, metaInfo.name)
            assertEquals(size.toLong(), metaInfo.size.toLong())
            assertEquals(scale.toLong(), metaInfo.scale.toLong())
            assertEquals(subtype, metaInfo.subtype)
            assertEquals(minOccurs.toLong(), metaInfo.minOccurs.toLong())
            assertEquals(maxOccurs.toLong(), metaInfo.maxOccurs.toLong())
        }

        fun assertMetainfo(metaInfo: MetaInfo, name: String?, subtype: Subtype, size: Int, scale: Int,
                           minOccurs: Int, maxOccurs: Int, defaultValue: String, domain: Domain, tags: String) {
            assertMetainfo(metaInfo, name, subtype, size, scale, minOccurs, maxOccurs)
            assertEquals(defaultValue, metaInfo.defaultValue)
            assertEquals(domain, metaInfo.domain)
            assertEquals(tags, metaInfo.tags)
        }

        fun domain(vararg items: String): Domain {
            return domainOf(*items)
        }
    }


}

