package br.net.buzu.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.net.buzu.pplspec.model.Domain;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.Subtype;

/**
 * @author Douglas Siviotti
 *
 */
public class ComplexMetadataTest {

	public static ComplexMetadata createSample(String name, Subtype subtype, int size, int scale, int minOccurs,
			int maxOccurs, Domain domain, String defaultValue, String tags, List<Metadata> children) {
		MetaInfo metaInfo = new MetaInfo("",31, name, subtype, size, 0, minOccurs, maxOccurs, domain, defaultValue, tags);
		return new ComplexMetadata(metaInfo, children);

	}

	static ComplexMetadata createSample(String name, Subtype subtype, int size, int minOccurs, int maxOccurs) {
		Domain domain = Domain.of("white", "black", "red");
		return createSample(name, subtype, size, 0, minOccurs, maxOccurs, domain, "red", "XYZ", null);

	}

	@Test
	public void test() {
		SimpleMetadata m1 = SimpleMetadataTest.createSample("atomic", Subtype.CHAR, 10, 0, 1);
		SimpleMetadata m2 = SimpleMetadataTest.createSample("array", Subtype.CHAR, 10, 1, 2);
		List<Metadata> children = new ArrayList<>();
		children.add(m1);
		children.add(m2);
		ComplexMetadata c1 = createSample("complex", Subtype.OBJ, 0, 0, 0, 1, null, null, null, children);

		assertEquals(m1, c1.children().get(0));
		assertEquals(m2, c1.children().get(1));
		//assertEquals(30, c1.serialMaxSize()); // m1 = 10 m2 = 20

		// Basic Methods
		ComplexMetadata c2 = createSample("complex", Subtype.OBJ, 0, 0, 0, 1, null, null, null, children);

		assertTrue(c1.equals(c2));
		assertTrue(c1.hashCode() == c2.hashCode());
		assertTrue(c1.toString().equals(c2.toString()));
		assertTrue(c2.equals(c1));
		assertTrue(c1.equals(c1));
		assertFalse(c1.equals(null));
		Object o = new String("abc");
		assertFalse(c1.equals(o));
	}

}
