package br.net.buzu.metadata;

import br.net.buzu.pplspec.model.*;
import br.net.buzu.util.StaticBehave;
import org.junit.Test;

import java.util.List;

import static br.net.buzu.pplspec.model.DomainKt.domainOf;
import static org.junit.Assert.*;

/**
 * @author Douglas Siviotti
 *
 */
public class AbstractMetadataTest {

	private static Metadata createSample(String name, Subtype subtype, int size, int minOccurs, int maxOccurs) {
		Domain domain = domainOf("white", "black", "red");
		MetaInfo metaInfo = new MetaInfo("", 31, name, subtype, size, 0, minOccurs, maxOccurs, domain, "red", "XYZ");
		return new GenericSimpleMetadata(Kind.ARRAY, metaInfo);

	}

	@Test(expected = NullPointerException.class)
	public void testNoKind() {
		new GenericSimpleMetadata(null, null);
	}

	@Test(expected = NullPointerException.class)
	public void testNoMetainfo() {
		new GenericSimpleMetadata(Kind.ARRAY, null);
	}

	@Test
	public void test() {
		Domain domain = domainOf("Diamons", "white", "black", "red");
		MetaInfo metaInfo = new MetaInfo("", 31, "test", Subtype.STRING, 10, 0, 1, 2, domain, "red", "XYZ");

		Metadata m1 = new GenericSimpleMetadata(Kind.RECORD, metaInfo);

		assertEquals("test", m1.name());
		assertEquals(Subtype.STRING, m1.info().getSubtype());
		assertEquals(10, m1.info().getSize()); // override MetaInfo
		assertEquals(0, m1.info().getScale());
		assertEquals(1, m1.info().getMinOccurs());
		assertEquals(true, m1.info().isRequired());
		assertEquals(2, m1.info().getMaxOccurs()); // override MetaInfo
	}

	@Test
	public void testEqualsHashcodeToString() {
		Metadata m1 = createSample("test", Subtype.STRING, 10, 1, 5);
		Metadata m2 = createSample("test", Subtype.STRING, 10, 1, 5);

		// assertEquals(m1, m2);
		assertTrue(m1.equals(m2));
		assertTrue(m1.hashCode() == m2.hashCode());
		assertTrue(m1.toString().equals(m2.toString()));
		assertTrue(m2.equals(m1));
		assertTrue(m1.equals(m1));
		assertFalse(m1.equals(null));
	}

}

class GenericSimpleMetadata extends BasicMetadata {

	public GenericSimpleMetadata(Kind kind, MetaInfo metaInfo) {
		super(metaInfo);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BasicMetadata> children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isStatic() {
		return StaticBehave.isStatic(this);
	}

}
