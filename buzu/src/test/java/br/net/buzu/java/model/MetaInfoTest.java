package br.net.buzu.java.model;

import static br.net.buzu.java.model.Domains.domainOf;
import static org.junit.Assert.assertEquals;
/**
 * MetaInfo Unit Test.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class MetaInfoTest {

	public static void assertMetainfo(MetaInfo metaInfo, String name, Subtype subtype, int size, int scale,
			int minOccurs, int maxOccurs) {
		assertEquals(name, metaInfo.getName());
		assertEquals(size, metaInfo.getSize());
		assertEquals(scale, metaInfo.getScale());
		assertEquals(subtype, metaInfo.getSubtype());
		assertEquals(minOccurs, metaInfo.getMinOccurs());
		assertEquals(maxOccurs, metaInfo.getMaxOccurs());
	}

	public static void assertMetainfo(MetaInfo metaInfo, String name, Subtype subtype, int size, int scale,
			int minOccurs, int maxOccurs, String defaultValue, Domain domain, String tags) {
		assertMetainfo(metaInfo, name, subtype, size, scale, minOccurs, maxOccurs);
		assertEquals(defaultValue, metaInfo.getDefaultValue());
		assertEquals(domain, metaInfo.getDomain());
		assertEquals(tags, metaInfo.getTags());
	}

	public static Domain domain(String... items) {
		return domainOf(items);
	}

}
