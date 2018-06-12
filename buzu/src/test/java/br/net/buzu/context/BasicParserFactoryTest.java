package br.net.buzu.context;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.net.buzu.metaclass.SimpleMetaclass;
import br.net.buzu.parsing.simple.text.StringParser;
import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.context.ParserFactory;
import br.net.buzu.pplspec.model.Kind;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Subtype;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class BasicParserFactoryTest {

	@Test
	public void test() {
		ParserFactory parserFactory = new BasicParserFactory();
		MetaInfo metaInfo = new MetaInfo("", 0, "testname", Subtype.STRING, 10,0, 0, 1);
		Metaclass metaclass = new SimpleMetaclass(null, String.class, String.class, Kind.ATOMIC, metaInfo, null);
		PayloadParser parser = parserFactory.create(metaclass);
		assertEquals(StringParser.class, parser.getClass());
	}

}
