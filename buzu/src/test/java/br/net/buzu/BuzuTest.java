package br.net.buzu;

import br.net.buzu.context.BasicCoderManager;
import br.net.buzu.context.BasicContext;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.load.BasicMetadataLoader;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.pplimpl.metadata.GenericMetadataParser;
import br.net.buzu.pplspec.api.MetadataCoder;
import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.Dialect;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.PplString;
import br.net.buzu.sample.order.*;
import br.net.buzu.sample.pojo.Person;
import br.net.buzu.sample.ppl.Human;
import br.net.buzu.sample.ppl.StaticPerson;
import br.net.buzu.sample.ppl.Xmen;
import br.net.buzu.sample.time.TimePojo;
import br.net.buzu.util.PplReader;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.Assert.*;

/**
 * 
 * Unit Test for Buzu. [REGRESSION TEST]
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BuzuTest {


	// *** Person ***
	public static final String[] PERSON_METADATA_NAMES = { "name", "age", "city" };
	public static final String[] PERSON_METADATA_TYPES = { "S", "I", "S" };
	public static final String PERSON_NAME = "Ladybug";
	public static final Integer PERSON_AGE = 15;
	public static final String PERSON_CITY = "Paris";

	public static final int[] PERSON_METADATA_SIZES = { PERSON_NAME.length(), PERSON_AGE.toString().length(),
			PERSON_CITY.length() };
	// metadata = (name:S7;age:I2;city:S5)
	public static final String PERSON_METADATA = "(" + PERSON_METADATA_NAMES[0] + Token.NAME_END
			+ PERSON_METADATA_TYPES[0] + PERSON_METADATA_SIZES[0] + Token.METADATA_END + PERSON_METADATA_NAMES[1]
			+ Token.NAME_END + PERSON_METADATA_TYPES[1] + PERSON_METADATA_SIZES[1] + Token.METADATA_END
			+ PERSON_METADATA_NAMES[2] + Token.NAME_END + PERSON_METADATA_TYPES[2] + PERSON_METADATA_SIZES[2] + ")";
	public static final String PERSON_PAYLOAD = "Ladybug15Paris";
	// PPL = (name:S7;age:I2;city:S5)Ladybug15Paris
	public static final String PERSON_PPL_STRING = PERSON_METADATA + PERSON_PAYLOAD;

	public static final Person PERSON_INSTANCE = new Person(PERSON_NAME, PERSON_AGE, PERSON_CITY);
	public static final StaticPerson STATIC_PERSON_INSTANCE = new StaticPerson(PERSON_NAME, PERSON_AGE, PERSON_CITY);
	
	// *** StaticPerson ***
	
	private static final String STATIC_PERSON = "(personName:C10;personAge:I10;personCity:C10)Ladybug   0000000015Paris     ";
	public static final String STATIC_PERSON_NAME = "Ladybug   ";
	public static final Integer STATIC_PERSON_AGE = 15;
	public static final String STATIC_PERSON_CITY = "Paris     ";

	// *** Order ***

	// Customer
	public static final List<Address> ADDRESSES = new ArrayList<>();
	public static final Address BILING = new Address("Champs Elysee 10", "Paris", "75008", AddressType.BILLING);
	public static final Address DELIVERY = new Address("Baker Street 221b", "London", "NW1 6XE", AddressType.DELIVERY);
	public static final List<String> PHONES = new ArrayList<>();
	public static final String PHONE1 = "11111111";
	public static final String PHONE2 = "22222222";
	public static final String CUSTOMER_NAME = "Ladybug";
	public static final Customer CUSTOMER;
	// Products
	public static final List<Product> PRODUCTS = new ArrayList<>();
	public static final Product PRODUCT1 = new Product("Book", 45.99);
	public static final Product PRODUCT2 = new Product("Notebook", 1200.00);
	public static final Product PRODUCT3 = new Product("Clock", 25.52);
	public static final Product PRODUCT4 = new Product("Software", 0.99);
	public static final Product PRODUCT5 = new Product("Tablet", 500.00);
	// Order
	public static final String ORDER_NUMBER = "1234567890";
	public static final LocalDate ORDER_DATE = LocalDate.of(2017, 11, 30);
	public static final Status ORDER_STATUS = Status.OPENED;
	public static final boolean ORDER_CANCELED = false;
	public static final Order ORDER_INSTANCE;
	public static final String ORDER_PPL_STRING = "(number:S10;customer:(name:S7;addresses:(street:S17;city:S6;zip:S7;type:S8)#0-2;phones:S8#0-2);date:D;products:(description:S8;price:N6)#0-5;status:S6;canceled:B)1234567890LadybugChamps Elysee 10 Paris 75008  BILLING Baker Street 221bLondonNW1 6XEDELIVERY111111112222222220171130Book    045.99Notebook1200.0Clock   025.52Software000.99Tablet  0500.0OPENEDfalse";

	// *** Human ***
	public static final String HUMAN_NAME = "Logan";
	public static final LocalDate HUMAN_BIRTH = LocalDate.of(1882, 04, 5);
	public static final double HUMAN_WEIGHT = 99.9;
	public static final String HUMAN_PPL_STRING = "(fixed-field:C3;fullName:C5;weight:N4;birthDay:DI)fixLogan99.91882-04-05";

	public static final Human HUMAN = new Human(HUMAN_NAME, HUMAN_BIRTH, HUMAN_WEIGHT);

	// *** Xmen ***
	public static final String XMEN_PPL_STRING = "(fixed-field:C3;fullName:C9;weight:N4;birthDay:DI;power:S7)fixWolverine99.01882-04-05healing";

	// *** TimePojo ***
	public static final int YEAR = 2015;
	public static final int MONTH = 10;
	public static final int DAY = 21;
	public static final int HOUR = 15;
	public static final int MINUTE = 43;
	public static final int SECOND = 27;

	public static final LocalDate LOCAL_DATE = LocalDate.of(YEAR, MONTH, DAY);
	public static final LocalTime LOCAL_TIME = LocalTime.of(HOUR, MINUTE, SECOND);
	public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
	public static final Date OLD_DATE = new GregorianCalendar(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND).getTime();

	static {

		// Customer
		ADDRESSES.add(BILING);
		ADDRESSES.add(DELIVERY);
		PHONES.add(PHONE1);
		PHONES.add(PHONE2);
		CUSTOMER = new Customer(CUSTOMER_NAME, ADDRESSES, PHONES);

		// Products
		PRODUCTS.add(PRODUCT1);
		PRODUCTS.add(PRODUCT2);
		PRODUCTS.add(PRODUCT3);
		PRODUCTS.add(PRODUCT4);
		PRODUCTS.add(PRODUCT5);

		// Order
		ORDER_INSTANCE = new Order(ORDER_NUMBER, CUSTOMER, ORDER_DATE, PRODUCTS, ORDER_STATUS, ORDER_CANCELED);
	}

	private Buzu buzu;

	@Before
	public void before() {
		//buzu = new Buzu();
		buzu = new BuzuBuilder().metadataParser(new GenericMetadataParser()).build();
	}

	// ********** CREATE **********

	@Test
	public void testCreate() {
		Buzu buzu1 = new Buzu();
		Buzu buzu2 = new Buzu(new BasicContext());
		Buzu buzu3 = new Buzu(new BasicContext(), new BasicMetadataParser(), new BasicMetaclassReader(),
				new BasicMetadataLoader(), Dialect.Companion.getDEFAULT(), false);
		Buzu buzu4 = new Buzu(new BasicContext(), null, null, null, null, false);

		checkBuzu(buzu1);
		checkBuzu(buzu2);
		checkBuzu(buzu3);
		checkBuzu(buzu4);
	}

	private void checkBuzu(Buzu buzu) {
		assertEquals(BasicContext.class, buzu.context().getClass());
		assertEquals(BasicMetadataParser.class, buzu.parser().getClass());
		assertEquals(BasicMetaclassReader.class, buzu.reader().getClass());
		assertEquals(BasicMetadataLoader.class, buzu.loader().getClass());
		MetadataCoder coder = BasicCoderManager.INSTANCE.getCoder(Dialect.Companion.getDEFAULT());
		assertEquals(coder.getClass(), buzu.coder().getClass());
		assertEquals(Dialect.Companion.getDEFAULT(), buzu.dialect());
		assertFalse(buzu.isSerializeNulls());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullContext() {
		new Buzu(null);
	}

	@Test
	public void testNulls() {
		// fromPpl
		assertNull(buzu.fromPpl(null, Object.class));
		assertNull(buzu.fromPpl("", Object.class));
		try {// empty
			assertNull(buzu.fromPpl("  ", Object.class));
		} catch (PplParseException ppe) {

		}	
		Metaclass metaclass = new BasicMetaclassReader().read(Order.class);
		assertNull(buzu.fromPpl(null, metaclass));
		assertNull(buzu.fromPpl("", metaclass));
		try { // empty
			assertNull(buzu.fromPpl("  ", metaclass));
		} catch (PplParseException ppe) {
		}	

		try { // Not Static Metadata
			assertNull(buzu.fromPpl("(S10#1-0)1234567890", metaclass));
		} catch (PplParseException ppe) {
			assertEquals(Buzu.PARSE_REQUIRES_STATIC_METADATA, ppe.getMessage());
		}	
		
		// toPpl
		assertNull(buzu.toPpl(null));
	}

	// ********** Person (Simple Pojo) **********

	@Test
	public void testPersonToPpl() {
		String ppl = buzu.toPpl(PERSON_INSTANCE);
		assertEquals(PERSON_PPL_STRING, ppl);
	}

	@Test
	public void testPersonToPplStaticMetaclass() {
		Metaclass metaclass = new BasicMetaclassReader().read(StaticPerson.class);
		String ppl = buzu.toPpl(STATIC_PERSON_INSTANCE, metaclass);
		assertEquals(STATIC_PERSON, ppl);
	}

	@Test
	public void testPersonFromPpl() {
		String ppl = PERSON_PPL_STRING;

		Person person = buzu.fromPpl(ppl, Person.class);

		assertEquals(PERSON_NAME, person.getName());
		assertEquals(PERSON_AGE, person.getAge());
		assertEquals(PERSON_CITY, person.getCity());
	}

	@Test
	public void testPersonFromPplStaticMetaclass() {
		String ppl = STATIC_PERSON;

		StaticPerson person = buzu.fromPpl(ppl, StaticPerson.class);

		assertEquals(STATIC_PERSON_NAME, person.getName());
		assertEquals(STATIC_PERSON_AGE, person.getAge());
		assertEquals(STATIC_PERSON_CITY, person.getCity());
	}

	@Test
	public void testPersonFromPplFile() {
		String ppl = PplReader.read("./src/test/resources/ladybug.ppl");

		Person person = buzu.fromPpl(ppl, Person.class);

		assertEquals(PERSON_NAME, person.getName());
		assertEquals(PERSON_AGE, person.getAge());
		assertEquals(PERSON_CITY, person.getCity());
	}

	// ********** Order (Complex Pojo)**********

	@Test
	public void testOrderToPpl() {
		String ppl = buzu.toPpl(ORDER_INSTANCE);
		assertEquals(ORDER_PPL_STRING, ppl);
	}


	@Test
	public void testOrderFromPpl() {
		String ppl = ORDER_PPL_STRING;
		Order order = buzu.fromPpl(ppl, Order.class);
		assertEquals(ORDER_INSTANCE, order);

	}

	@Test
	public void testOrderFromPplFile() {
		String ppl = PplReader.read("./src/test/resources/order.ppl");
		Order order = buzu.fromPpl(ppl, Order.class);
		assertEquals(ORDER_INSTANCE, order);
	}

	// ********** Human (Simple PPL (Annotation) Class)**********

	@Test
	public void testHumanToPpl() {
		String ppl = buzu.toPpl(HUMAN);
		assertEquals(HUMAN_PPL_STRING, ppl);
	}

	@Test
	public void testHumanFromPpl() {
		Human human = buzu.fromPpl(HUMAN_PPL_STRING, Human.class);

		assertEquals(HUMAN_NAME, human.getName());
		assertEquals(HUMAN_BIRTH, human.getBirth());
		assertTrue(Double.doubleToLongBits(human.getWeight()) == Double.doubleToLongBits(HUMAN_WEIGHT));

	}

	// ********** Xmen (Simple PPL (Annotation) SubClass)**********

	@Test
	public void testXmenToPpl() {
		String ppl = buzu.toPpl(Xmen.WOLVERINE);
		assertEquals(XMEN_PPL_STRING, ppl);
	}

	@Test
	public void testXmenFromPpl() {
		Xmen wolverine = buzu.fromPpl(XMEN_PPL_STRING, Xmen.class);
		assertEquals(Xmen.WOLVERINE.getName(), wolverine.getName());
		assertEquals(Xmen.WOLVERINE.getSkill(), wolverine.getSkill());
		assertEquals(Xmen.WOLVERINE.getBirth(), wolverine.getBirth());

		Human human = buzu.fromPpl(XMEN_PPL_STRING, Human.class);
		assertEquals(Xmen.WOLVERINE.getName(), human.getName());
		assertEquals(Xmen.WOLVERINE.getBirth(), human.getBirth());
	}

	// ********** Date fields **********

	@Test
	public void testTimeFields() {
		TimePojo timePojo = new TimePojo();
		timePojo.setLocalDate(LOCAL_DATE);
		timePojo.setLocalTime(LOCAL_TIME);
		timePojo.setLocalDateTime(LOCAL_DATE_TIME);
		timePojo.setDate(OLD_DATE);

		String ppl = buzu.toPpl(timePojo);
		
		assertTrue(ppl.contains("localDate:D"));
		assertTrue(ppl.contains("localTime:t"));
		assertTrue(ppl.contains("localDateTime:T"));
		assertTrue(ppl.contains("date:T"));
	}

	// ********** Collections **********
	@Test
	public void testCollections() {
		assertEquals(PplString.Companion.getEMPTY().getMetadata(), buzu.toPpl(new ArrayList<>()));
		List<Person> people = new ArrayList<>();
		people.add(PERSON_INSTANCE);
		people.add(PERSON_INSTANCE);
		people.add(PERSON_INSTANCE);
		String ppl = buzu.toPpl(people);
		assertTrue(ppl.contains(PERSON_METADATA)); // name:S7;age:I2;city:S5
		assertTrue(ppl.contains("#0-" + people.size())); // #0-3
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUnsafeCollection() {
		List<Person> people = new ArrayList<>();
		people.add(PERSON_INSTANCE);
		people.add(new Person("Catnoir", 16, "London"));
		String ppl = buzu.toPpl(people);

		// 2 Records = List<Person>
		Object o = buzu.fromPpl(ppl, Person.class);// 2 Records
		assertTrue(o instanceof Collection<?>); // 2 items
		List<Person> list = (List<Person>) o; // Is Collection
		assertEquals(2, list.size());

		// 1 Record = Single Person Object
		people.remove(1);
		ppl = buzu.toPpl(people);
		o = buzu.fromPpl(ppl, Person.class); // 1 Record
		assertFalse(o instanceof Collection<?>);// Is not Collection
		assertTrue(o instanceof Person);
		assertEquals(PERSON_INSTANCE.getName(), ((Person) o).getName());
	}

	@Test
	public void testExplictCollection() {
		// Create a PPL text with 2 Records
		List<Person> people = new ArrayList<>();
		people.add(PERSON_INSTANCE);
		people.add(new Person("Catnoir", 16, "London"));
		String ppl = buzu.toPpl(people);

		// Explicit collection parse - List.size = 2
		List<Person> list = buzu.fromPplList(ppl, Person.class);
		assertEquals(2, list.size());

		// Explicit collection parse - List.size = 1
		people.remove(1);
		ppl = buzu.toPpl(people);
		list = buzu.fromPplList(ppl, Person.class);
		assertEquals(1, list.size());

	}

}
