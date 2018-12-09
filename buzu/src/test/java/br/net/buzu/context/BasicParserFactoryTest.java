package br.net.buzu.context;

import br.net.buzu.metaclass.SimpleMetaclass;
import br.net.buzu.parsing.simple.text.StringMapper;
import br.net.buzu.pplspec.api.PayloadMapper;
import br.net.buzu.pplspec.context.ParserFactory;
import br.net.buzu.pplspec.model.Kind;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Subtype;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
		PayloadMapper parser = parserFactory.create(metaclass);
		assertEquals(StringMapper.class, parser.getClass());
	}

}
