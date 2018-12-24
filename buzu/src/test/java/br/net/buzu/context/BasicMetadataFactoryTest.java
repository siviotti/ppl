package br.net.buzu.context;

import br.net.buzu.metadata.SimpleStaticMetadata;
import br.net.buzu.model.Domain;
import br.net.buzu.model.MetaInfo;
import br.net.buzu.model.Metadata;
import br.net.buzu.model.Subtype;
import org.junit.Before;
import org.junit.Test;

import static br.net.buzu.model.Domains.domainOf;
import static org.junit.Assert.assertEquals;

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
		MetaInfo metaInfo = new MetaInfo(0, "simpleBasic", Subtype.STRING, 10, 0, 0, 1);
		Metadata metadata = factory.create(metaInfo, null);
		assertEquals(SimpleStaticMetadata.class, metadata.getClass());
	}

	@Test
	public void testComplexBasic() {
	}

	@Test
	public void testSimpleExtended() {
		Domain domain = domainOf("Diamond", "Heart", "Club", "Spade");
		MetaInfo metaInfo = new MetaInfo( 0, "simpleComplex", Subtype.STRING, 10, 0, 0, 1, domain, "Club", "K");
		Metadata metadata = factory.create(metaInfo, null);
		assertEquals(SimpleStaticMetadata.class, metadata.getClass());
	}

	@Test
	public void testComplexExtended() {
	}

}
