package br.net.buzu.parsing.filter;

import org.junit.Test;

import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.StaticMetadata;

public class ParserFilterTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}

class CustomFilter extends ParserFilter{

	public CustomFilter(PayloadParser next) {
		super(next);
	}

	@Override
	protected void beforeParse(StaticMetadata metadata, String text, Metaclass toClass) {
		
	}

	@Override
	protected <T> T onParse(Object parse) {
		return null;
	}

	@Override
	protected void afterParse(StaticMetadata metadata, String text, Metaclass toClass, Object t) {
		
	}

	@Override
	protected void beforeSerialize(Metadata metadata, Object obj) {
		
	}

	@Override
	protected String onSerialize(String serialize) {
		return null;
	}

	@Override
	protected void afterSerialize(Metadata metadata, Object obj, String s) {
		
	}
	
}