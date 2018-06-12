package br.net.buzu.context;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.net.buzu.metadata.SimpleStaticMetadata;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.MetaInfoTest;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.Subtype;

/**
 * TODO
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicMetadataFactoryTest {

	private BasicMetadataFactory factory;

	@Before
	public void before() {
		factory = new BasicMetadataFactory();
	}

	@Test
	public void testSimpleBasic() {
		MetaInfo metaInfo = new MetaInfo("", 0, "simpleBasic", Subtype.STRING, 10, 0, 0, 1);
		Metadata metadata = factory.create(metaInfo, null);
		assertEquals(SimpleStaticMetadata.class, metadata.getClass());
	}

	@Test
	public void testComplexBasic() {
	}

	@Test
	public void testSimpleExtended() {
		List<String> domain = MetaInfoTest.domain("Diamond", "Heart", "Club", "Spade");
		MetaInfo metaInfo = new MetaInfo("", 0, "simpleComplex", Subtype.STRING, 10, 0, 0, 1, domain, "Club", "K");
		Metadata metadata = factory.create(metaInfo, null);
		assertEquals(SimpleStaticMetadata.class, metadata.getClass());
	}

	@Test
	public void testComplexExtended() {
	}

}
