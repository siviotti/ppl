package br.net.buzu.parsing;

import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.java.model.Metaclass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for Metaclass
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class MetaclassTest {

	@Test
	public void testString() {
		Metaclass toClass = new BasicMetaclassReader().read(String.class);
		assertEquals(String.class, toClass.fieldType());
		assertEquals(String.class, toClass.elementType());
		assertTrue(toClass.match(String.class));
		assertFalse(toClass.isCollection());
		assertFalse(toClass.isArray());
		assertFalse(toClass.isPrimitive());
	}

	@Test
	public void testUnsafeList() {
		Metaclass toClass = new BasicMetaclassReader().read(List.class);
		assertEquals(List.class, toClass.fieldType());
		assertEquals(Object.class, toClass.elementType());
		assertTrue(toClass.match(Object.class));
		assertTrue(toClass.isCollection());
		assertFalse(toClass.isArray());
		assertFalse(toClass.isPrimitive());
	}

	@Test
	public void testTypedList() {
		Metaclass toClass = new BasicMetaclassReader().read(List.class, String.class);
		assertEquals(List.class, toClass.fieldType());
		assertEquals(String.class, toClass.elementType());
		assertTrue(toClass.match(String.class));
		assertTrue(toClass.isCollection());
		assertFalse(toClass.isArray());
		assertFalse(toClass.isPrimitive());
	}

	@Test
	public void testArray() {
		Metaclass toClass = new BasicMetaclassReader().read(Integer[].class);
		assertEquals(Integer[].class, toClass.fieldType());
		assertEquals(Integer.class, toClass.elementType());
		assertTrue(toClass.match(Integer.class));
		assertTrue(toClass.isArray());
		assertFalse(toClass.isPrimitive());
	}

	@Test
	public void testPrimitiveArray() {
		Metaclass toClass = new BasicMetaclassReader().read(int[].class);
		assertEquals(int[].class, toClass.fieldType());
		assertEquals(int.class, toClass.elementType());
		assertTrue(toClass.match(int.class));
		assertTrue(toClass.isArray());
		assertTrue(toClass.isPrimitive());
	}

}
