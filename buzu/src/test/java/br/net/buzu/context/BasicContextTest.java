package br.net.buzu.context;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Douglas Siviotti
 *
 */
public class BasicContextTest {
	
	@Test
	public void test() {
		BasicContext context = new BasicContext();
		
		// default members
		assertEquals(BasicSubtypeManager.class, context.subtypeManager().getClass());
		assertEquals(BasicMetadataFactory.class, context.metadataFactory().getClass());
		assertEquals(BasicMetadataCoderResolver.class, context.coderManager().getClass());
		assertEquals(BasicParserFactory.class, context.parserFactory().getClass());
	}
	





}
