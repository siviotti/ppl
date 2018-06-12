package br.net.buzu.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.net.buzu.metadata.ComplexStaticMetadada;
import br.net.buzu.metadata.SimpleStaticMetadata;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.Subtype;

/**
 * @author Douglas siviotti
 *
 */
public class ComplexStaticMetadataTest {

	static MetaInfo metaInfo = new MetaInfo("",31, "color", Subtype.STRING, 10, 0, 1, 2);
	static SimpleStaticMetadata ssm1 = new SimpleStaticMetadata(metaInfo);
	static SimpleStaticMetadata ssm2 = new SimpleStaticMetadata(metaInfo);
	static List<Metadata> children = new ArrayList<>();
	static ComplexStaticMetadada cmm1;
	static ComplexStaticMetadada cmm2;
	
	static {
		children.add(ssm1);
		children.add(ssm2);
		
		cmm1 = new ComplexStaticMetadada(metaInfo, children);
		cmm2 = new ComplexStaticMetadada(metaInfo, children);
	}
	
	
	@Test
	public void testIsStatic() {
		assertTrue(cmm1.isStatic());
	}

	@Test
	public void testMaxSerialSize() {
		assertEquals(80, cmm1.serialMaxSize());
	}
	
	@Test
	public void testEquals() {
		assertEquals(cmm1, cmm2);
		assertEquals(cmm2, cmm1);
		assertEquals(cmm1, cmm1);
		assertFalse(cmm1.equals(null));
	}
	
	@Test
	public void testHascode() {
		assertEquals(cmm1.hashCode(), cmm2.hashCode());
	}

}
