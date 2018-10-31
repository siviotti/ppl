package br.net.buzu.ppllib;

import static org.junit.Assert.*;

import br.net.buzu.ppllib.*;

import org.junit.Test;

public class TextTest {
	
	
	@Test
	public void testBlank(){
		assertTrue(isBlank(null));
		assertTrue(Text.INSTANCE.isBlank(""));
		assertTrue(Text.INSTANCE.isBlank("    "));
		assertTrue(Text.INSTANCE.isBlank("         "));
		
		assertFalse(Text.INSTANCE.isBlank("   0"));
		assertFalse(Text.INSTANCE.isBlank("_______"));
		assertFalse(Text.INSTANCE.isBlank("''''"));
	}
	
	@Test
	public void testLeftFit(){
		assertEquals("12345", Text.INSTANCE.leftFit("12345", 5, '0'));
		assertEquals("12345", Text.INSTANCE.leftFit("00012345", 5, '0'));
		assertEquals("00123", Text.INSTANCE.leftFit("123", 5, '0'));
	}

	@Test
	public void testRightFit(){
		assertEquals("12345", Text.INSTANCE.rightFit("12345", 5, '0'));
		assertEquals("00012", Text.INSTANCE.rightFit("00012345", 5, '0'));
		assertEquals("12300", Text.INSTANCE.rightFit("123", 5, '0'));
	}

	@Test
	public void testRightCut() {
		assertEquals("a", Text.INSTANCE.rightCut("abcdef", 1));
		assertEquals("ab", Text.INSTANCE.rightCut("abcdef", 2));
		assertEquals("abc", Text.INSTANCE.rightCut("abcdef", 3));
		assertEquals("abcd", Text.INSTANCE.rightCut("abcdef", 4));
		assertEquals("abcde", Text.INSTANCE.rightCut("abcdef", 5));
		assertEquals("abcdef", Text.INSTANCE.rightCut("abcdef", 6));
		try {
			assertEquals("abcdef", Text.INSTANCE.rightCut("abcdef", 7));
			fail();
		} catch (StringIndexOutOfBoundsException e) {

		}

	}

	@Test
	public void testleftCut() {
		assertEquals("", Text.INSTANCE.leftCut("abcdef", 0));
		assertEquals("f", Text.INSTANCE.leftCut("abcdef", 1));
		assertEquals("ef", Text.INSTANCE.leftCut("abcdef", 2));
		assertEquals("def", Text.INSTANCE.leftCut("abcdef", 3));
		assertEquals("cdef", Text.INSTANCE.leftCut("abcdef", 4));
		assertEquals("bcdef", Text.INSTANCE.leftCut("abcdef", 5));
		assertEquals("abcdef", Text.INSTANCE.leftCut("abcdef", 6));
		try {
			assertEquals("abcdef", Text.INSTANCE.leftCut("abcdef", 7));
			fail();
		} catch (StringIndexOutOfBoundsException e) {

		}
	}
	
	@Test
	public void testRightFill(){
		assertEquals("abcd00", Text.INSTANCE.rightFill("abcd", 6, '0'));
		assertEquals("abc000", Text.INSTANCE.rightFill("abc", 6, '0'));
	}

	@Test
	public void testLeftFill(){
		assertEquals("00abcd", Text.INSTANCE.leftFill("abcd", 6, '0'));
		assertEquals("000abc", Text.INSTANCE.leftFill("abc", 6, '0'));
	}

}
