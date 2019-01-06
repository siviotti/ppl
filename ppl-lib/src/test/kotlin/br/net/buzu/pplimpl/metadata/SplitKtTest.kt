package br.net.buzu.pplimpl.metadata

import br.net.buzu.lang.EMPTY
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.text.ParseException

internal class SplitKtTest {

    @Test
    fun testSplitName() {
        assertEquals(Split(5, "xpto"), splitName("xpto:INT", 0))
        assertEquals(Split(7, "Xpto25"), splitName("Xpto25:INT", 0))
        assertEquals(Split(0, EMPTY), splitName("INT", 0))
        assertEquals(Split(1, EMPTY), splitName(":INT", 0))
        assertEquals(Split(0, EMPTY), splitName("xpto", 0))
        assertEquals(Split(8, "xpto"), splitName("   xpto:INT", 0))
        assertEquals(Split(9, "\$xpto"), splitName("   \$xpto:INT", 0))
        assertEquals(Split(8, "xpto"), splitName("\n" + "\n" + "\n" + "xpto:INT", 0))
        assertEquals(Split(8, "xpto"), splitName("\t\t\txpto:INT", 0))
        assertEquals(Split(2, EMPTY), splitName("   ", 0))
        assertEquals(Split(0, EMPTY), splitName("INT", 0))
    }

    @Test
    fun testSplitType() {
        assertEquals(TypeSplit(3, EMPTY), splitType("    ", 0))
    }

    @Test
    fun testSplitSize() {
        assertEquals(Split(2, "20"), splitSize("20", 0))
        assertEquals(Split(4, "20"), splitSize("  20  ", 0))
        assertEquals(Split(10, "20"), splitSize("NAME:STR20", 8))
        assertEquals(Split(6, EMPTY), splitSize("NAME:D{yyyy-MM-dd}", 6))
        assertEquals(Split(13, "20,02"), splitSize("NAME:STR20,02", 8))
        assertEquals(Split(13, "20,02"), splitSize("NAME:STR20,02*", 8))
        assertEquals(Split(12, "20,2"), splitSize("NAME:STR20,2,3", 8))
    }

    @Test
    fun testSplitOccurs() {
        assertEquals(Split(0, EMPTY), splitOccurs("1-7", 0))
        assertEquals(Split(4, "1-5"), splitOccurs("#1-5", 0))
        assertEquals(Split(7, "1-5"), splitOccurs("   #1-5   ", 0))
        assertEquals(Split(14, "1-5"), splitOccurs("name:STR20#1-5", 10))
        assertEquals(Split(14, "1-5"), splitOccurs("name:STR20#1-5xpto", 10))
        assertEquals(Split(14, "1-5"), splitOccurs("name:STR20#1-5-2", 10))
        assertEquals(Split(15, "1-55"), splitOccurs("name:STR20#1-55", 10))
    }

    @Test
    fun testSplitDomain() {
        assertEquals(Split(17, "['black','white']"), splitDomain("['black','white']", 0))
        assertEquals(Split(20, "['black','white']"), splitDomain("   ['black','white']", 0))
        assertEquals(Split(20, "['black','white']"), splitDomain("   ['black','white']    ", 0))
        assertEquals(Split(3, EMPTY), splitDomain("   bla", 0))
        assertEquals(Split(30, "['black','white']"), splitDomain("field:S10#1-3['black','white']", 13))
        try {
            assertEquals(Split(17, "['black','white']"), splitDomain("['black','white'", 0))
            fail("should not enter here")
        } catch (e: ParseException){

        }
    }

    @Test
    fun testSplitDefaultValue() {
        assertEquals(Split(9, "black"), splitDefaultValue("='black'", 0))
        assertEquals(Split(9, "black"), splitDefaultValue("=\"black\"", 0))
        assertEquals(Split(13, "black"), splitDefaultValue("    ='black'", 0))
        assertEquals(Split(13, "55555"), splitDefaultValue("    ='55555'", 0))
        assertEquals(Split(3, EMPTY), splitDefaultValue("   bla", 0))
        assertEquals(Split(0, EMPTY), splitDefaultValue("=", 0))
        assertEquals(Split(8, "black'"), splitDefaultValue("=black'", 0))
        assertEquals(Split(12, "black'abc'"), splitDefaultValue("=black'abc' out", 0))
        assertEquals(Split(13, "black='abc'"), splitDefaultValue("=black='abc' out", 0))
        try {
            assertEquals(Split(9, "black"), splitDefaultValue("='black", 0))
            fail("should not enter here")
        } catch (e: ParseException){

        }
        assertEquals(Split(9, "-'black"), splitDefaultValue("=-'black", 0))
        try {
            assertEquals(Split(9, "black"), splitDefaultValue("='", 0))
            fail("should not enter here")
        } catch (e: ParseException){

        }
        try {
            assertEquals(Split(9, "black"), splitDefaultValue("=\"black'", 0))
            fail("should not enter here")
        } catch (e: ParseException){

        }

    }

    @Test
    fun testSplitTags() {
        assertEquals("1-5", splitTags("1-5", 0))
        assertEquals("'teste'", splitTags("'teste'", 0))
        assertEquals("'teste' abc ';'", splitTags("'teste' abc ';'", 0))
        assertEquals("(20)", splitTags("(20)", 0))
        assertEquals("tags", splitTags("tags", 0))
        assertEquals("tags", splitTags(" tags", 0))
        assertEquals("tags", splitTags(" tags ", 0))
        assertEquals("tags", splitTags("    tags     ", 0))
        assertEquals("A B C", splitTags(" A B C ", 0))
        assertEquals("A  B  C", splitTags("  A  B  C  ", 0))
        assertEquals("A\n\t B\t\n C", splitTags("\n\t  A\n\t B\t\n C \t\n", 0))

    }

    @Test
    @Throws(ParseException::class)
    fun testSplitNodes() {
        var metas = splitNodes("price:NUM10,2#1-2dolar")
        assertEquals(1, metas.size.toLong())
        val node = metas.get(0)
        assertEquals("price", node.name)
        assertEquals("NUM", node.type)
        assertEquals("10,2", node.size)
        assertEquals("1-2", node.occurs)
        assertEquals("dolar", node.tags)

        metas = splitNodes("a:STR5;b:INT3#0-9*prop;c:NUM9;")
        metas = splitNodes("a:STR5;b:R(x:STr2;y:STR3)3#0-9*prop;c:NUM9")
    }

    @Test
    fun testSplitMeta() {
        // Minimum
        //assertMetaChildren("(20)", "", "", "", "", "", 1)
        assertMeta("20", "", "", "20", "", "")
        assertMeta("onlyName:", "onlyName", "", "", "", "")
        assertMeta(":20", "", "", "20", "", "")
        // Basic
        assertMeta("age:INT3", "age", "INT", "3", "", "")
        assertMeta("age:INT3#", "age", "INT", "3", "", "")
        assertMeta("age: INT 3", "age", "INT", "3", "", "")
        assertMeta("birthday:D'extension'", "birthday", "D", "", "", "'extension'")
        assertMeta("hour:T#1-2{hh:mm:ss}", "hour", "T", "", "1-2", "{hh:mm:ss}")
        assertMeta("smoke:BYN", "smoke", "BYN", "", "", "")
        // Complete
        assertMeta("price:NUM10,2#1-2dolar", "price", "NUM", "10,2", "1-2", "dolar")
        assertMeta("price: NUM 10,2 #1-2 tags", "price", "NUM", "10,2", "1-2", "tags")
        assertMeta("price:\n NUM\n 10,2\n #1-2 A B C", "price", "NUM", "10,2", "1-2", "A B C")
        assertMeta("price:\t NUM\t 10,2\t #1-2 A B C ", "price", "NUM", "10,2", "1-2", "A B C")
        assertMeta("price:    NUM    10,2    #1-2 dolar", "price", "NUM", "10,2", "1-2", "dolar")

        assertMeta("price: NUM 10,2 # dolar", "price", "NUM", "10,2", "", "dolar")
        assertMeta(" price:NUM10,2#1-2dolar", "price", "NUM", "10,2", "1-2", "dolar")
        assertMeta(" price: NUM10,2#1-2 dolar", "price", "NUM", "10,2", "1-2", "dolar")
        assertMeta("\nprice:NUM10,2#1-2dolar", "price", "NUM", "10,2", "1-2", "dolar")
        // Extension (domain, defaultValue and tags)
        assertMetaExt("color: S 10 ['White','Red', 'Green']", "color", "S", "10", "", "['White','Red', 'Green']", "",
                "")
        assertMetaExt("color: S 10 ['White','Red', 'Green'] ='def' A B C", "color", "S", "10", "",
                "['White','Red', 'Green']", "def", "A B C")
        // Complex
        assertMetaChildren("R(f1:STR10;f2:ST20)#2-4dolar", "", "R", "", "2-4", "dolar", 2)
        assertMetaChildren("(f1:STR10;f2:ST20)#2-4dolar", "", "", "", "2-4", "dolar", 2)

        // Bad
        assertMeta("price : NUM 10,2 #1-2 dolar", "", "price", "", "", ": NUM 10,2 #1-2 dolar")

    }

    @Test
    fun testSplitMetaError() {

    }

    private fun assertMeta(metaStr: String, name: String, type: String, size: String, occurs: String, tags: String) {
        val node = splitNode(metaStr)
        assertEquals(name, node.name)
        assertEquals(type, node.type)
        assertEquals(size, node.size)
        assertEquals(occurs, node.occurs)
        assertEquals(tags, node.tags)
    }

    private fun assertMetaExt(metaStr: String, name: String, type: String, size: String, occurs: String, domain: String,
                              defaultValue: String, tags: String) {
        val node = splitNode(metaStr)
        assertEquals(name, node.name)
        assertEquals(type, node.type)
        assertEquals(size, node.size)
        assertEquals(occurs, node.occurs)
        assertEquals(tags, node.tags)
        assertEquals(domain, node.domain)
        assertEquals(defaultValue, node.defaultValue)
        assertEquals(tags, node.tags)
    }

    private fun assertMetaChildren(metaStr: String, name: String, type: String, size: String, occurs: String, tags: String,
                                   childrenCount: Int) {
        val node = splitNode(metaStr)
        assertEquals(name, node.name)
        assertEquals(type, node.type)
        assertEquals(size, node.size)
        assertEquals(occurs, node.occurs)
        assertEquals(tags, node.tags)
        assertEquals(tags, node.tags)
        assertEquals(childrenCount.toLong(), node.children.size.toLong())
    }
}