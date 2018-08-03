package br.net.buzu.pplspec.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.model.PplString;

/**
 * PplString unit test
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class PplStringTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNull() {
		new PplString(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmpty() {
		new PplString("");
	}

	@Test(expected = PplParseException.class)
	public void testOpenSub() {
		new PplString("(");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullContext() {
		new PplString("(zzz)abc", null);
	}
	
	@Test(expected = PplParseException.class)
	public void testNoMetadata() {
		new PplString("Ladybug   015Paris");
	}

	@Test
	public void testEmptyMetadata() {
		Syntax syntax = new Syntax();
		PplString pplString = new PplString("()Ladybug   015Paris", syntax);
		assertEquals("()", pplString.getMetadata());
		assertEquals("Ladybug   015Paris", pplString.getPayload());
		assertEquals(syntax, pplString.getSyntax());
	}

	@Test
	public void testInvalidMetadata() {
		PplString pplString = new PplString("(%&?)Ladybug   015Paris");
		assertEquals("(%&?)", pplString.getMetadata());
		assertEquals("Ladybug   015Paris", pplString.getPayload());
	}

	@Test(expected = PplParseException.class)
	public void testUnclosedMetadata() {
		new PplString("(name: S10; age: I3; city:S5 Ladybug   015Paris");
	}

	@Test
	public void testEmptyPayload() {
		PplString pplString = new PplString("(name: S10; age: I3; city:S5)");
		assertEquals("(name: S10; age: I3; city:S5)", pplString.getMetadata());
		assertEquals("", pplString.getPayload());
	}

	@Test
	public void test() {
		PplString pplString1 = new PplString("(name: S10; age: I3; city:S5)Ladybug   015Paris");
		assertEquals("(name: S10; age: I3; city:S5)", pplString1.getMetadata());
		assertEquals("Ladybug   015Paris", pplString1.getPayload());
		assertEquals(Syntax.class, pplString1.getSyntax().getClass());
		
		PplString pplString2 = new PplString("(name: S10; age: I3; city:S5)Catnoir   015Paris");
		assertFalse(pplString1.equals(pplString2));
		PplString pplString3 = new PplString("(name: S7; age: I3; city:S5)Ladybug015Paris");
		assertFalse(pplString1.equals(pplString3));
	}

	@Test
	public void testEqualsHashodeToString() {
		PplString pplString1 = new PplString("(name: S10; age: I3; city:S5)Ladybug   015Paris");
		PplString pplString2 = new PplString("(name: S10; age: I3; city:S5)Ladybug   015Paris");
		assertFalse(pplString1.equals(null));
		Object o = new String("abc");
		assertFalse(pplString1.equals(o));
		assertTrue(pplString1.equals(pplString1));
		assertEquals(pplString1, pplString2);
		assertEquals(pplString1.hashCode(), pplString2.hashCode());
		assertEquals(pplString1.toString(), pplString2.toString());
		assertEquals("(name: S10; age: I3; city:S5)Ladybug   015Paris", pplString1.toString());
	}

	

}
