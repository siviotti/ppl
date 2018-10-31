package br.net.buzu.ppllib;

import static org.junit.Assert.*;
import static br.net.buzu.ppllib.TextKt.*;


import org.junit.Test;


public class TextTest {
	
	
	@Test
	public void testBlank(){
		assertTrue(isBlank(null));
		assertTrue(isBlank(""));
		assertTrue(isBlank("    "));
		assertTrue(isBlank("         "));
		
		assertFalse(isBlank("   0"));
		assertFalse(isBlank("_______"));
		assertFalse(isBlank("''''"));
	}
	
	@Test
	public void testLeftFit(){
		assertEquals("12345", leftFit("12345", 5, '0'));
		assertEquals("12345", leftFit("00012345", 5, '0'));
		assertEquals("00123", leftFit("123", 5, '0'));
	}

	@Test
	public void testRightFit(){
		assertEquals("12345", rightFit("12345", 5, '0'));
		assertEquals("00012", rightFit("00012345", 5, '0'));
		assertEquals("12300", rightFit("123", 5, '0'));
	}

	@Test
	public void testRightCut() {
		assertEquals("a", rightCut("abcdef", 1));
		assertEquals("ab", rightCut("abcdef", 2));
		assertEquals("abc", rightCut("abcdef", 3));
		assertEquals("abcd", rightCut("abcdef", 4));
		assertEquals("abcde", rightCut("abcdef", 5));
		assertEquals("abcdef", rightCut("abcdef", 6));
		try {
			assertEquals("abcdef", rightCut("abcdef", 7));
			fail();
		} catch (StringIndexOutOfBoundsException e) {

		}

	}

	@Test
	public void testleftCut() {
		assertEquals("", leftCut("abcdef", 0));
		assertEquals("f", leftCut("abcdef", 1));
		assertEquals("ef", leftCut("abcdef", 2));
		assertEquals("def", leftCut("abcdef", 3));
		assertEquals("cdef", leftCut("abcdef", 4));
		assertEquals("bcdef", leftCut("abcdef", 5));
		assertEquals("abcdef", leftCut("abcdef", 6));
		try {
			assertEquals("abcdef", leftCut("abcdef", 7));
			fail();
		} catch (StringIndexOutOfBoundsException e) {

		}
	}
	
	@Test
	public void testRightFill(){
		assertEquals("abcd00", rightFill("abcd", 6, '0'));
		assertEquals("abc000", rightFill("abc", 6, '0'));
	}

	@Test
	public void testLeftFill(){
		assertEquals("00abcd", leftFill("abcd", 6, '0'));
		assertEquals("000abc", leftFill("abc", 6, '0'));
	}

}
