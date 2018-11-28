package br.net.buzu.metadata;

import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Subtype;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Test domainOf SimpleStaticMetadata
 * 
 * @author Dougla Siviotti
 *
 */
public class SimpleStaticMetadataTest {
	
	MetaInfo metaInfo = new MetaInfo("",31, "color", Subtype.STRING, 10, 0, 1, 2);
	SimpleStaticMetadata ssm1 = new SimpleStaticMetadata(metaInfo);
	SimpleStaticMetadata ssm2 = new SimpleStaticMetadata(metaInfo);
	
	@Test
	public void testIsStatic() {
		assertTrue(ssm1.isStatic());
	}

	@Test
	public void testMaxSerialSize() {
		assertEquals(20, ssm1.serialMaxSize());
	}
	
	@Test
	public void testEquals() {
		assertEquals(ssm1, ssm2);
		assertEquals(ssm2, ssm1);
		assertEquals(ssm1, ssm1);
		assertFalse(ssm1.equals(null));
	}
	
	@Test
	public void testHascode() {
		assertEquals(ssm1.hashCode(), ssm2.hashCode());
	}



}

