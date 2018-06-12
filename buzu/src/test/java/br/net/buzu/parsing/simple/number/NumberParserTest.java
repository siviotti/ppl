package br.net.buzu.parsing.simple.number;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class NumberParserTest {

	BasicMetaclassReader reader = new BasicMetaclassReader();
	NumberParser parser = NumberParser.INSTANCE;
	Metaclass bigDecimalClass = reader.read(BigDecimal.class);

	@Test
	public void testNoScale() {
		String text = "123.456";
		BigDecimal number = new BigDecimal(text);
		assertEquals(text, number.toPlainString());
		StaticMetadata metadata = (StaticMetadata) new BasicMetadataParser().parse("(N6)");
		// Parse
		BigDecimal parsedNumber = (BigDecimal) parser.asSingleObject(metadata, text, bigDecimalClass);	
		assertEquals(number, parsedNumber);
		assertEquals(text, parsedNumber.toPlainString());
		// Serialize
		String serializedText = parser.asStringFromNotNull(metadata.info(), number);
		assertEquals(text, serializedText);
		assertEquals(number, new BigDecimal(serializedText));
	}

	@Test
	public void testScale() {
		String text = "123.456";
		BigDecimal number = new BigDecimal(text);
		assertEquals(text, number.toPlainString());
		StaticMetadata metadata = (StaticMetadata) new BasicMetadataParser().parse("(N6,3)");
		// Parse
		BigDecimal parsedNumber = (BigDecimal) parser.asSingleObject(metadata, text, bigDecimalClass);	
		assertEquals(number, parsedNumber);
		assertEquals(text, parsedNumber.toPlainString());
		assertEquals(3, parsedNumber.scale());
		// Serialize
		String serializedText = parser.asStringFromNotNull(metadata.info(), number);
		assertEquals(text, serializedText);
		assertEquals(number, new BigDecimal(serializedText));
		// Complete Zero
		assertEquals("123.400", parser.asStringFromNotNull(metadata.info(), new BigDecimal("123.4")));
		// Out of Range - ignore
		assertEquals("1234.123", parser.asStringFromNotNull(metadata.info(), new BigDecimal("1234.123")));
		
	}

	@Test
	public void testTruncateScale() {
		String text = "123.456";
		BigDecimal number = new BigDecimal(text);
		assertEquals(text, number.toPlainString());
		StaticMetadata metadata = (StaticMetadata) new BasicMetadataParser().parse("(N6,2)"); // Truncate!
		// Parse
		BigDecimal parsedNumber = (BigDecimal) parser.asSingleObject(metadata, text, bigDecimalClass);	
		assertEquals(new BigDecimal("123.45"), parsedNumber);
		assertEquals("123.45", parsedNumber.toPlainString());
		assertEquals(2, parsedNumber.scale());
		// Serialize
		String serializedText = parser.asStringFromNotNull(metadata.info(), number);
		assertEquals("123.45", serializedText);
		assertEquals(new BigDecimal("123.45"), new BigDecimal(serializedText));
	}

}
