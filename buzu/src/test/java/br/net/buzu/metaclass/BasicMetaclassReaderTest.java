package br.net.buzu.metaclass;

import br.net.buzu.context.BasicParserFactory;
import br.net.buzu.parsing.simple.EnumNameMapper;
import br.net.buzu.parsing.simple.oldtime.OldTimestampMapper;
import br.net.buzu.parsing.simple.time.DateMapper;
import br.net.buzu.pplspec.annotation.PplParser;
import br.net.buzu.pplspec.api.MetaclassReader;
import br.net.buzu.pplspec.api.PayloadMapper;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.MetaInfoTest;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Subtype;
import br.net.buzu.sample.order.Order;
import br.net.buzu.sample.order.Status;
import br.net.buzu.sample.pojo.Person;
import br.net.buzu.sample.ppl.Human;
import br.net.buzu.sample.ppl.Xmen;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static br.net.buzu.pplspec.annotation.PplMetadata.EMPTY_INTEGER;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class BasicMetaclassReaderTest {

	private static final int NO_SCALE = MetaInfo.Companion.getNO_SCALE();

	private MetaclassReader reader;

	@Before
	public void before() {
		reader = new BasicMetaclassReader();
	}

	private void assertMetaclass(Metaclass metaclass, String fieldName, Class<?> fieldType, Class<?> elementType) {
		assertEquals(fieldName, metaclass.fieldName());
		assertEquals(fieldType, metaclass.fieldType());
		assertEquals(elementType, metaclass.elementType());

	}

	private void assertMetainfo(Metaclass metaclass, String name, int size, int scale, Subtype subtype,
			int minOccurs, int maxOccurs) {
		MetaInfoTest.assertMetainfo(metaclass.info(), name, subtype, size, scale, minOccurs, maxOccurs);
	}

	@Test
	public void testPerson() {
		Metaclass metaclass = reader.read(Person.class);
		assertMetaclass(metaclass, "", Person.class, Person.class);
		assertMetainfo(metaclass, "", EMPTY_INTEGER, NO_SCALE, Subtype.OBJ, 0, EMPTY_INTEGER);
		List<Metaclass> children = metaclass.children();

		Metaclass nameMC = children.get(0);
		assertMetaclass(nameMC, "name", String.class, String.class);
		assertMetainfo(nameMC, "name", EMPTY_INTEGER, NO_SCALE, Subtype.STRING, 0, EMPTY_INTEGER);

		Metaclass ageMC = children.get(1);
		assertMetaclass(ageMC, "age", Integer.class, Integer.class);
		assertMetainfo(ageMC, "age", EMPTY_INTEGER, NO_SCALE, Subtype.INTEGER, 0, EMPTY_INTEGER);

		Metaclass cityMC = children.get(2);
		assertMetaclass(cityMC, "city", String.class, String.class);
		assertMetainfo(cityMC, "city", EMPTY_INTEGER, NO_SCALE, Subtype.STRING, 0, EMPTY_INTEGER);
	}

	@Test
	public void testHuman() {
		Metaclass metaclass = reader.read(Human.class);
		assertMetaclass(metaclass, "", Human.class, Human.class);
		assertMetainfo(metaclass, "", EMPTY_INTEGER, NO_SCALE, Subtype.OBJ, 0, EMPTY_INTEGER);
		List<Metaclass> children = metaclass.children();
		assertEquals(6, sourceFields(Human.class).size());
		assertEquals(4, children.size()); // Ignored field: 2

		Metaclass fixMC = children.get(0);
		assertMetaclass(fixMC, "FIXED_FIELD", String.class, String.class);
		assertMetainfo(fixMC, "fixed-field", 5, NO_SCALE, Subtype.CHAR, 0, EMPTY_INTEGER);

		Metaclass nameMC = children.get(1);
		assertMetaclass(nameMC, "name", String.class, String.class);
		assertMetainfo(nameMC, "fullName", 10, NO_SCALE, Subtype.CHAR, 0, EMPTY_INTEGER);


		Metaclass weightMC = children.get(2);
		assertMetaclass(weightMC, "weight", double.class, double.class);
		assertMetainfo(weightMC, "weight", 5, NO_SCALE, Subtype.NUMBER, 0, EMPTY_INTEGER);

		Metaclass birthMC = children.get(3);
		assertMetaclass(birthMC, "birth", LocalDate.class, LocalDate.class);
		assertMetainfo(birthMC, "birthDay", 10, NO_SCALE, Subtype.ISO_DATE, 0, EMPTY_INTEGER);
	}

	/**
	 * Remove Jacoco fields
	 * 
	 * @param c
	 * @return
	 */
	private static List<Field> sourceFields(Class<?> c) {
		List<Field> fields = Arrays.asList(c.getDeclaredFields());
		return fields.stream().filter(f -> !f.getName().contains("$jacocoData")).collect(Collectors.toList());
	}

	@Test
	public void testXmen() {

		Metaclass metaclass = reader.read(Xmen.class);
		assertMetaclass(metaclass, "", Xmen.class, Xmen.class);
		assertMetainfo(metaclass, "", EMPTY_INTEGER, NO_SCALE, Subtype.OBJ, 0, EMPTY_INTEGER);

		List<Metaclass> children = metaclass.children();
		assertEquals(3, sourceFields(Xmen.class).size());
		assertEquals(5, children.size()); // Ignored field: 2

		Metaclass fixMC = children.get(0);
		assertMetaclass(fixMC, "FIXED_FIELD", String.class, String.class);
		assertMetainfo(fixMC, "fixed-field", 5, NO_SCALE, Subtype.CHAR, 0, EMPTY_INTEGER);

		Metaclass nameMC = children.get(1);
		assertMetaclass(nameMC, "name", String.class, String.class);
		assertMetainfo(nameMC, "fullName", 10, NO_SCALE, Subtype.CHAR, 0, EMPTY_INTEGER);

		Metaclass weightMC = children.get(2);
		assertMetaclass(weightMC, "weight", double.class, double.class);
		assertMetainfo(weightMC, "weight", 5, NO_SCALE, Subtype.NUMBER, 0, EMPTY_INTEGER);

		Metaclass birthMC = children.get(3);
		assertMetaclass(birthMC, "birth", LocalDate.class, LocalDate.class);
		assertMetainfo(birthMC, "birthDay", 10, NO_SCALE, Subtype.ISO_DATE, 0, EMPTY_INTEGER);

		Metaclass skillMC = children.get(4);
		assertMetaclass(skillMC, "skill", String.class, String.class);
		assertMetainfo(skillMC, "power", 20, NO_SCALE, Subtype.STRING, 0, EMPTY_INTEGER);
	}

	@Test
	public void testOrder() {

		Metaclass metaclass = reader.read(Order.class);
		assertMetaclass(metaclass, "", Order.class, Order.class);
		assertMetainfo(metaclass, "", EMPTY_INTEGER, NO_SCALE, Subtype.OBJ, 0, EMPTY_INTEGER);

		// number, customer, date, products, status, canceled [6]
		List<Metaclass> children = metaclass.children();
		assertEquals(6, sourceFields(Order.class).size());
		assertEquals(6, children.size());

		Metaclass statusMC = children.get(4);
		assertMetaclass(statusMC, "status", Status.class, Status.class);
		assertMetainfo(statusMC, "status", EMPTY_INTEGER, NO_SCALE, Subtype.STRING, 0, EMPTY_INTEGER);
		

	}
	
	@Test
	public void testCustomParser() {
		Metaclass metaclass = reader.read(Bean.class);
		
		Metaclass field2Metaclass = metaclass.getChildByName("field2");
		assertEquals(EnumNameMapper.class, field2Metaclass.mapperType());
		
		BasicParserFactory factory = new BasicParserFactory();
		Metaclass field4Metaclass = metaclass.getChildByName("field4");
		PayloadMapper parser4 = factory.create(field4Metaclass);
		assertEquals(OldTimestampMapper.class, parser4.getClass());
		
		Metaclass field5Metaclass = metaclass.getChildByName("field5");
		PayloadMapper parser5 = factory.create(field5Metaclass);
		assertEquals(OldTimestampMapper.class, parser5.getClass());

		Metaclass field6Metaclass = metaclass.getChildByName("field6");
		PayloadMapper parser6 = factory.create(field6Metaclass);
		assertEquals(DateMapper.class, parser6.getClass());
	}

}

@PplParser(EnumNameMapper.class)
class CustomType{
	
	
}

class Bean{
	private int field1;
	private CustomType field2;
	private String field3;
	private Date field4;
	@PplParser(OldTimestampMapper.class)
	private Date field5;
	private LocalDate field6;
	public int getField1() {
		return field1;
	}
	public void setField1(int field1) {
		this.field1 = field1;
	}
	public CustomType getField2() {
		return field2;
	}
	public void setField2(CustomType field2) {
		this.field2 = field2;
	}
	public String getField3() {
		return field3;
	}
	public void setField3(String field3) {
		this.field3 = field3;
	}
	public Date getField4() {
		return field4;
	}
	public void setField4(Date field4) {
		this.field4 = field4;
	}
	public Date getField5() {
		return field5;
	}
	public void setField5(Date field5) {
		this.field5 = field5;
	}
	public LocalDate getField6() {
		return field6;
	}
	public void setField6(LocalDate field6) {
		this.field6 = field6;
	}
}