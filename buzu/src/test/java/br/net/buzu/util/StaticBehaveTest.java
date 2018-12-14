package br.net.buzu.util;

import br.net.buzu.context.BasicContext;
import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.Subtype;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Douglas Siviotti
 *
 */
public class StaticBehaveTest {

	private PplContext context = new BasicContext();
	private MetaInfo staticMetaInfo = new MetaInfo( 11, "color", Subtype.STRING, 10, 0, 1, 2);
	private MetaInfo uncompleteMetaInfo = new MetaInfo( 11, "color", Subtype.STRING, -1, 0, 1, 2);
	private MetaInfo unboundedMetaInfo = new MetaInfo( 11, "color", Subtype.STRING, 10, 0, 1, 0);

	@Test
	public void testCheckMetainfo() {
		StaticBehave.checkStaticInfo(staticMetaInfo);
		try {
			StaticBehave.checkStaticInfo(uncompleteMetaInfo);
			fail();
		} catch (IllegalArgumentException iae) {
			assertTrue(iae.getMessage().startsWith(StaticBehave.META_INFO_MUST_BE_COMPLETE));
		}

		try {
			StaticBehave.checkStaticInfo(unboundedMetaInfo);
			fail();
		} catch (IllegalArgumentException iae) {
			assertTrue(iae.getMessage().startsWith(StaticBehave.META_INFO_CANNOT_BE_UNBOUNDED));
		}
	}

	@Test
	public void testChild() {
		Metadata child1 = context.metadataFactory().create(staticMetaInfo, null);
		StaticBehave.checkStaticChild(child1);


		Metadata child2 = context.metadataFactory().create(uncompleteMetaInfo, null);
		try {
			StaticBehave.checkStaticChild(child2);
			fail();
		} catch (IllegalArgumentException iae) {
			assertTrue(iae.getMessage().startsWith(StaticBehave.INVALID_STATIC_CHILD));
		}

		Metadata child3 = context.metadataFactory().create(unboundedMetaInfo, null);
		try {
			StaticBehave.checkStaticChild(child3);
			fail();
		} catch (IllegalArgumentException iae) {
			assertTrue(iae.getMessage().startsWith(StaticBehave.INVALID_STATIC_CHILD));
		}

	}

}
