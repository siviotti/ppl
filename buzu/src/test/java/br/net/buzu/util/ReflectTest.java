package br.net.buzu.util;

import br.net.buzu.pplspec.annotation.PplMetadata;
import br.net.buzu.pplspec.exception.PplReflectionException;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author Douglas Siviotti
 *
 */
public class ReflectTest {

	// **************************************************
	// Multiple
	// **************************************************

	@Test
	public void testIsMultiple() {
		String[] array = new String[0];
		// Array or Collection
		assertTrue(Reflect.isMultiple(array.getClass()));
		assertTrue(Reflect.isMultiple(ArrayList.class));
		// Single
		String s = "abc";
		assertFalse(Reflect.isMultiple(s.getClass()));
		assertFalse(Reflect.isMultiple(String.class));
	}

	@Test
	public void testIsCollection() {
		assertTrue(Reflect.isMultiple(ArrayList.class));
		assertTrue(Reflect.isMultiple(HashSet.class));
		assertFalse(Reflect.isMultiple(String.class));
		assertFalse(Reflect.isMultiple(Integer.class));
	}

	// **************************************************
	// Method Invoke
	// **************************************************

	@Test
	public void testFindGet() {
		Bean bean = new Bean();
		Method getName = Reflect.findGet("name", bean);
		assertEquals("getName", getName.getName());
		Method getSize = Reflect.findGet("size", bean);
		assertEquals("getSize", getSize.getName());
		Method isActive = Reflect.findGet("active", bean);
		assertEquals("isActive", isActive.getName());

		try {
			Reflect.findGet("color", bean);
			fail();
		} catch (PplReflectionException e) {
			assertTrue(e.getMessage().contains("getColor"));
		}
	}

	@Test
	public void testFindSet() {
		Bean bean = new Bean();
		Method setName = Reflect.findSet("name", bean, String.class);
		assertEquals("setName", setName.getName());
		Method setSize = Reflect.findSet("size", bean, int.class);
		assertEquals("setSize", setSize.getName());

		try {
			Reflect.findSet("color", bean, String.class);
			fail();
		} catch (PplReflectionException e) {
			assertTrue(e.getMessage().contains("setColor"));
		}
	}

	@Test
	public void testGet() {
		Bean bean = new Bean();
		bean.setName("Paul");
		bean.setSize(5);
		String s = Reflect.get(bean, "name");
		assertEquals("Paul", s);
		int i = Reflect.get(bean, "size");
		assertEquals(5, i);
		boolean b = Reflect.get(bean, "active");
		assertEquals(false, b);
		try {
			Reflect.get(bean, "color");
			fail();
		} catch (PplReflectionException e) {
			assertTrue(e.getMessage().contains("getColor"));
		}
	}

	@Test
	public void testSet() {
		Bean bean = new Bean();
		Reflect.set(bean, "name", String.class, "Paul");
		assertEquals("Paul", bean.getName());
		Reflect.set(bean, "size", int.class, 5);
		assertEquals(5, bean.getSize());

		try {
			Reflect.set(bean, "color", String.class, "red");
			fail();
		} catch (PplReflectionException e) {
			assertTrue(e.getMessage().contains("setColor"));
		}
	}

	// **************************************************
	// Fields
	// **************************************************
	@Test
	public void testGetElementType() throws NoSuchFieldException, SecurityException {
		Field field = Elements.class.getDeclaredField("field");
		assertEquals(String.class, field.getType());
		assertEquals(String.class, Reflect.getElementType(field));

		Field strings = Elements.class.getDeclaredField("strings");
		assertEquals(List.class, strings.getType());
		assertEquals(String.class, Reflect.getElementType(strings));

		Field array = Elements.class.getDeclaredField("array");
		assertEquals(String[].class, array.getType());
		assertEquals(String.class, Reflect.getElementType(strings));

		Field list = Elements.class.getDeclaredField("list");
		assertEquals(List.class, list.getType());
		try {
			assertEquals(String.class, Reflect.getElementType(list));
		} catch (PplReflectionException e) {
			assertTrue(e.getMessage().startsWith(Reflect.UNSAFE_COLLECTION));
		}
	}

	@Test
	public void testNewInstance() {
		// Implicit COnstructor
		Bean bean = (Bean) Reflect.newInstance(Bean.class);
		assertTrue(bean instanceof Bean);
		// # Constructors - select no parameters
		MultiConstructor m = (MultiConstructor) Reflect.newInstance(MultiConstructor.class);
		assertTrue(m instanceof MultiConstructor);
		assertTrue(m.getS().equals("DEFAULT"));// Used constructor has 0 parameters
		// List
		List<?> list = (List<?>) Reflect.newInstance(List.class);
		assertTrue(list instanceof ArrayList);
		// Set
		Set<?> set = (Set<?>) Reflect.newInstance(Set.class);
		assertTrue(set instanceof HashSet);
		// Map
		Map<?,?> map = (Map<?,?>) Reflect.newInstance(Map.class);
		assertTrue(map instanceof HashMap);

		// Explicit Construtor
		Elements elements = (Elements) Reflect.newInstance(Elements.class);
		assertTrue(map instanceof HashMap);
		// # Constructors - select 1 parameter
		MultiConstructorNoDefaultNotSerializable m2 = (MultiConstructorNoDefaultNotSerializable) Reflect.newInstance(MultiConstructorNoDefaultNotSerializable.class);
		assertTrue(m2 instanceof MultiConstructorNoDefaultNotSerializable);
		assertTrue(m2.getS().equals("ONE-PAR"));// Used constructor has 1 parameter
		// # Constructors - select none constructor because is serializable
		MultiConstructorNoDefaultButSerializable m3 = (MultiConstructorNoDefaultButSerializable) Reflect.newInstance(MultiConstructorNoDefaultButSerializable.class);
		assertTrue(m3 instanceof MultiConstructorNoDefaultButSerializable);
		assertTrue(m3.getI()!=2);// 2 Parameter constructor sets i == 2
		assertTrue(!"ONE-PAR".equals(m3.getS()));// 1 parameter constructor sets s = ONE-PAR

	}

	@Test
	public void testGetAllFields() {
		// Sonar/Jacoco Bug
		int jacocoList = Bean.class.getDeclaredFields().length > 3 ? 1 : 0;
		int jacocoArray = jacocoList;

		Field[] beanFieldArray = Bean.class.getDeclaredFields();
		List<Field> beanFieldList = Reflect.getAllFields(Bean.class);
		assertEquals(beanFieldArray.length, beanFieldList.size());
		assertEquals(3 + jacocoList, beanFieldList.size());
		assertEquals(3 + jacocoArray, beanFieldArray.length);

		jacocoList = jacocoArray == 0 ? jacocoList : jacocoList + 1;
		Field[] extendedBeanFieldArray = ExtendedBean.class.getDeclaredFields();
		List<Field> extendedBeanFieldLIst = Reflect.getAllFields(ExtendedBean.class);
		assertFalse(extendedBeanFieldArray.length == extendedBeanFieldLIst.size());
		assertEquals(4 + jacocoList, extendedBeanFieldLIst.size());
		assertEquals(1 + jacocoArray, extendedBeanFieldArray.length);

		jacocoList = jacocoArray == 0 ? jacocoList : jacocoList + 1;
		Field[] leafBeanFieldArray = LeafBean.class.getDeclaredFields();
		List<Field> leafBeanFieldLIst = Reflect.getAllFields(LeafBean.class);
		assertFalse(leafBeanFieldArray.length == leafBeanFieldLIst.size());
		assertEquals(5 + jacocoList, leafBeanFieldLIst.size());
		assertEquals(1 + jacocoArray, leafBeanFieldArray.length);
	}

	@Test
	public void testGetPplMetadata() {
		Map<String, Field> fields = Arrays.asList(MultiConstructor.class.getDeclaredFields()).stream()
				.collect(Collectors.toMap(Field::getName, f -> f));

		PplMetadata ppl1 = Reflect.getPplMetadata(fields.get("s"));
		assertNotNull(ppl1);
		assertEquals("s2", ppl1.name());

		PplMetadata ppl2 = Reflect.getPplMetadata(fields.get("leafBean"));
		assertNotNull(ppl2);
		assertEquals("Leaf", ppl2.name());

		PplMetadata ppl3 = Reflect.getPplMetadata(fields.get("i"));
		assertTrue(ppl3 == null);

	}

}

class Bean {

	private String name;
	private int size;
	private boolean active;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}

class ExtendedBean extends Bean {
	private String ext;

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
}

@PplMetadata(name = "Leaf")
class LeafBean extends ExtendedBean {
	private String leaf;

	public String getLeaf() {
		return leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
}

class Elements {
	private String field;
	private List<String> strings = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	private List list = new ArrayList();
	private String[] array = new String[1];

	public Elements(String[] array) {
		super();
		this.array = array;
	}

	public Elements(String field) {
		super();
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public List<String> getStrings() {
		return strings;
	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}

	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}

	public void setList(@SuppressWarnings("rawtypes") List list) {
		this.list = list;
	}

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

}

class MultiConstructor {
	@PplMetadata(name = "s2")
	private final String s;
	private final int i;
	private LeafBean leafBean;

	public MultiConstructor(String s, int i) {
		super();
		this.s = s;
		this.i = i+1;
	}

	public MultiConstructor(String s) {
		this(s, 1);
	}

	// THIS CONSTRUCTOR WILL BE CALLED
	public MultiConstructor() {
		this("DEFAULT", 0);
	}

	public String getS() {
		return s;
	}

	public int getI() {
		return i;
	}

	public LeafBean getLeafBean() {
		return leafBean;
	}

	public void setLeafBean(LeafBean leafBean) {
		this.leafBean = leafBean;
	}

}

class MultiConstructorNoDefaultNotSerializable {
	@PplMetadata(name = "s2")
	private final String s;
	private final int i;
	private LeafBean leafBean;

	// Nou used - 2 parameters
	public MultiConstructorNoDefaultNotSerializable(String s, int i) {
		super();
		this.s = s;
		this.i = 2;
	}

	// THIS IS CALLED - less parameters (1)
	public MultiConstructorNoDefaultNotSerializable(String s) {
		this("ONE-PAR", 1);
	}

	public String getS() {
		return s;
	}

	public int getI() {
		return i;
	}

	public LeafBean getLeafBean() {
		return leafBean;
	}

	public void setLeafBean(LeafBean leafBean) {
		this.leafBean = leafBean;
	}

}

class MultiConstructorNoDefaultButSerializable implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@PplMetadata(name = "s2")
	private final String s;
	private final int i;
	private LeafBean leafBean;

	// Nou used (2 parameters) because is Serializable
	public MultiConstructorNoDefaultButSerializable(String s, int i) {
		super();
		this.s = s;
		this.i = 2;
	}

	// Not used (1 parameter) because is Serializable
	public MultiConstructorNoDefaultButSerializable(String s) {
		this("ONE-PAR", 1);
	}

	public String getS() {
		return s;
	}

	public int getI() {
		return i;
	}

	public LeafBean getLeafBean() {
		return leafBean;
	}

	public void setLeafBean(LeafBean leafBean) {
		this.leafBean = leafBean;
	}

}