package br.net.buzu.metadata;

import br.net.buzu.context.BasicMetadataFactory;
import br.net.buzu.model.Domain;
import br.net.buzu.model.Kind;
import br.net.buzu.model.MetaInfo;
import br.net.buzu.model.Subtype;
import org.junit.Test;

import static br.net.buzu.model.Domains.domainOf;
import static org.junit.Assert.*;

/**
 * @author Douglas Siviotti
 *
 */
public class SimpleMetadataTest {

	public static SimpleMetadata createSample(String name, Subtype subtype, int size, int scale, int minOccurs,
			int maxOccurs, Domain domain, String defaultValue, String tags) {
		MetaInfo metaInfo = new MetaInfo(31, name, subtype, size, 0, minOccurs, maxOccurs, domain, defaultValue, tags);
		return (SimpleMetadata) new BasicMetadataFactory().create(metaInfo, null);

	}

	static SimpleMetadata createSample(String name, Subtype subtype, int size, int minOccurs, int maxOccurs) {
		Domain domain = domainOf("white", "black", "red");
		return createSample(name, subtype, size, 0, minOccurs, maxOccurs, domain, "red", "XYZ");

	}

	@Test
	public void testAtomic() {
		SimpleStaticMetadata m1 = (SimpleStaticMetadata) createSample("item", Subtype.CHAR, 10, 0, 1);

		assertEquals(Kind.ATOMIC, m1.kind());
		assertEquals(false, m1.info().isRequired());
		assertEquals(10, m1.serialMaxSize()); // 1 x 10
	}

	@Test
	public void testArray() {
		SimpleStaticMetadata m2 = (SimpleStaticMetadata) createSample("list", Subtype.CHAR, 10, 1, 2);

		assertEquals(Kind.ARRAY, m2.kind());
		assertEquals(true, m2.info().isRequired());
		assertEquals(20, m2.serialMaxSize()); // 2 x 10
	}

	@Test
	public void testBasicMethods() {
		SimpleMetadata m1 = createSample("item", Subtype.CHAR, 10, 0, 1);
		SimpleMetadata m2 = createSample("item", Subtype.CHAR, 10, 0, 1);

		assertTrue(m1.equals(m2));
		assertTrue(m1.hashCode() == m2.hashCode());
		assertTrue(m1.toString().equals(m2.toString()));
		assertTrue(m2.equals(m1));
		assertTrue(m1.equals(m1));
		assertFalse(m1.equals(null));
	}

}
