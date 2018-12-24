package br.net.buzu.metadata.build.parse;

import br.net.buzu.lang.Syntax;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class ParseNodeTest {
	
	

	@Test
	public void testEmpty() {
		ParseNode node = new ParseNode();
		assertEquals(Syntax.EMPTY, node.name);
		assertEquals(Syntax.EMPTY, node.type);
		assertEquals(Syntax.EMPTY, node.size);
		assertEquals(Syntax.EMPTY, node.occurs);
		assertEquals(Syntax.EMPTY, node.domain);
		assertEquals(Syntax.EMPTY, node.defaultValue);
		assertEquals(Syntax.EMPTY, node.tags);
		assertFalse(node.hasExtension());
		assertFalse(node.hasName());
		assertFalse(node.hasOccurs());
		assertFalse(node.hasSize());
		assertFalse(node.hasType());
		assertEquals(null, node.children);
	}

	@Test
	public void testSimple() {
		ParseNode node = new ParseNode("name", "S", "10", "0-1", null);
		assertTrue(node.toString().startsWith("name:S10#0-1"));
		assertTrue(node.toTree(0).contains("name"));
	}
	
	@Test
	public void testComplex(){
		ParseNode name = new ParseNode("name", "S", "7", "0-1", null);
		ParseNode age = new ParseNode("age", "S", "2", "0-1",  null);
		ParseNode city = new ParseNode("city", "S", "5", "0-1", null);
		List<ParseNode> children = new ArrayList<>();
		children.add(name);
		children.add(age);
		children.add(city);
		ParseNode person = new ParseNode("person", "", "", "1-1", children);
		//assertEquals("person:(name:S7#0-1;age:S2#0-1;city:S5#0-1;)#1-1", person.toString());
		assertEquals(3, person.children.size());
		assertTrue(person.toString().startsWith("person"));
		assertTrue(person.toString().contains("name"));
		assertTrue(person.toString().contains("age"));
		assertTrue(person.toString().contains("city"));
		assertTrue(person.toTree(0).contains("person"));
		assertTrue(person.toTree(0).contains("name"));
		assertTrue(person.toTree(0).contains("age"));
		assertTrue(person.toTree(0).contains("city"));
	}

}
