package br.net.buzu.metaclass;

import br.net.buzu.java.annotation.PplIgnore;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Douglas Siviotti
 *
 */
public class AnnotationSkipStrategyTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testOverSample() {
		Map<String, Field> fields = Arrays.asList(Sample.class.getDeclaredFields()).stream()
				.collect(Collectors.toMap(Field::getName, f -> f));

		AnnotationSkipStrategy skip = new AnnotationSkipStrategy();

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
	public void testOverCustom() {
		Map<String, Field> fields = Arrays.asList(Custom.class.getDeclaredFields()).stream()
				.collect(Collectors.toMap(Field::getName, f -> f));
		
		Set<Class<? extends Annotation>> ignoreAnnotations = new HashSet<>();
		ignoreAnnotations.add(CustomIgnore.class);
		Set<Class<? extends Annotation>> useAnnotations = new HashSet<>();
		useAnnotations.add(CustomUse.class);
		
		AnnotationSkipStrategy skip = new AnnotationSkipStrategy(ignoreAnnotations, useAnnotations);

		assertFalse(skip.skip(fields.get("field1")));
		assertTrue(skip.skip(fields.get("field2")));
		assertTrue(skip.skip(fields.get("staticField1")));
		assertFalse(skip.skip(fields.get("staticField2")));
		assertTrue(skip.skip(fields.get("transientField1")));
		assertFalse(skip.skip(fields.get("transientField2")));
		assertFalse(skip.skip(fields.get("sampleField1")));
		assertTrue(skip.skip(fields.get("sampleField2")));

	}

}

/** Custom annotation to force use a Field. */
@Target({ FIELD, TYPE })
@Retention(RetentionPolicy.RUNTIME)
@interface CustomUse {

}

/** Custom annotation to ignore a Field. */
@Target({ FIELD, TYPE })
@Retention(RetentionPolicy.RUNTIME)
@interface CustomIgnore {

}

class NormalType {

}

@PplIgnore
class PplIgnoreType {

}

class Custom {

	private int field1; // use

	@CustomIgnore
	private int field2; // ignore (force)

	private static int staticField1;// ignore

	@CustomUse
	private static int staticField2; // use (force)

	private transient int transientField1;// ignore

	@CustomUse
	private transient int transientField2; // use (force)

	private NormalType sampleField1; // use

	private PplIgnoreType sampleField2; // ignore by Type

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
		Custom.staticField1 = staticField1;
	}

	public static int getStaticField2() {
		return staticField2;
	}

	public static void setStaticField2(int staticField2) {
		Custom.staticField2 = staticField2;
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

	public NormalType getSampleField1() {
		return sampleField1;
	}

	public void setSampleField1(NormalType sampleField1) {
		this.sampleField1 = sampleField1;
	}

	public PplIgnoreType getSampleField2() {
		return sampleField2;
	}

	public void setSampleField2(PplIgnoreType sampleField2) {
		this.sampleField2 = sampleField2;
	}

}
