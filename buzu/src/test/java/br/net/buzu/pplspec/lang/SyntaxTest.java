package br.net.buzu.pplspec.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class SyntaxTest {
	
	private Syntax syntax;
	
	@Before
	public void before(){
		syntax = new Syntax();
	}
	

	@Test
	public void testIgnoreChar() {
		assertTrue(syntax.isIgnored(' '));
		assertTrue(syntax.isIgnored('\n'));
		assertTrue(syntax.isIgnored('\t'));

		assertFalse(syntax.isIgnored('A'));
		assertFalse(syntax.isIgnored('_'));
		assertFalse(syntax.isIgnored('2'));
	}

	@Test
	public void testValidFirstCharMetaName() {
		assertTrue(syntax.isValidFirstCharMetaName('A'));
		assertTrue(syntax.isValidFirstCharMetaName('a'));
		assertTrue(syntax.isValidFirstCharMetaName('_'));

		assertFalse(syntax.isValidFirstCharMetaName('-'));
		assertFalse(syntax.isValidFirstCharMetaName('2'));

	}

	@Test
	public void testValidCharMetaName() {
		assertTrue(syntax.isValidCharMetaName('A'));
		assertTrue(syntax.isValidCharMetaName('a'));
		assertTrue(syntax.isValidCharMetaName('_'));
		assertTrue(syntax.isValidCharMetaName('-'));
		assertTrue(syntax.isValidCharMetaName('-'));
		assertTrue(syntax.isValidCharMetaName('2'));

		assertFalse(syntax.isValidCharMetaName('+'));
		assertFalse(syntax.isValidCharMetaName('|'));

	}

	@Test
	public void testValidMetaName() {
		assertTrue(syntax.isValidMetaName("abc"));
		assertTrue(syntax.isValidMetaName("camelCase"));
		assertTrue(syntax.isValidMetaName("CAPS_CONST"));
		assertTrue(syntax.isValidMetaName("name-ok-20_X"));

		assertFalse(syntax.isValidMetaName(null));
		assertFalse(syntax.isValidMetaName(""));
		assertFalse(syntax.isValidMetaName("2abc"));
		assertFalse(syntax.isValidMetaName("abc&def"));
	}

	 @Test
	public void testPerformance() {
		String name = "name-test_ABC-25";
		long t0 = System.currentTimeMillis();
		int repeat = 1000000;
		for (int i = 0; i < repeat; i++) {
			Syntax.NAME_PATTERN.matcher(name).matches();
		}
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < repeat; i++) {
			syntax.isValidMetaName(name);
		}
		long t2 = System.currentTimeMillis();
	}

	@Test
	public void testRegex() {
		// Name
		assertTrue(Syntax.NAME_PATTERN.matcher("abc:").matches());
		assertTrue(Syntax.NAME_PATTERN.matcher("_abc:").matches());
		assertTrue(Syntax.NAME_PATTERN.matcher("abcXpto25:").matches());
		assertTrue(Syntax.NAME_PATTERN.matcher("Abc-Xpto_2:").matches());
		
		assertFalse(Syntax.NAME_PATTERN.matcher("").matches());
		assertFalse(Syntax.NAME_PATTERN.matcher(":").matches());
		assertFalse(Syntax.NAME_PATTERN.matcher("abc").matches());
		assertFalse(Syntax.NAME_PATTERN.matcher("2abcXpto25:").matches());
		assertFalse(Syntax.NAME_PATTERN.matcher("-abcXpto25:").matches());
		assertFalse(Syntax.NAME_PATTERN.matcher("abc def:").matches());

		// Type
		assertTrue(Syntax.TYPE_PATTERN.matcher("C").matches());
		assertTrue(Syntax.TYPE_PATTERN.matcher("String").matches());
		assertTrue(Syntax.TYPE_PATTERN.matcher("STR(etc * ç #2)").matches());
		
		assertFalse(Syntax.TYPE_PATTERN.matcher("").matches());
		assertFalse(Syntax.TYPE_PATTERN.matcher(" C").matches());
		assertFalse(Syntax.TYPE_PATTERN.matcher("VARCHAR2").matches());
		assertFalse(Syntax.TYPE_PATTERN.matcher("STR(etc * ç #2").matches());

		// Size
		assertTrue(Syntax.SIZE_PATTERN.matcher("25").matches());
		assertTrue(Syntax.SIZE_PATTERN.matcher("10,2").matches());
		
		assertFalse(Syntax.SIZE_PATTERN.matcher("").matches());
		assertFalse(Syntax.SIZE_PATTERN.matcher("25 ").matches());
		assertFalse(Syntax.SIZE_PATTERN.matcher(" 25").matches());
		assertFalse(Syntax.SIZE_PATTERN.matcher("10.2").matches());
		assertFalse(Syntax.SIZE_PATTERN.matcher("10,2,2").matches());

		// Occurs
		assertTrue(Syntax.OCCURS_PATTERN.matcher("25").matches());
		assertTrue(Syntax.OCCURS_PATTERN.matcher("2-10").matches());
		
		assertFalse(Syntax.OCCURS_PATTERN.matcher("").matches());
		assertFalse(Syntax.OCCURS_PATTERN.matcher("25 ").matches());
		assertFalse(Syntax.OCCURS_PATTERN.matcher(" 25").matches());
		assertFalse(Syntax.OCCURS_PATTERN.matcher("2,10").matches());
		assertFalse(Syntax.OCCURS_PATTERN.matcher("2:10").matches());

		// Meta
		assertTrue(Syntax.META_PATTERN.matcher(";").matches());
		assertTrue(Syntax.META_PATTERN.matcher("phone:S9#0-2;").matches());
		assertTrue(Syntax.META_PATTERN.matcher("birthday:D{yyyy-mm-dd};").matches());

	}

	@Test
	public void testEndIndex() throws ParseException {
		assertEquals(3, syntax.blockEndIndex("a{b}c", 1, '{', '}'));
		assertEquals(28, syntax.blockEndIndex("a( teste ( str ' xpto ) 'b)c) abc", 1, '(', ')'));
		assertEquals(28, syntax.blockEndIndex("a( teste ( str \" xpto ) \"b)c) abc", 1, '(', ')'));
		assertEquals(24, syntax.blockEndIndex("test(inside')'(go)\"x'y\"z)out", 4, '(', ')'));
		// 1 2 X 2 1

		try {
			assertEquals(3, syntax.blockEndIndex("a{b}c", 1, '(', ')'));
			fail(); // ( no lugar de {
		} catch (ParseException e) {
			assertTrue(e.getMessage().startsWith(Syntax.BLOCK_ERROR_INVALID_OPEN_CHAR));
		}
		assertEquals(12, syntax.blockEndIndex("xyz (abc(JJ)) def", 4, '(', ')'));
		assertEquals(12, syntax.blockEndIndex("2[34[4[5]ax]]", 1, '[', ']'));
		
		try {
			syntax.blockEndIndex("2[34[4[5]ax]", 1, '[', ']');
			fail(); // abrem 3 e fecham 2
		} catch (ParseException e) {
			assertTrue(e.getMessage().startsWith(Syntax.BLOCK_ERROR));
		}

		try {
			syntax.blockEndIndex("2[34[4[5]ax]", 1, '[', ']');
			fail(); // abrem 3 e fecham 2
		} catch (ParseException e) {
			assertTrue(e.getMessage().startsWith(Syntax.BLOCK_ERROR));
		}

		try {
			syntax.blockEndIndex("abc ( 'def) xyz", 4, '(', ')');
			fail(); // opened String
		} catch (ParseException e) {
			assertTrue(e.getMessage().startsWith(Syntax.UNTERMINATED_STRING));
		}
	}
	
	@Test
	public void testStringEndIndex() throws ParseException {
		assertEquals(5, syntax.nextStringDelimeter("'1234'", 0, '\''));
		assertEquals(8, syntax.nextStringDelimeter("test 'ok' test", 5, '\''));
		assertEquals(16, syntax.nextStringDelimeter("test 'ok \"inner\"' test", 5, '\''));
	}
	
	@Test
	public void testStringExtract() throws ParseException {
		assertEquals("'1234'", syntax.extractString("'1234'", 0, '\''));
		assertEquals("'ok'", syntax.extractString("test 'ok' test", 5, '\''));
		assertEquals("'ok \"inner\"'", syntax.extractString("test 'ok \"inner\"' test", 5, '\''));
	}
	
	@Test
	public void testIsString() throws ParseException {
		assertTrue(syntax.isString("'123'"));
		assertTrue(syntax.isString("\"123\""));
		assertTrue(syntax.isString("'123 \" 321'"));
		
		assertFalse(syntax.isString("'123"));
		assertFalse(syntax.isString(" '123'"));
		assertFalse(syntax.isString("'123' "));
		assertFalse(syntax.isString("123'"));
		assertFalse(syntax.isString("'123\""));
		assertFalse(syntax.isString("\"123'"));
	}
	
	@Test
	public void testFirstChar() throws ParseException {
		assertEquals(0, syntax.firstChar("", 0));
		assertEquals(2, syntax.firstChar("   ", 0));
		assertEquals(3, syntax.firstChar("   color:", 0));
		assertEquals(3, syntax.firstChar("\n\n\ncolor:", 0));
		assertEquals(3, syntax.firstChar("\t\t\tcolor:", 0));
		assertEquals(3, syntax.firstChar("\n \tcolor:", 0));
	}
	
	@Test
	public void testLastNumberIndex() {
		assertEquals(10, syntax.lastNumberIndex("NAME:STR20", 8, Token.DECIMAL_SEP));
		assertEquals(13, syntax.lastNumberIndex("NAME:STR20,02", 8, Token.DECIMAL_SEP));
		assertEquals(13, syntax.lastNumberIndex("NAME:STR20,02*", 8, Token.DECIMAL_SEP));
		assertEquals(12, syntax.lastNumberIndex("NAME:STR20,2,3", 8, Token.DECIMAL_SEP));
		assertEquals(4, syntax.lastNumberIndex("#1-5", 1, Token.OCCURS_RANGE));
		assertEquals(14, syntax.lastNumberIndex("name:STR20#1-5", 11, Token.OCCURS_RANGE));
		assertEquals(14, syntax.lastNumberIndex("name:STR20#1-5xpto", 11, Token.OCCURS_RANGE));
		assertEquals(14, syntax.lastNumberIndex("name:STR20#1-5-2", 11, Token.OCCURS_RANGE));
		assertEquals(15, syntax.lastNumberIndex("name:STR20#1-55", 11, Token.OCCURS_RANGE));

	}


	@Test
	public void testAsPpl() {
		Syntax syntax = new Syntax();
		String metadata = "name:S7;age:I2";
		String metadata2 = "(name:S7;age:I2)";
		String payload = "Ladybug15";
		assertEquals("(" + metadata + ")" + payload, syntax.asPplString(metadata, payload));
		assertEquals("(" + metadata + ")" + payload, syntax.asPplString(metadata2, payload));
	}

}
