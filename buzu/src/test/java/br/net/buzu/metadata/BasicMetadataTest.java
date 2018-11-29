package br.net.buzu.metadata;

import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.PplString;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Douglas Siviotti
 *
 */
public class BasicMetadataTest {

	@Test
	public void test() {
		PplString pplString = new PplString("(name:S10;age:I3;city:S10)");
		Metadata bm1 = new BasicMetadataParser().parse(pplString);
		Metadata bm2 = new BasicMetadataParser().parse(pplString);
		
		assertTrue(bm1.equals(bm2));
		assertTrue(bm2.equals(bm1));
		assertTrue(bm2.hashCode() == bm1.hashCode());
		assertTrue(bm2.toString().equals(bm1.toString()));
		assertTrue(bm2.toTree(0).equals(bm1.toTree(0)));
		assertTrue(bm1.equals(bm1));
		assertFalse(bm1.equals(null));
		Object o = "abc";
		assertFalse(bm1.equals(o));
	}

}
