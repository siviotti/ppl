/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.lib

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.fail


/**
 * @author Douglas Siviotti
 */
class TextTest {


    @Test
    fun testLeftFit() {
        assertEquals("12345", leftFit("12345", 5, '0'))
        assertEquals("12345", leftFit("00012345", 5, '0'))
        assertEquals("00123", leftFit("123", 5, '0'))
    }

    @Test
    fun testRightFit() {
        assertEquals("12345", rightFit("12345", 5, '0'))
        assertEquals("00012", rightFit("00012345", 5, '0'))
        assertEquals("12300", rightFit("123", 5, '0'))
    }

    @Test
    fun testRightCut() {
        assertEquals("a", rightCut("abcdef", 1))
        assertEquals("ab", rightCut("abcdef", 2))
        assertEquals("abc", rightCut("abcdef", 3))
        assertEquals("abcd", rightCut("abcdef", 4))
        assertEquals("abcde", rightCut("abcdef", 5))
        assertEquals("abcdef", rightCut("abcdef", 6))
        try {
            assertEquals("abcdef", rightCut("abcdef", 7))
            fail("should not enter here")
        } catch (e: StringIndexOutOfBoundsException) {

        }

    }

    @Test
    fun testleftCut() {
        assertEquals("", leftCut("abcdef", 0))
        assertEquals("f", leftCut("abcdef", 1))
        assertEquals("ef", leftCut("abcdef", 2))
        assertEquals("def", leftCut("abcdef", 3))
        assertEquals("cdef", leftCut("abcdef", 4))
        assertEquals("bcdef", leftCut("abcdef", 5))
        assertEquals("abcdef", leftCut("abcdef", 6))
        try {
            assertEquals("abcdef", leftCut("abcdef", 7))
            fail("should not enter here")
        } catch (e: StringIndexOutOfBoundsException) {

        }

    }

    @Test
    fun testRightFill() {
        assertEquals("abcd00", rightFill("abcd", 6, '0'))
        assertEquals("abc000", rightFill("abc", 6, '0'))
    }

    @Test
    fun testLeftFill() {
        assertEquals("00abcd", leftFill("abcd", 6, '0'))
        assertEquals("000abc", leftFill("abc", 6, '0'))
    }


}
