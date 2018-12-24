package br.net.buzu.metaclass;

import br.net.buzu.annotation.PplIgnore;
import br.net.buzu.annotation.PplUse;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Douglas Siviotti
 *
 */
public class BasicSkipStrategyTest {

	@Test
	public void test() {
		Map<String, Field> fields = Arrays.asList(Sample.class.getDeclaredFields()).stream()
				.collect(Collectors.toMap(Field::getName, f -> f));

		BasicSkipStrategy skip= new BasicSkipStrategy();
		
		assertFalse(skip.skip(fields.get("field1")));
		assertTrue(skip.skip(fields.get("field2")));
		assertTrue(skip.skip(fields.get("staticField1")));
		assertFalse(skip.skip(fields.get("staticField2")));
		assertTrue(skip.skip(fields.get("transientField1")));
		assertFalse(skip.skip(fields.get("transientField2")));
		assertFalse(skip.skip(fields.get("sampleField1")));
		assertTrue(skip.skip(fields.get("sampleField2")));
		
	}
	
	@Test
	public void testDecorator() {
		Map<String, Field> fields = Arrays.asList(Sample.class.getDeclaredFields()).stream()
				.collect(Collectors.toMap(Field::getName, f -> f));

		BasicSkipStrategy skip= new BasicSkipStrategy(new AlwaysSkip());

		assertTrue(skip.skip(fields.get("field1"))); // IGNORE THIS (none annotation)
		assertTrue(skip.skip(fields.get("field2")));
		assertTrue(skip.skip(fields.get("staticField1")));
		assertFalse(skip.skip(fields.get("staticField2")));
		assertTrue(skip.skip(fields.get("transientField1")));
		assertFalse(skip.skip(fields.get("transientField2")));
		assertTrue(skip.skip(fields.get("sampleField1"))); // IGNORE THIS (none annotation)
		assertTrue(skip.skip(fields.get("sampleField2")));
		
	}

}

class UseType {

}

@PplIgnore
class IgnoreType{
	
}

class Sample {
	
	private int field1; // use
	
	@PplIgnore
	private int field2; // ignore (force)
	
	private static int staticField1;// ignore
	
	@PplUse
	private static int staticField2; // use (force)

	private transient int transientField1;// ignore
	
	@PplUse
	private transient int transientField2; // use (force)
	
	private UseType sampleField1; // use
	
	private IgnoreType sampleField2; // ignore by Type

	public int getField1() {
		return field1;
	}

	public void setField1(int field1) {
		this.field1 = field1;
	}

	public int getField2() {
		return field2;
	}

	public void setField2(int field2) {
		this.field2 = field2;
	}

	public static int getStaticField1() {
		return staticField1;
	}

	public static void setStaticField1(int staticField1) {
		Sample.staticField1 = staticField1;
	}

	public static int getStaticField2() {
		return staticField2;
	}

	public static void setStaticField2(int staticField2) {
		Sample.staticField2 = staticField2;
	}

	public int getTransientField1() {
		return transientField1;
	}

	public void setTransientField1(int transientField1) {
		this.transientField1 = transientField1;
	}

	public int getTransientField2() {
		return transientField2;
	}

	public void setTransientField2(int transientField2) {
		this.transientField2 = transientField2;
	}

	public UseType getSampleField1() {
		return sampleField1;
	}

	public void setSampleField1(UseType sampleField1) {
		this.sampleField1 = sampleField1;
	}

	public IgnoreType getSampleField2() {
		return sampleField2;
	}

	public void setSampleField2(IgnoreType sampleField2) {
		this.sampleField2 = sampleField2;
	}
	

}

class AlwaysSkip extends BasicSkipStrategy {

	@Override
	public boolean skip(Field field) {
		return true;
	}
	
}
