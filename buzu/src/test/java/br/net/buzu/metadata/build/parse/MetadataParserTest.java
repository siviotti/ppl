package br.net.buzu.metadata.build.parse;

import br.net.buzu.context.BasicContext;
import br.net.buzu.metadata.ComplexStaticMetadada;
import br.net.buzu.metadata.code.ShortMetadataCoder;
import br.net.buzu.lang.Syntax;
import br.net.buzu.model.Domain;
import br.net.buzu.model.Metadata;
import br.net.buzu.model.StaticMetadata;
import br.net.buzu.model.Subtype;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static br.net.buzu.model.Domains.domainOf;
import static br.net.buzu.model.PplStringKt.pplStringOf;
import static org.junit.Assert.*;

/**
 * Unit Test domainOf MetadataParser
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class MetadataParserTest {

	static ParseNode NAME = new ParseNode("name", "S", "20", "0-1", null);
	static ParseNode AGE = new ParseNode("age", "I", "2", "", null);
	static ParseNode CITY = new ParseNode("city", "S", "5", "", null);
	static List<ParseNode> CHILDREN = new ArrayList<ParseNode>();
	static {
		CHILDREN.add(NAME);
		CHILDREN.add(AGE);
		CHILDREN.add(CITY);
	}
	static ParseNode PERSON = new ParseNode("", "", "", "", CHILDREN);
	static ParseNode EXT = new ParseNode("color", "S", "10", "0-1", "['black','white','red']", "red", "K I test", null);
	static ParseNode COMPLEX_EXT = new ParseNode("color", "S", "10", "0-1", "['1=black','2=white','3=red']", "3",
			"K I test", null);

	private BasicMetadataParser parser;

	@Before
	public void before() {
		parser = new BasicMetadataParser();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoSplitter() {
		new BasicMetadataParser(new BasicContext(), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoExtensionFactory() {
		new BasicMetadataParser(null, new Splitter());
	}

	@Test()
	public void testNew() {
		BasicMetadataParser parser = new BasicMetadataParser(new BasicContext(), new Splitter());
		assertEquals(Splitter.class, parser.getSplitter().getClass());
	}

	@Test
	public void testParse() {
		BasicMetadataParser metaParser = new BasicMetadataParser();
		Metadata metadata = metaParser.parse(pplStringOf("(name:S20#0-1)"));
		assertMetadata(metadata, "name", Subtype.STRING, 20, 0, 1);

	}

	@Test
	public void testParseSingle() {
		BasicMetadataParser metadataParser = new BasicMetadataParser();
		Metadata metadata = metadataParser.parse("", NAME, 0);
		assertMetadata(metadata, "name", Subtype.STRING, 20, 0, 1);
	}

	private void assertMetadata(Metadata metadata, String name, Subtype subtype, int size, int minOccurs,
			int maxOccurs) {
		assertEquals(name, metadata.name());
		assertEquals(subtype, metadata.info().getSubtype());
		assertEquals(size, metadata.info().getSize());
		assertEquals(minOccurs, metadata.info().getMinOccurs());
		assertEquals(maxOccurs, metadata.info().getMaxOccurs());
	}

	@Test
	public void testParseComplex() {
		Metadata metadata = parser.parse("", PERSON, 0);
		assertEquals(3, metadata.children().size());
		assertMetadata(metadata.children().get(0), "name", Subtype.STRING, 20, 0, 1);
		assertMetadata(metadata.children().get(1), "age", Subtype.INTEGER, 2, 0, 1);
		assertMetadata(metadata.children().get(2), "city", Subtype.STRING, 5, 0, 1);
	}

	@Test
	public void testParseExtension() {
		Metadata metadata = parser.parse("", EXT, 0);
		assertMetadata(metadata, "color", Subtype.STRING, 10, 0, 1);
		Domain domain = domainOf("black", "white", "red");
		assertEquals(domain.items(), metadata.info().getDomain().items());
	}

	@Test
	public void testEmptyMetadata() {
		StaticMetadata metadada = (StaticMetadata) parser.parse(pplStringOf("()"));
		assertEquals(Syntax.NO_NAME_START + "0", metadada.name());
		assertEquals(Subtype.STRING, metadada.info().getSubtype());
		assertEquals(Subtype.CHAR.fixedSize(), metadada.info().getSize());
		assertEquals(Subtype.CHAR.fixedSize(), metadada.serialMaxSize());
		assertEquals(0, metadada.info().getScale());
		assertEquals(0, metadada.info().getMinOccurs());
		assertEquals(1, metadada.info().getMaxOccurs());
		assertEquals("S0", ShortMetadataCoder.INSTANCE.code(metadada));
	}

	@Test
	public void testEmptyLayout() {
		StaticMetadata metadada = (StaticMetadata) parser.parse(pplStringOf("(X:(;;))"));
		assertEquals(ComplexStaticMetadada.class, metadada.getClass());
		assertEquals("X", metadada.name());
		assertEquals(Subtype.OBJ, metadada.info().getSubtype());
		assertEquals(0, metadada.info().getSize()); // 2 x 0
		assertEquals(0, metadada.serialMaxSize());
		assertEquals(0, metadada.info().getMinOccurs());
		assertEquals(1, metadada.info().getMaxOccurs());
		assertFalse(metadada.info().isExtended());
		// assertEquals("C0", Dialect.VERBOSE.serialize(metadada));
	}

	@Test(expected = MetadataParseException.class)
	public void testParseNameErrorTooLong() {
		parser.parseName(new ParseNode("name5678901234567890123456789012345678901234567890xyz", "", "", "", null));
	}

	@Test(expected = MetadataParseException.class)
	public void testParseNameErrorInvalidChar() {
		parser.parseName(new ParseNode("name@xyz", "", "", "", null));
	}

	@Test
	public void testParseSubtype() {
		assertEquals(Subtype.STRING, parser.parseSubtype(new ParseNode("", "", "", "", null)));
		assertEquals(Subtype.STRING, parser.parseSubtype(new ParseNode("", "S", "", "", null)));
		assertEquals(Subtype.STRING, parser.parseSubtype(NAME));
		assertEquals(Subtype.INTEGER, parser.parseSubtype(AGE));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseSubtypeError() {
		Metadata metadata = parser.parse(pplStringOf("(name:z20#0-1)"));
		assertMetadata(metadata, "name", Subtype.STRING, 20, 0, 1);
	}

	@Test
	public void testParseDomain() {
		Domain domain = parser.parseDomain(EXT);
		assertEquals("black", domain.items().get(0).value());
		assertEquals("white", domain.items().get(1).value());
		assertEquals("red", domain.items().get(2).value());

		ParseNode node1 = new ParseNode("color", "S", "10", "0-1", "[black,white,red]", "red", "K I test", null);
		Domain domain1 = parser.parseDomain(node1);
		assertEquals(domain, domain1);

		ParseNode node2 = new ParseNode("color", "S", "10", "0-1", "[\"bl'ack\",'wh,i\"te',red]", "red", "K I test",
				null);
		Domain domain2 = parser.parseDomain(node2);
		assertEquals("bl'ack", domain2.items().get(0).value());
		assertEquals("wh,i\"te", domain2.items().get(1).value());
		assertEquals("red", domain2.items().get(2).value());

		ParseNode nodeError = new ParseNode("color", "S", "10", "0-1", "['black','white,'red']", "red", "K I test",
				null);
		try {
			parser.parseDomain(nodeError);
			fail();
		} catch (MetadataParseException e) {
			assertTrue(e.getMessage().contains(BasicMetadataParser.INVALID_DOMAIN));
		}
	}

	@Test
	public void testParseComplexDomain() {
		parser.parseDomain(COMPLEX_EXT);
	}
}
