package br.net.buzu.metaclass;

import br.net.buzu.pplspec.model.Kind;
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
public class SimpleStaticMetaclassTest {
	
	MetaInfo metaInfo = new MetaInfo("",31, "color", Subtype.STRING, 10, 0, 1, 2);
	SimpleStaticMetaclass ssm1 = new SimpleStaticMetaclass(null, String.class, String.class, Kind.ATOMIC, metaInfo, null);
	SimpleStaticMetaclass ssm2 = new SimpleStaticMetaclass(null, String.class, String.class, Kind.ATOMIC, metaInfo, null);
	
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

