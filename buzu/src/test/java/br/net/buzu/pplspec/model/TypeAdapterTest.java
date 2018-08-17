package br.net.buzu.pplspec.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import br.net.buzu.sample.pojo.Person;
import br.net.buzu.util.Reflect;

public class TypeAdapterTest {

	@Test
	public void test() {
		Person person = new Person("Ladybug", 15, "Paris");
		try {
			Field[] fields = Person.class.getDeclaredFields();
			Field name = fields[0];
			name.setAccessible(true);
			TypeAdapter typeAdapter = TypeAdapter.create(name);
			assertEquals("Ladybug", person.getName());
			assertEquals("Ladybug", typeAdapter.get(person));
			typeAdapter.set(person, "Catnoir");
			assertEquals("Catnoir", person.getName());
			assertEquals("Catnoir", typeAdapter.get(person));
		} catch (SecurityException e) {
			fail();
		}
	}

	@Test
	public void testTime() {
		Field[] fields = Person.class.getDeclaredFields();
		Field name = fields[0];
		name.setAccessible(true);
		TypeAdapter typeAdapter = TypeAdapter.create(name);
		int max = 1000000;

		Person person = new Person("Ladybug", 15, "Paris");
		long t0 = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			Reflect.set(person, "name", String.class, "Catnoir");
			assertEquals("Catnoir", person.getName());
		}
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < max; i++) {
			typeAdapter.set(person, "Catnoir");
			assertEquals("Catnoir", person.getName());
		}
		long t2 = System.currentTimeMillis();
		
		System.out.println("Reflect:" + (t1-t0));
		System.out.println("Reflect:" + (t2-t1));
	}

}
