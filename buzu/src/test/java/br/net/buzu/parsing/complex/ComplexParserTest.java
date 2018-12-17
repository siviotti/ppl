package br.net.buzu.parsing.complex;

import br.net.buzu.conserpro.*;
import br.net.buzu.context.BasicParserFactory;
import br.net.buzu.example.Address;
import br.net.buzu.example.Customer;
import br.net.buzu.example.Product;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.MetadataBuilder;
import br.net.buzu.java.api.PayloadMapper;
import br.net.buzu.java.model.Metaclass;
import br.net.buzu.java.model.Metadata;
import br.net.buzu.java.model.StaticMetadata;
import br.net.buzu.sample.pojo.Person;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Douglas Siviotti
 *
 */
public class ComplexParserTest {

	static void serial(String s) {
		// System.out.println("<SERIAL size='" + s.length() + "'>\n" + s +
		// "\n</SERIAL>");
	}

	static void dump(Metadata metadata) {
		// System.out.println(metadata.asTree(0));
		// System.out.println(VerboseMetadataSerializer.INSTANCE.serialize(metadata));
	}

	static PayloadMapper createParser(Metadata metadata, Class<?> clazz) {
		return createParser(metadata, new BasicMetaclassReader().read(clazz));
	}

	static PayloadMapper createParser(Metadata metadata, Metaclass pplClass) {
		return new BasicParserFactory().create(pplClass);
	}

	@Test
	public void test() {
		List<Address> adresses = new ArrayList<Address>();
		adresses.add(new Address("10 XPTO Street", "London", "1111", new Product(1L, 'A', "Book"),
				new Product(2L, 'B', "Pen")));
		adresses.add(new Address("999-B Baker Street", "London", "2222", new Product(1L, 'X', "Notebook")));
		adresses.add(new Address("221-B Baker Street", "London", "3333", new Product(1L, 'D', "Disc1"),
				new Product(1L, 'D', "Disc2"), new Product(1L, 'D', "Disc3")));
		adresses.add(new Address("10 Uping Street", "London", "4444", new Product(1L, 'Z', "Car")));
		Customer customer = new Customer("John", adresses, 33, "12345678", "8765432000");
		StaticMetadata metadata = new MetadataBuilder().buildStatic(customer);
		Metaclass from = new BasicMetaclassReader().read(Customer.class);
		PayloadMapper parser = createParser(metadata, from);
		dump(metadata);
		String s = parser.serialize(metadata, customer, from);
		serial(s);
		Customer c2 = parser.parse(metadata, s, from);
		assertTrue(c2.getName().equals("John"));
	}

	@Test
	public void testEmpty() {
		Empresa empresa = new Empresa();
		StaticMetadata metadata = new MetadataBuilder().buildStatic(empresa);
		Metaclass fromEmpresa = new BasicMetaclassReader().read(Empresa.class);
		PayloadMapper parser = createParser(metadata, fromEmpresa);
		dump(metadata);
		String serial = parser.serialize(metadata, empresa, fromEmpresa);
		serial(serial);
		assertEquals(metadata.info().getSize(), serial.length());
		assertEquals(metadata.info().getSize(), metadata.serialMaxSize());
		Empresa parsedEmpresa = parser.parse(metadata, serial,
				new BasicMetaclassReader().read(Empresa.class));
		assertEquals(parsedEmpresa.getRazaoSocial(), new Empresa().getRazaoSocial());
	}

	@Test
	public void testNoMultiple() {
		List<Telefone> telefonesCabral = new ArrayList<Telefone>();
		telefonesCabral.add(new Telefone("23", "55555555"));
		Socio cabral = new Socio("98765432100", "Cabral", 50.0, telefonesCabral, "teste@test", "abc");

		Date aberturaBB = new Date(new GregorianCalendar(1808, Calendar.OCTOBER, 12).getTimeInMillis());

		Empresa empresa = new Empresa("00000000000191", "Banco do Basil", aberturaBB, true,
				new NaturezaJuridica(252, "Financeira"), cabral);
		
		StaticMetadata metadata = new MetadataBuilder().buildStatic(empresa);
		Metaclass from = new BasicMetaclassReader().read(Empresa.class);
		PayloadMapper parser = createParser(metadata, from);
		dump(metadata);
		String serial = parser.serialize(metadata, empresa, from);
		serial(serial);
		assertEquals(metadata.serialMaxSize(), serial.length());
		// assertEquals(metadata.getSize(), metadata.getMaxSize());
		Empresa parsedEmpresa = parser.parse(metadata, serial, from);
		assertEquals("Banco do Basil", parsedEmpresa.getRazaoSocial());
		assertEquals("00000000000191", parsedEmpresa.getCnpj());
		assertEquals(aberturaBB, parsedEmpresa.getDtAbertura());
		assertEquals(1, parsedEmpresa.getSocios().get(0).getTelefones().size());
		assertEquals("55555555", parsedEmpresa.getSocios().get(0).getTelefones().get(0).getNumero());
	}

	@Test
	public void testEmpresaTeste() {
		Empresa empresa = MockFactory.empresaTeste();
		StaticMetadata metadata = new MetadataBuilder().buildStatic(empresa);
		PayloadMapper parser = createParser(metadata, Empresa.class);
		dump(metadata);
		// Serialize
		Metaclass metaclass = new BasicMetaclassReader().read(Empresa.class);
		String serial = parser.serialize(metadata, empresa, metaclass);
		serial(serial);
		assertEquals(metadata.serialMaxSize(), serial.length());
		// Parse
		Empresa parsed = parser.parse(metadata, serial, new BasicMetaclassReader().read(Empresa.class));
		assertEquals(MockFactory.EMPRESA_NOME, parsed.getRazaoSocial());
		assertEquals(MockFactory.EMPRESA_CNPJ, parsed.getCnpj());
		assertEquals(MockFactory.NJ_CODIGO, parsed.getNj().getCodigo());
		assertEquals(MockFactory.NJ_NOME, parsed.getNj().getDescricao());
		assertEquals(3, parsed.getSocios().size());
	}

	@Test
	public void testBB() {
		Empresa empresa = MockFactory.bancoDoBrasil();
		StaticMetadata metadata = new MetadataBuilder().buildStatic(empresa);
		PayloadMapper parser = createParser(metadata, Empresa.class);
		dump(metadata);
		String serial = parser.serialize(metadata, empresa, new BasicMetaclassReader().read(Empresa.class));
		serial(serial);
		assertEquals(metadata.serialMaxSize(), serial.length());
		Empresa parsed = parser.parse(metadata, serial, new BasicMetaclassReader().read(Empresa.class));
		assertEquals("Banco do Basil", parsed.getRazaoSocial());
		assertEquals("00000000000191", parsed.getCnpj());
		assertEquals(2, parsed.getSocios().size());
		assertEquals(3, parsed.getSocios().get(1).getTelefones().size());
		assertEquals("33333333", parsed.getSocios().get(1).getTelefones().get(1).getNumero());
	}

	// @Test
	public void listPerson() {
		List<Person> persons = new ArrayList<Person>();
		persons.add(new Person("Ladybug", 15, "Paris"));
		persons.add(new Person("Catnoir", 15, "Paris"));
		StaticMetadata metadata = new MetadataBuilder().buildStatic(persons);
		Metaclass from = new BasicMetaclassReader().read(ArrayList.class, Person.class);
		PayloadMapper parser = createParser(metadata, from);
		dump(metadata);
		String serial = parser.serialize(metadata, persons, from);
		serial(serial);
		assertEquals(metadata.serialMaxSize(), serial.length());
		List<Person> parsed = parser.parse(metadata, serial,
				new BasicMetaclassReader().read(List.class, Person.class));
		assertEquals(2, parsed.size());
		assertEquals("Ladybug", parsed.get(0).getName());
	}

	// @Test
	public void testList() {
		List<Empresa> empresas = new ArrayList<Empresa>();
		empresas.add(MockFactory.empresaTeste());
		empresas.add(MockFactory.empresaTeste());
		empresas.add(MockFactory.empresaTeste());
		// empresas.add(MockFactory.empresaVazia());
		// empresas.add(MockFactory.bancoDoBrasil());
		StaticMetadata metadata = new MetadataBuilder().buildStatic(empresas);
		Metaclass from = new BasicMetaclassReader().read(List.class, Empresa.class);
		PayloadMapper parser = createParser(metadata, from);
		dump(metadata);
		for (Empresa empresa : empresas) {
			parser.serialize(metadata, empresa, from);
		}
		String serial = parser.serialize(metadata, empresas, from);
		serial(serial);
		List<Empresa> parsed = parser.parse(metadata, serial,
				new BasicMetaclassReader().read(List.class, Empresa.class));
		assertEquals(3, parsed.size());
	}

	@Test
	public void testPj() {
		PJ pj = MockFactory.pjBancoDoBrasil();
		StaticMetadata metadata = new MetadataBuilder().buildStatic(pj);
		Metaclass from = new BasicMetaclassReader().read(PJ.class);
		PayloadMapper parser = createParser(metadata, from);
		dump(metadata);
		String serial = parser.serialize(metadata, pj, from);
		serial(serial);
		PJ parsedEmpresa = parser.parse(metadata, serial, new BasicMetaclassReader().read(PJ.class));
		assertTrue(parsedEmpresa instanceof PJ);
	}

	@Test
	public void testPerson() {
		Person person = new Person("Ladybug", 15, "Paris");
		StaticMetadata metadata = new MetadataBuilder().buildStatic(person);
		PayloadMapper parser = createParser(metadata, new BasicMetaclassReader().read(Person.class));
		dump(metadata);
		String s = parser.serialize(metadata, person, new BasicMetaclassReader().read(Person.class));
		serial(s);
		Person clone = parser.parse(metadata, s, new BasicMetaclassReader().read(Person.class));
		assertEquals("Ladybug", clone.getName());
		assertEquals(new Integer(15), clone.getAge());
		assertEquals("Paris", clone.getCity());
	}

}
