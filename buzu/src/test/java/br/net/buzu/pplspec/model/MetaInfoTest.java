package br.net.buzu.pplspec.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.net.buzu.pplspec.annotation.PplMetadata;
import br.net.buzu.pplspec.lang.Syntax;

/**
 * MetaInfo Unit Test.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class MetaInfoTest {
	
	private static String [] array = {"Diamond", "Heart", "Club", "Spade"};

	public static void assertMetainfo(MetaInfo metaInfo, String name, Subtype subtype, int size, int scale,
			int minOccurs, int maxOccurs) {
		assertEquals(name, metaInfo.name());
		assertEquals(size, metaInfo.size());
		assertEquals(scale, metaInfo.scale());
		assertEquals(subtype, metaInfo.subtype());
		assertEquals(minOccurs, metaInfo.minOccurs());
		assertEquals(maxOccurs, metaInfo.maxOccurs());
	}

	public static void assertMetainfo(MetaInfo metaInfo, String name, Subtype subtype, int size, int scale,
			int minOccurs, int maxOccurs, String defaultValue, Domain domain, String tags) {
		assertMetainfo(metaInfo, name, subtype, size, scale, minOccurs, maxOccurs);
		assertEquals(defaultValue, metaInfo.defaultValue());
		assertEquals(domain, metaInfo.domain());
		assertEquals(tags, metaInfo.tags());
	}

	public static Domain domain(String... items) {
		return Domain.create(items);
	}
	

	@Test(expected = NullPointerException.class)
	public void testMissingSubtype() {
		new MetaInfo("",0, null, null, 0, 0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidOccurs() {
		new MetaInfo("",0, null, Subtype.STRING, 0, 0, 5, 0); // 5 > 0, Unbounded
		new MetaInfo("",0, null, Subtype.STRING, 0, 0, 5, 4); // 5 > 4, invalid
	}

	@Test
	public void testEmptyMetaInfo() {
		MetaInfo metaInfo = new MetaInfo("",0, null, Subtype.STRING, 0, 0, 0, 0);
		assertFalse(metaInfo.hasDomain());
		assertMetainfo(metaInfo, null, Subtype.STRING, 0, 0, 0, 0, Syntax.EMPTY, Domain.EMPTY, Syntax.EMPTY);
		assertEquals("", metaInfo.id());
	}

	@Test
	public void testIncomplete() {
		MetaInfo metaInfo1 = new MetaInfo("",0, null, Subtype.STRING, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
				0, PplMetadata.EMPTY_INTEGER);
		assertMetainfo(metaInfo1, null, Subtype.STRING, PplMetadata.EMPTY_INTEGER, MetaInfo.NO_SCALE, 0,
				PplMetadata.EMPTY_INTEGER, Syntax.EMPTY, Domain.EMPTY, Syntax.EMPTY);
		assertFalse(metaInfo1.isComplete());
		assertFalse(metaInfo1.hasSize());
		assertFalse(metaInfo1.hasMaxOccurs());
		//
		MetaInfo metaInfo2 = new MetaInfo("",0, null, Subtype.STRING, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
				0, 5);
		assertMetainfo(metaInfo2, null, Subtype.STRING, PplMetadata.EMPTY_INTEGER, MetaInfo.NO_SCALE, 0, 5,
				Syntax.EMPTY, Domain.EMPTY, Syntax.EMPTY);
		assertFalse(metaInfo2.isComplete());
		assertFalse(metaInfo2.hasSize()); // PplMetadata.EMPTY_INTEGER
		assertTrue(metaInfo2.hasMaxOccurs()); // 5
		//
		MetaInfo metaInfo3 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 0, PplMetadata.EMPTY_INTEGER);
		assertMetainfo(metaInfo3, null, Subtype.STRING, 10, 0, 0, PplMetadata.EMPTY_INTEGER, Syntax.EMPTY,
				Domain.EMPTY, Syntax.EMPTY);
		assertFalse(metaInfo3.isComplete());
		assertTrue(metaInfo3.hasSize()); // PplMetadata.EMPTY_INTEGER
		assertFalse(metaInfo3.hasMaxOccurs()); // 5
	}

	@Test
	public void testCompleteMetaInfo() {
		MetaInfo metaInfo2 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 0, 5);
		assertMetainfo(metaInfo2, null, Subtype.STRING, 10, 0, 0, 5, Syntax.EMPTY, Domain.EMPTY, Syntax.EMPTY);
		assertTrue(metaInfo2.isComplete());
		assertTrue(metaInfo2.hasSize()); // 10
		assertTrue(metaInfo2.hasMaxOccurs()); // 5
	}

	@Test
	public void testRequired() {
		MetaInfo metaInfo1 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 0, 5);
		assertFalse(metaInfo1.isRequired()); // min = 0 - not required
		MetaInfo metaInfo2 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 1, 5);
		assertTrue(metaInfo2.isRequired()); // min = 1 = requered
		MetaInfo metaInfo3 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 5, 5);
		assertTrue(metaInfo3.isRequired()); // min = 5 = requered
	}

	@Test
	public void testMultiple() {
		MetaInfo metaInfo1 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 0, 5);
		assertTrue(metaInfo1.isMultiple()); // 5
		MetaInfo metaInfo2 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 0, 1);
		assertFalse(metaInfo2.isMultiple()); // 1
	}

	@Test
	public void testUnbounded() {
		MetaInfo metaInfo1 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 0, 0);
		assertTrue(metaInfo1.isMultiple()); // 5
		assertTrue(metaInfo1.isUnbounded()); // 0 = many

		MetaInfo metaInfo2 = new MetaInfo("",0, null, Subtype.STRING, 10, 0, 0, 1);
		assertFalse(metaInfo2.isMultiple()); // 1
		assertFalse(metaInfo2.isUnbounded()); // 1 = unique
	}

	@Test
	public void testDomain() {
		Domain domain = Domain.create(array);
		MetaInfo metaInfo = new MetaInfo("",31, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain,"Heart",  "XYZ");

		assertTrue(metaInfo.hasDomain());
		assertTrue(metaInfo.inDomain("Diamond"));
		assertTrue(metaInfo.inDomain("Heart"));
		assertTrue(metaInfo.inDomain("Club"));
		assertTrue(metaInfo.inDomain("Spade"));

		assertFalse(metaInfo.inDomain(null));
		assertFalse(metaInfo.inDomain(""));
		assertFalse(metaInfo.inDomain("xyz"));
		assertFalse(metaInfo.inDomain("club"));
		
		MetaInfo other = new MetaInfo("",31, "nameTest", Subtype.STRING, 100, 0, 1, 20, null, null, null);
		assertFalse(other.hasDomain());
		assertTrue(other.inDomain("Diamond"));
		assertTrue(other.domain().equals(Domain.EMPTY));
	}

	@Test
	public void testDefaultValue() {
		MetaInfo metaInfo = new MetaInfo("",31, "nameTest", Subtype.STRING, 100, 0, 1, 20, null, "DefaultValue", "XYZ");
		assertEquals("DefaultValue", metaInfo.defaultValue());
	}

	@Test
	public void testTags() {
		MetaInfo metaInfo = new MetaInfo("",31, "nameTest", Subtype.STRING, 100, 0, 1, 20, null, null, "XYZ");

		assertTrue(metaInfo.isTagPresent("X"));
		assertTrue(metaInfo.isTagPresent("XY"));
		assertTrue(metaInfo.isTagPresent("XYZ"));

		assertFalse(metaInfo.isTagPresent("XZ"));
		assertFalse(metaInfo.isTagPresent(null));
		
		MetaInfo other = new MetaInfo("",31, "nameTest", Subtype.STRING, 100, 0, 1, 20, null, null, null);
		assertFalse(other.isTagPresent("X"));
		assertTrue(other.tags()==Syntax.EMPTY);
	}

	@Test
	public void testComplete() {
		Domain domain = Domain.create(array);
		MetaInfo metaInfo = new MetaInfo("",1, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ");
		assertMetainfo(metaInfo, "nameTest", Subtype.STRING, 100, 0, 1, 20, "Heart", domain, "XYZ");

		MetaInfo copy = new MetaInfo("",1, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain, "Heart",  "XYZ");
		MetaInfo copy2 = new MetaInfo("",1, "nameTest", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ");
		MetaInfo copy3 = new MetaInfo("",9, "NOT-EQUALS", Subtype.STRING, 100, 0, 1, 20, domain, "Heart", "XYZ");
		// equals, hascode and toString
		assertEquals(metaInfo, copy);
		assertNotSame(metaInfo, copy3);
		assertTrue(metaInfo.equals(metaInfo)); // reflexive
		assertTrue(metaInfo.equals(copy) && copy.equals(metaInfo)); // Symmetry
		assertTrue(copy.equals(metaInfo) && copy2.equals(metaInfo) && copy.equals(copy2)); // Transitive
		assertTrue(metaInfo.equals(copy) && metaInfo.equals(copy) && metaInfo.equals(copy)); // Consistent
		assertFalse(metaInfo.equals(null));
		Object o = new String("abc");
		assertFalse(metaInfo.equals(o));
		// compareTo
		assertTrue(copy.compareTo(copy2)==0);
		assertTrue(copy.compareTo(copy3)==-1);
		assertTrue(copy3.compareTo(copy)==1);
		// hashcode
		assertEquals(metaInfo.hashCode(), copy.hashCode());
		assertEquals(metaInfo.toString(), copy.toString());
		// Boolean API
		assertTrue(metaInfo.isMultiple());
		assertTrue(metaInfo.isMultiple());
	}
	

}
