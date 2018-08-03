package br.net.buzu.pplspec.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.net.buzu.pplspec.model.DomainItem;

/**
 * 
 * @author Douglas Siviotti
 * @since 1.0
 */
public class DomainItemTest {

	@Test
	public void testNull() {
		DomainItem item = DomainItem.parse(null);
		assertEquals(null, item);
	}

	@Test
	public void testBlank() {
		DomainItem item = DomainItem.parse("");
		assertEquals("", item.value());
		assertEquals(null, item.label());
	}

	@Test
	public void testValueLabel() {
		DomainItem item = DomainItem.parse("01=abc");
		assertEquals("01", item.value());
		assertEquals("abc", item.label());
	}

	@Test
	public void testOnlyValue() {
		DomainItem item = DomainItem.parse("345");
		assertEquals("345", item.value());
		assertEquals(null, item.label());
	}

}
