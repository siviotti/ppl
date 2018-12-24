package br.net.buzu.metadata.build;

import br.net.buzu.BuzuTest;
import br.net.buzu.context.BasicContext;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.load.BasicMetadataLoader;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.model.Metadata;
import br.net.buzu.model.PplString;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class MetadataBuilderTest {

	private MetadataBuilder builder;

	@Before
	public void before() {
		builder = new MetadataBuilder();
	}

	@Test
	public void testFromPplString() {
		Metadata metadata = builder.build(new PplString(BuzuTest.PERSON_PPL_STRING));
		assertEquals(BuzuTest.PERSON_METADATA_NAMES.length, metadata.children().size());
		for (int i = 0; i < BuzuTest.PERSON_METADATA_NAMES.length; i++) {
			assertEquals(BuzuTest.PERSON_METADATA_NAMES[i], metadata.children().get(i).info().getName());
		}
	}

	@Test
	public void testFromObject() {
		Metadata metadata = builder.build(BuzuTest.PERSON_INSTANCE);
		assertEquals(BuzuTest.PERSON_METADATA_NAMES.length, metadata.children().size());
		for (int i = 0; i < BuzuTest.PERSON_METADATA_NAMES.length; i++) {
			assertEquals(BuzuTest.PERSON_METADATA_NAMES[i], metadata.children().get(i).info().getName());
		}
	}

	@Test
	public void testFromLayout() {
		Metadata metadata = builder.build(LayoutTest.createOrderLayout());
		assertEquals(LayoutTest.ORDER_CHILDREN_COUNT, metadata.children().size());
	}

	@Test
	public void testCustomBuilder() {
		MetadataBuilder builder = new MetadataBuilder(new BasicContext(), new BasicMetadataLoader(), new BasicMetadataParser(), new BasicMetaclassReader());
		assertEquals(BasicMetadataLoader.class, builder.getLoader().getClass());
		assertEquals(BasicMetadataParser.class, builder.getParser().getClass());
		assertEquals(BasicMetaclassReader.class, builder.getReader().getClass());

		Metadata metadata = builder.build(BuzuTest.PERSON_INSTANCE);
		assertEquals(BuzuTest.PERSON_METADATA_NAMES.length, metadata.children().size());
		for (int i = 0; i < BuzuTest.PERSON_METADATA_NAMES.length; i++) {
			assertEquals(BuzuTest.PERSON_METADATA_NAMES[i], metadata.children().get(i).info().getName());
		}

	}

}
