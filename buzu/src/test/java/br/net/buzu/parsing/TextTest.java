package br.net.buzu.parsing;

import static org.junit.Assert.*;

import org.junit.Test;

import br.net.buzu.parsing.Text;

public class TextTest {
	
	
	@Test
	public void testBlank(){
		assertTrue(Text.isBlank(null));
		assertTrue(Text.isBlank(""));
		assertTrue(Text.isBlank("    "));
		assertTrue(Text.isBlank("         "));
		
		assertFalse(Text.isBlank("   0"));
		assertFalse(Text.isBlank("_______"));
		assertFalse(Text.isBlank("''''"));
	}
	
	@Test
	public void testLeftFit(){
		assertEquals("12345", Text.leftFit("12345", 5, '0'));
		assertEquals("12345", Text.leftFit("00012345", 5, '0'));
		assertEquals("00123", Text.leftFit("123", 5, '0'));
	}

	@Test
	public void testRightFit(){
		assertEquals("12345", Text.rightFit("12345", 5, '0'));
		assertEquals("00012", Text.rightFit("00012345", 5, '0'));
		assertEquals("12300", Text.rightFit("123", 5, '0'));
	}

	@Test
	public void testRightCut() {
		assertEquals("a", Text.rightCut("abcdef", 1));
		assertEquals("ab", Text.rightCut("abcdef", 2));
		assertEquals("abc", Text.rightCut("abcdef", 3));
		assertEquals("abcd", Text.rightCut("abcdef", 4));
		assertEquals("abcde", Text.rightCut("abcdef", 5));
		assertEquals("abcdef", Text.rightCut("abcdef", 6));
		try {
			assertEquals("abcdef", Text.rightCut("abcdef", 7));
			fail();
		} catch (StringIndexOutOfBoundsException e) {

		}

	}

	@Test
	public void testleftCut() {
		assertEquals("", Text.leftCut("abcdef", 0));
		assertEquals("f", Text.leftCut("abcdef", 1));
		assertEquals("ef", Text.leftCut("abcdef", 2));
		assertEquals("def", Text.leftCut("abcdef", 3));
		assertEquals("cdef", Text.leftCut("abcdef", 4));
		assertEquals("bcdef", Text.leftCut("abcdef", 5));
		assertEquals("abcdef", Text.leftCut("abcdef", 6));
		try {
			assertEquals("abcdef", Text.leftCut("abcdef", 7));
			fail();
		} catch (StringIndexOutOfBoundsException e) {

		}
	}
	
	@Test
	public void testRightFill(){
		assertEquals("abcd00", Text.rightFill("abcd", 6, '0'));
		assertEquals("abc000", Text.rightFill("abc", 6, '0'));
	}

	@Test
	public void testLeftFill(){
		assertEquals("00abcd", Text.leftFill("abcd", 6, '0'));
		assertEquals("000abc", Text.leftFill("abc", 6, '0'));
	}

}
