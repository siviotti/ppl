package br.net.buzu.context;

import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.lang.Syntax;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit Test for ContextBuilder
 * 
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585
 * @since 13 de abr de 2018 - Construção da Duimp (Release 1)
 *
 */
public class ContextBuilderTest {

	private ContextBuilder contextBuilder;

	@Before
	public void before() {
		contextBuilder = new ContextBuilder();
	}

	@Test(expected = NullPointerException.class)
	public void testSubtypeManager() {
		contextBuilder.subtypeManager(null);
	}

	@Test(expected = NullPointerException.class)
	public void testMetadataFactorySyntax() {
		contextBuilder.metadataFactory(null);
	}

	@Test(expected = NullPointerException.class)
	public void testCoderManager() {
		contextBuilder.coderManager(null);
	}

	@Test(expected = NullPointerException.class)
	public void testParserFactorySyntax() {
		contextBuilder.parserFactory(null);
	}

	@Test
	public void testSimpleBuild() {
		PplContext context = contextBuilder.build();
		assertEquals(BasicContext.class, context.getClass());
	}

	@Test
	public void testCustomBuild() {
		PplContext context = contextBuilder.metadataFactory(new CustomMetadataFactory()).coderManager(new CustomCoderManager())
				.subtypeManager(new CustomSubtypeManager()).parserFactory(new CustomParserFactory()).build();
		assertEquals(BasicContext.class, context.getClass());

		assertEquals(CustomSubtypeManager.class, context.subtypeManager().getClass());
		assertEquals(CustomMetadataFactory.class, context.metadataFactory().getClass());
		assertEquals(CustomParserFactory.class, context.parserFactory().getClass());
	}

}



class CustomSubtypeManager extends BasicSubtypeManager {
}

class CustomMetadataFactory extends BasicMetadataFactory {
}

class CustomCoderManager extends BasicCoderManager {
}

class CustomParserFactory extends BasicParserFactory {
}