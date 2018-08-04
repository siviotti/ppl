package br.net.buzu.pplspec.model;

import static org.junit.Assert.*;

import org.junit.Test;

import br.net.buzu.pplspec.lang.Syntax;

/**
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class DomainTest {

	private static final String BLUE = "Blue";
	private static final String _01_WHITE = "01=white";
	private static final String _02_BLACK = "02=black";
	private static final String _03_RED = "03=red";
	private static final String DOMAIN_NAME1 = "domainName1";
	private static final String DOMAIN_NAME2 = "domainName2";
	private static final String WHITE = "white";
	private static final String BLACK = "black";
	private static final String RED = "red";

	@Test
	public void testAnonymousSimple() {
		Domain domain = Domain.of(WHITE, BLACK, RED);
		assertEquals(3, domain.items().size());
		assertEquals(Syntax.EMPTY, domain.name());
		assertEquals(WHITE, domain.items().get(0).value());
		assertEquals(BLACK, domain.items().get(1).value());
		assertEquals(RED, domain.items().get(2).value());
		assertEquals("[\"white\",\"black\",\"red\"]", domain.asSerial());
		assertTrue(domain.containsValue(WHITE));
		assertTrue(domain.containsValue(BLACK));
		assertTrue(domain.containsValue(RED));
		assertFalse(domain.containsValue(BLUE));
	}

	@Test
	public void testAnonymousLabel() {
		Domain domain = Domain.of(_01_WHITE, _02_BLACK, _03_RED);
		assertEquals(3, domain.items().size());
		assertEquals(Syntax.EMPTY, domain.name());
		assertEquals(_01_WHITE, domain.items().get(0).asSerial());
		assertEquals(_02_BLACK, domain.items().get(1).asSerial());
		assertEquals(_03_RED, domain.items().get(2).asSerial());
		assertEquals(WHITE, domain.items().get(0).label());
		assertEquals(BLACK, domain.items().get(1).label());
		assertEquals(RED, domain.items().get(2).label());
		assertEquals("01", domain.items().get(0).value());
		assertEquals("02", domain.items().get(1).value());
		assertEquals("03", domain.items().get(2).value());
		assertEquals(1, domain.items().get(0).intValue());
		assertEquals(2, domain.items().get(1).intValue());
		assertEquals(3, domain.items().get(2).intValue());
		assertEquals("[\"01=white\",\"02=black\",\"03=red\"]", domain.asSerial());
		assertTrue(domain.containsValue("01"));
		assertTrue(domain.containsValue("02"));
		assertTrue(domain.containsValue("03"));
		assertFalse(domain.containsValue(WHITE));

	}

	@Test
	public void testEmptyOf() {
		Domain domain = Domain.of();
		assertEquals(0, domain.items().size());
		assertEquals(Syntax.EMPTY, domain.name());
		assertEquals(Syntax.EMPTY, domain.asSerial());
		assertEquals(Syntax.EMPTY, domain.toString());
	}

	@Test
	public void testNamed() {
		Domain domain1 = Domain.create(DOMAIN_NAME1, Domain.list(WHITE, BLACK, RED));
		Domain clone = Domain.create(DOMAIN_NAME1, Domain.list(WHITE, BLACK, RED));
		Domain domain2 = Domain.create(DOMAIN_NAME2, Domain.list(WHITE, BLACK, RED));
		Domain domain3 = Domain.create(DOMAIN_NAME1, Domain.list(WHITE, BLACK));
		assertEquals(3, domain1.items().size());
		assertEquals(DOMAIN_NAME1, domain1.name());
		assertEquals(WHITE, domain1.items().get(0).value());
		assertEquals(BLACK, domain1.items().get(1).value());
		assertEquals(RED, domain1.items().get(2).value());
		// Object
		assertEquals(domain1, clone);
		assertEquals(clone, domain1);
		assertFalse(domain1.equals(null));
		assertTrue(domain1.equals(domain1));
		assertFalse(domain1.equals("abc"));
		assertTrue(domain1.hashCode() == clone.hashCode());
		
		assertFalse(domain1.equals(domain2));
		assertFalse(domain1.name().equals(domain2.name()));
		assertTrue(domain1.name().equals(domain3.name()));
		assertFalse(domain1.equals(domain3));
		assertFalse(domain1.hashCode()== domain2.hashCode());
		assertFalse(domain1.toString().equals(domain2.toString()));
		assertTrue(domain1.equalsItems(domain2));
		assertFalse(domain1.equalsItems(domain3));
		
	}


}

