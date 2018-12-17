package br.net.buzu.metadata;

import br.net.buzu.java.lang.Syntax;
import br.net.buzu.java.model.Domain;
import br.net.buzu.java.model.MetaInfo;
import br.net.buzu.java.model.Metadata;
import br.net.buzu.java.model.Subtype;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static br.net.buzu.java.model.Domains.domainOf;
import static org.junit.Assert.*;

/**
 * @author Douglas Siviotti
 *
 */
public class ComplexMetadataTest {

	public static ComplexMetadata createSample(String name, Subtype subtype, int size, int scale, int minOccurs,
			int maxOccurs, Domain domain, String defaultValue, String tags, List<Metadata> children) {
		MetaInfo metaInfo = new MetaInfo(31, name, subtype, size, 0, minOccurs, maxOccurs, domain, defaultValue, tags);
		return new ComplexMetadata(metaInfo, children);

	}

	static ComplexMetadata createSample(String name, Subtype subtype, int size, int minOccurs, int maxOccurs) {
		Domain domain = domainOf("white", "black", "red");
		return createSample(name, subtype, size, 0, minOccurs, maxOccurs, domain, "red", "XYZ", null);

	}

	@Test
	public void test() {
		SimpleMetadata m1 = SimpleMetadataTest.createSample("atomic", Subtype.CHAR, 10, 0, 1);
		SimpleMetadata m2 = SimpleMetadataTest.createSample("array", Subtype.CHAR, 10, 1, 2);
		List<Metadata> children = new ArrayList<>();
		children.add(m1);
		children.add(m2);
		ComplexMetadata c1 = createSample("complex", Subtype.OBJ, 0, 0, 0, 1, Domain.Companion.getEMPTY(), Syntax.EMPTY, Syntax.EMPTY, children);

		assertEquals(m1, c1.children().get(0));
		assertEquals(m2, c1.children().get(1));
		//assertEquals(30, c1.serialMaxSize()); // m1 = 10 m2 = 20

		// Basic Methods
		ComplexMetadata c2 = createSample("complex", Subtype.OBJ, 0, 0, 0, 1, Domain.Companion.getEMPTY(), Syntax.EMPTY, Syntax.EMPTY, children);

		assertTrue(c1.equals(c2));
		assertTrue(c1.hashCode() == c2.hashCode());
		assertTrue(c1.toString().equals(c2.toString()));
		assertTrue(c2.equals(c1));
		assertTrue(c1.equals(c1));
		assertFalse(c1.equals(null));
		Object o = "abc";
		assertFalse(c1.equals(o));
	}

}
