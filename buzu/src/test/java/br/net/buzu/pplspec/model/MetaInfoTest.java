package br.net.buzu.pplspec.model;

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
		return Domain.of(items);
	}

}
