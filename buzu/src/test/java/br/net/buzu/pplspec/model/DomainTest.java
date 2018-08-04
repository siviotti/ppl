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
	private static final String DOMAIN_NAME = "domainName";
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
		Domain domain = Domain.create(DOMAIN_NAME, Domain.list(WHITE, BLACK, RED));
		assertEquals(3, domain.items().size());
		assertEquals(DOMAIN_NAME, domain.name());
		assertEquals(WHITE, domain.items().get(0).value());
		assertEquals(BLACK, domain.items().get(1).value());
		assertEquals(RED, domain.items().get(2).value());
	}


}
