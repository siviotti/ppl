package br.net.buzu.lib

import org.junit.Assert
import org.junit.Test

/**
 * @author Douglas Siviotti
 */
class TextTest {


    @Test
    fun testBlank() {
        Assert.assertTrue(isBlank(null))
        Assert.assertTrue(isBlank(""))
        Assert.assertTrue(isBlank("    "))
        Assert.assertTrue(isBlank("         "))

        Assert.assertFalse(isBlank("   0"))
        Assert.assertFalse(isBlank("_______"))
        Assert.assertFalse(isBlank("''''"))
    }

    @Test
    fun testLeftFit() {
        Assert.assertEquals("12345", leftFit("12345", 5, '0'))
        Assert.assertEquals("12345", leftFit("00012345", 5, '0'))
        Assert.assertEquals("00123", leftFit("123", 5, '0'))
    }

    @Test
    fun testRightFit() {
        Assert.assertEquals("12345", rightFit("12345", 5, '0'))
        Assert.assertEquals("00012", rightFit("00012345", 5, '0'))
        Assert.assertEquals("12300", rightFit("123", 5, '0'))
    }

    @Test
    fun testRightCut() {
        Assert.assertEquals("a", rightCut("abcdef", 1))
        Assert.assertEquals("ab", rightCut("abcdef", 2))
        Assert.assertEquals("abc", rightCut("abcdef", 3))
        Assert.assertEquals("abcd", rightCut("abcdef", 4))
        Assert.assertEquals("abcde", rightCut("abcdef", 5))
        Assert.assertEquals("abcdef", rightCut("abcdef", 6))
        try {
            Assert.assertEquals("abcdef", rightCut("abcdef", 7))
            Assert.fail()
        } catch (e: StringIndexOutOfBoundsException) {

        }

    }

    @Test
    fun testleftCut() {
        Assert.assertEquals("", leftCut("abcdef", 0))
        Assert.assertEquals("f", leftCut("abcdef", 1))
        Assert.assertEquals("ef", leftCut("abcdef", 2))
        Assert.assertEquals("def", leftCut("abcdef", 3))
        Assert.assertEquals("cdef", leftCut("abcdef", 4))
        Assert.assertEquals("bcdef", leftCut("abcdef", 5))
        Assert.assertEquals("abcdef", leftCut("abcdef", 6))
        try {
            Assert.assertEquals("abcdef", leftCut("abcdef", 7))
            Assert.fail()
        } catch (e: StringIndexOutOfBoundsException) {

        }

    }

    @Test
    fun testRightFill() {
        Assert.assertEquals("abcd00", rightFill("abcd", 6, '0'))
        Assert.assertEquals("abc000", rightFill("abc", 6, '0'))
    }

    @Test
    fun testLeftFill() {
        Assert.assertEquals("00abcd", leftFill("abcd", 6, '0'))
        Assert.assertEquals("000abc", leftFill("abc", 6, '0'))
    }


}
