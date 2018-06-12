package br.net.buzu.metadata.build.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.lang.Syntax;

/**
 * 
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class SplitterTest {

	@Test
	public void testNew() {
		Splitter splitter = new Splitter();
		assertEquals(Syntax.class, splitter.getSyntax().getClass());
		Syntax alternativeSyntax = new Syntax() {
			private static final long serialVersionUID = 1L;
		};
		Splitter splitter2 = new Splitter(alternativeSyntax);
		assertNotSame(Syntax.class, splitter2.getSyntax().getClass());
	}

	@Test
	public void testExtractName() throws ParseException {
		Splitter splitter = new Splitter();
		
		ParseNode node = new ParseNode();
		assertEquals(5, splitter.extractName("xpto:INT", 0, node));
		assertEquals("xpto", node.name);

		//assertEquals(5, splitter.extractName("'abc' xpto:INT", node));
		//assertEquals("xpto", node.name);
		node = new ParseNode();
		assertEquals(7, splitter.extractName("Xpto25:INT", 0, node));
		assertEquals("Xpto25", node.name);

		node = new ParseNode();
		assertEquals(0, splitter.extractName("INT", 0, node));
		assertEquals("", node.name);

		node = new ParseNode();
		assertEquals(1, splitter.extractName(":INT", 0, node));
		assertEquals("", node.name);

		node = new ParseNode();
		assertEquals(0, splitter.extractName("xpto", 0, node));
		assertEquals("", node.name);

		// Ignored chars before name
		node = new ParseNode();
		assertEquals(8, splitter.extractName("   xpto:INT", 0, node));
		assertEquals("xpto", node.name);

		// $name $ = var
		node = new ParseNode();
		assertEquals(9, splitter.extractName("   $xpto:INT", 0, node));
		assertEquals("$xpto", node.name);

		node = new ParseNode();
		assertEquals(8, splitter.extractName("\n\n\nxpto:INT", 0, node));
		assertEquals("xpto", node.name);

		node = new ParseNode();
		assertEquals(8, splitter.extractName("\t\t\txpto:INT", 0, node));
		assertEquals("xpto", node.name);

		node = new ParseNode();
		assertEquals(2, splitter.extractName("   ", 0, node));
		assertEquals("", node.name);

	}

	@Test
	public void testExtractType() throws ParseException {
		Splitter splitter = new Splitter();
		ParseNode node = new ParseNode();

		node = new ParseNode();
		assertEquals(3, splitter.extractType("    ", 0, node));
		assertEquals("", node.getType());

		node = new ParseNode();
		assertEquals(3, splitter.extractType("STR2abc", 0, node));
		assertEquals("STR", node.getType());

		node = new ParseNode();
		assertEquals(6, splitter.extractType("   STR2abc", 0, node));
		assertEquals("STR", node.getType());

		node = new ParseNode();
		assertEquals(8, splitter.extractType("name:STR20", 5, node));
		assertEquals("STR", node.getType());

		node = new ParseNode();
		assertEquals(9, splitter.extractType("abcSTRabc", 0, node));
		assertEquals("abcSTRabc", node.getType());

		node = new ParseNode();
		assertEquals(8, splitter.extractType("NAME:STR20", 5, node));
		assertEquals("STR", node.getType());

		node = new ParseNode();
		assertEquals(31, splitter.extractType("R(xx:INT5,2#1-5*{@@@};yy:INT5;)*{prop}", 0, node));
		assertEquals("R", node.getType());

		node = new ParseNode();
		assertEquals(30, splitter.extractType("(xx:INT5,2#1-5*{@@@};yy:INT5;)*{prop}", 0, node));
		assertEquals(Syntax.EMPTY, node.getType());

		// Ignored chars before getType()
		node = new ParseNode();
		assertEquals(6, splitter.extractType("   STR2abc", 0, node));
		assertEquals("STR", node.getType());

	}


	@Test
	public void testExtractSize() throws ParseException {
		Splitter splitter = new Splitter();
		ParseNode node = new ParseNode();

		node = new ParseNode();
		assertEquals(2, splitter.extractSize("20", 0, node));
		assertEquals("20", node.getSize());

		node = new ParseNode();
		assertEquals(4, splitter.extractSize("  20  ", 0, node));
		assertEquals("20", node.getSize());

		node = new ParseNode();
		assertEquals(10, splitter.extractSize("NAME:STR20", 8, node));
		assertEquals("20", node.getSize());

		node = new ParseNode();
		assertEquals(6, splitter.extractSize("NAME:D{yyyy-MM-dd}", 6, node));
		assertEquals("", node.getSize());

		node = new ParseNode();
		assertEquals(13, splitter.extractSize("NAME:STR20,02", 8, node));
		assertEquals("20,02", node.getSize());

		node = new ParseNode();
		assertEquals(13, splitter.extractSize("NAME:STR20,02*", 8, node));
		assertEquals("20,02", node.getSize());

		node = new ParseNode();
		assertEquals(12, splitter.extractSize("NAME:STR20,2,3", 8, node));
		assertEquals("20,2", node.getSize());
	}

	@Test
	public void testExtractOccurs() {
		Splitter splitter = new Splitter();
		ParseNode node = new ParseNode();

		node = new ParseNode();
		assertEquals(0, splitter.extractOccurs("1-7", 0, node));
		assertEquals("", node.getOccurs());

		node = new ParseNode();
		assertEquals(4, splitter.extractOccurs("#1-5", 0, node));
		assertEquals("1-5", node.getOccurs());

		node = new ParseNode();
		assertEquals(7, splitter.extractOccurs("   #1-5   ", 0, node));
		assertEquals("1-5", node.getOccurs());

		node = new ParseNode();
		assertEquals(14, splitter.extractOccurs("name:STR20#1-5", 10, node));
		assertEquals("1-5", node.getOccurs());

		node = new ParseNode();
		assertEquals(14, splitter.extractOccurs("name:STR20#1-5xpto", 10, node));
		assertEquals("1-5", node.getOccurs());

		node = new ParseNode();
		assertEquals(14, splitter.extractOccurs("name:STR20#1-5-2", 10, node));
		assertEquals("1-5", node.getOccurs());

		node = new ParseNode();
		assertEquals(15, splitter.extractOccurs("name:STR20#1-55", 10, node));
		assertEquals("1-55", node.getOccurs());
	}

	@Test
	public void testExtractDomain() throws ParseException {
		Splitter splitter = new Splitter();
		ParseNode node;

		node = new ParseNode();
		assertEquals(17, splitter.extractDomain("['black','white']", 0, node));
		assertEquals("['black','white']", node.getDomain());

		node = new ParseNode();
		assertEquals(20, splitter.extractDomain("   ['black','white']", 0, node));
		assertEquals("['black','white']", node.getDomain());

		node = new ParseNode();
		assertEquals(20, splitter.extractDomain("   ['black','white']    ", 0, node));
		assertEquals("['black','white']", node.getDomain());

		node = new ParseNode();
		assertEquals(3, splitter.extractDomain("   bla", 0, node));
		assertEquals(Syntax.EMPTY, node.getDomain());

		node = new ParseNode();
		assertEquals(30, splitter.extractDomain("field:S10#1-3['black','white']", 13, node));
		assertEquals("['black','white']", node.getDomain());

		try {
			node = new ParseNode();
			assertEquals(17, splitter.extractDomain("['black','white'", 0, node));
			fail();
		} catch (ParseException e) {

		}

	}

	@Test
	public void testExtractDefaultvalue() throws ParseException {
		Splitter splitter = new Splitter();
		ParseNode node;

		node = new ParseNode();
		assertEquals(9, splitter.extractDefaultvalue("='black'", 0, node));
		assertEquals("'black'", node.getDefaultValue());

		node = new ParseNode();
		assertEquals(13, splitter.extractDefaultvalue("    ='black'", 0, node));
		assertEquals("'black'", node.getDefaultValue());

		node = new ParseNode();
		assertEquals(13, splitter.extractDefaultvalue("    ='55555'", 0, node));
		assertEquals("'55555'", node.getDefaultValue());

		node = new ParseNode();
		assertEquals(13, splitter.extractDefaultvalue("    ='black'    ", 0, node));
		assertEquals("'black'", node.getDefaultValue());

		node = new ParseNode();
		assertEquals(3, splitter.extractDefaultvalue("   bla", 0, node));
		assertEquals(Syntax.EMPTY, node.getDefaultValue());
		
		assertEquals(0, splitter.extractDefaultvalue("=", 0, node));
		assertEquals(8, splitter.extractDefaultvalue("=black'", 0, node));

		node = new ParseNode();
		try {
			assertEquals(8, splitter.extractDefaultvalue("='black", 0, node));
			fail();
		} catch (ParseException e) {
			assertTrue(e.getMessage().startsWith(Syntax.UNTERMINATED_STRING));
		}
		
		try {
			assertEquals(8, splitter.extractDefaultvalue("='", 0, node));
			fail();
		} catch (ParseException e) {
			assertTrue(e.getMessage().startsWith(Syntax.UNTERMINATED_STRING));
		}


		


	}

	@Test
	public void testExtractTags() {
		Splitter splitter = new Splitter();
		assertEquals("1-5", splitter.extractTags("1-5", 0));
		assertEquals("'teste'", splitter.extractTags("'teste'", 0));
		assertEquals("'teste' abc ';'", splitter.extractTags("'teste' abc ';'", 0));
		assertEquals("(20)", splitter.extractTags("(20)", 0));
		assertEquals("tags", splitter.extractTags("tags", 0));
		assertEquals("tags", splitter.extractTags(" tags", 0));
		assertEquals("tags", splitter.extractTags(" tags ", 0));
		assertEquals("tags", splitter.extractTags("    tags     ", 0));
		assertEquals("A B C", splitter.extractTags(" A B C ", 0));
		assertEquals("A  B  C", splitter.extractTags("  A  B  C  ", 0));
		assertEquals("A\n\t B\t\n C", splitter.extractTags("\n\t  A\n\t B\t\n C \t\n", 0));
	}

	@Test
	public void testSplitMeta() {
		// Minimum
		assertMetaChildren("(20)", "", "", "", "", "", 1);
		assertMeta("20", "", "", "20", "", "");
		assertMeta("onlyName:", "onlyName", "", "", "", "");
		assertMeta(":20", "", "", "20", "", "");
		// Basic
		assertMeta("age:INT3", "age", "INT", "3", "", "");
		assertMeta("age:INT3#", "age", "INT", "3", "", "");
		assertMeta("age: INT 3", "age", "INT", "3", "", "");
		assertMeta("birthday:D'extension'", "birthday", "D", "", "", "'extension'");
		assertMeta("hour:T#1-2{hh:mm:ss}", "hour", "T", "", "1-2", "{hh:mm:ss}");
		assertMeta("smoke:BYN", "smoke", "BYN", "", "", "");
		// Complete
		assertMeta("price:NUM10,2#1-2dolar", "price", "NUM", "10,2", "1-2", "dolar");
		assertMeta("price: NUM 10,2 #1-2 tags", "price", "NUM", "10,2", "1-2", "tags");
		assertMeta("price:\n NUM\n 10,2\n #1-2 A B C", "price", "NUM", "10,2", "1-2", "A B C");
		assertMeta("price:\t NUM\t 10,2\t #1-2 A B C ", "price", "NUM", "10,2", "1-2", "A B C");
		assertMeta("price:    NUM    10,2    #1-2 dolar", "price", "NUM", "10,2", "1-2", "dolar");

		assertMeta("price: NUM 10,2 # dolar", "price", "NUM", "10,2", "", "dolar");
		assertMeta(" price:NUM10,2#1-2dolar", "price", "NUM", "10,2", "1-2", "dolar");
		assertMeta(" price: NUM10,2#1-2 dolar", "price", "NUM", "10,2", "1-2", "dolar");
		assertMeta("\nprice:NUM10,2#1-2dolar", "price", "NUM", "10,2", "1-2", "dolar");
		// Extension (domain, defaultValue and tags)
		assertMetaExt("color: S 10 ['White','Red', 'Green']", "color", "S", "10", "", "['White','Red', 'Green']", "",
				"");
		assertMetaExt("color: S 10 ['White','Red', 'Green'] ='def' A B C", "color", "S", "10", "",
				"['White','Red', 'Green']", "'def'", "A B C");
		// Complex
		assertMetaChildren("R(f1:STR10;f2:ST20)#2-4dolar", "", "R", "", "2-4", "dolar", 2);
		assertMetaChildren("(f1:STR10;f2:ST20)#2-4dolar", "", "", "", "2-4", "dolar", 2);

		// Bad
		assertMeta("price : NUM 10,2 #1-2 dolar", "", "price", "", "", ": NUM 10,2 #1-2 dolar");

	}

	@Test
	public void testSplitMetaError() {

	}

	private void assertMeta(String metaStr, String name, String type, String size, String occurs, String tags) {
		Splitter splitter = new Splitter();
		ParseNode node = splitter.splitMeta(metaStr);
		assertEquals(name, node.getName());
		assertEquals(type, node.getType());
		assertEquals(size, node.getSize());
		assertEquals(occurs, node.getOccurs());
		assertEquals(tags, node.getTags());
	}

	private void assertMetaExt(String metaStr, String name, String type, String size, String occurs, String domain,
			String defaultValue, String tags) {
		Splitter splitter = new Splitter();
		ParseNode node = splitter.splitMeta(metaStr);
		assertEquals(name, node.getName());
		assertEquals(type, node.getType());
		assertEquals(size, node.getSize());
		assertEquals(occurs, node.getOccurs());
		assertEquals(domain, node.getDomain());
		assertEquals(defaultValue, node.getDefaultValue());
		assertEquals(tags, node.getTags());
	}

	private void assertMetaChildren(String metaStr, String name, String type, String size, String occurs, String tags,
			int childrenCount) {
		Splitter splitter = new Splitter();
		ParseNode node = splitter.splitMeta(metaStr);
		assertEquals(name, node.getName());
		assertEquals(type, node.getType());
		assertEquals(size, node.getSize());
		assertEquals(occurs, node.getOccurs());
		assertEquals(childrenCount, node.children.size());
		assertEquals(tags, node.getTags());
	}

	@Test
	public void testSplitLayout() throws ParseException {
		Splitter splitter = new Splitter();
		List<ParseNode> metas = splitter.splitLayout("price:NUM10,2#1-2dolar");
		assertEquals(1, metas.size());
		ParseNode node = metas.get(0);
		assertEquals("price", node.getName());
		assertEquals("NUM", node.getType());
		assertEquals("10,2", node.getSize());
		assertEquals("1-2", node.getOccurs());
		assertEquals("dolar", node.getTags());

		metas = splitter.splitLayout("a:STR5;b:INT3#0-9*prop;c:NUM9;");
		metas = splitter.splitLayout("a:STR5;b:R(x:STr2;y:STR3)3#0-9*prop;c:NUM9");
	}

	@Test
	public void testCheckStr() throws ParseException{
		Splitter splitter = new Splitter();

		List<ParseNode> list = splitter.splitLayout("price:NUM10,2#1-2['dolar','euro']");
		ParseNode node = list.get(0);
		assertEquals("['dolar','euro']", node.getDomain());
		list = splitter.splitLayout("price:NUM10,2#1-2[\"dolar\",\"euro\"]");
		node = list.get(0);
		assertEquals("[\"dolar\",\"euro\"]", node.getDomain());
		try {
			splitter.splitLayout("price:NUM10,2#1-2{\"dolar,\"euro\"}");
			fail();
		} catch (ParseException ple) {
			assertTrue(ple.getMessage().startsWith(Syntax.UNTERMINATED_STRING));
		}
		try {
			splitter.splitLayout("price:NUM10,2#1-2{\"dolar\"\",\"euro\"}");
			fail();
		} catch (ParseException ple) {
			assertTrue(ple.getMessage().startsWith(Syntax.UNTERMINATED_STRING));
		}
		try {
			splitter.splitLayout("price:NUM10,2#1-2{'dolar,'euro'}");
			fail();
		} catch (ParseException ple) {
			assertTrue(ple.getMessage().startsWith(Syntax.UNTERMINATED_STRING));
		}
		try {
			splitter.splitLayout("price:NUM10,2#1-2{'dolar'','euro'}");
			fail();
		} catch (ParseException ple) {
			assertTrue(ple.getMessage().startsWith(Syntax.UNTERMINATED_STRING));
		}

	}

	@Test
	public void testCheckBlock() throws ParseException{
		Splitter splitter = new Splitter();
		List<ParseNode> list = splitter.splitLayout("price:NUM10,2#1-2{\"dolar\",\"euro\"}");
		ParseNode node = list.get(0);
		assertEquals("{\"dolar\",\"euro\"}", node.getTags());
		list = splitter.splitLayout("price:NUM10,2#1-2['dolar','euro']");
		node = list.get(0);
		assertEquals("['dolar','euro']", node.getDomain());
		try {
			splitter.splitLayout("price:NUM10,2#1-2{'dolar','euro'");
			fail();
		} catch (PplParseException ple) {
			assertTrue(ple.getMessage().startsWith(Splitter.WRONG_USE_OF_BLOCK));
		}
		try {
			splitter.splitLayout("price:NUM10,2#1-2['dolar','euro'");
			fail();
		} catch (PplParseException ple) {
			assertTrue(ple.getMessage().startsWith(Splitter.WRONG_USE_OF_BLOCK));
		}
		try {
			splitter.splitLayout("price:(20;30;40");
			fail();
		} catch (PplParseException ple) {
			assertTrue(ple.getMessage().startsWith(Splitter.WRONG_USE_OF_BLOCK));
		}
		try {
			splitter.splitLayout("price:(20;30;40))");
			fail();
		} catch (PplParseException ple) {
			assertTrue(ple.getMessage().startsWith(Splitter.WRONG_USE_OF_BLOCK));
		}
	}

	@Test
	public void testSplitLayoutComplex() throws ParseException{
		Splitter splitter = new Splitter();
		List<ParseNode> metas = splitter.splitLayout(
				"(a:STR5; b:R(x: STR2; y: STR3; z:R(xx:INT5,2#1-5*{@@@};yy:INT5)*{prop})3#0-9*{%%%};c:NUM9)");
		ParseNode root = metas.get(0);
		assertEquals(3, root.children.size());
	}

}
