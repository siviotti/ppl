package br.net.buzu.pplspec.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

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

	@Test(expected = NullPointerException.class)
	public void testNullPointerException() {
		new DomainItem(null);
	}

	@Test
	public void testObjectMethods() {
		DomainItem item1 = DomainItem.parse("01=abc");
		DomainItem item2 = DomainItem.parse("01=abc");
		DomainItem item3 = DomainItem.parse("01=xyz");
		DomainItem item4 = DomainItem.parse("02=xyz");
		assertEquals(item1, item2);
		assertEquals(item2, item1);
		assertFalse(item1.equals(null));
		assertFalse(item1.equals("abc"));
		assertTrue(item1.equals(item1));
		assertEquals(item1.hashCode(), item2.hashCode());
		assertEquals(item1.toString(), item2.toString());
		assertFalse(item1.equals(item3));
		assertFalse(item1.equals(item4));
		
		assertEquals(0, item1.compareTo(item2));
		assertEquals(0, item1.compareTo(item3));
		assertEquals(-1, item1.compareTo(item4));
		assertEquals(1, item4.compareTo(item1));
	}

}
