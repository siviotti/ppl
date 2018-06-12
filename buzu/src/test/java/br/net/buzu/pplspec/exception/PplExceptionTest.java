package br.net.buzu.pplspec.exception;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Douglas Siviotti
 *
 */
public class PplExceptionTest {

	private static void assertPplException(PplException e, String message, Class<? extends Throwable> causeClass) {
		if (message != null) {
			assertEquals(message, e.getMessage());
		}
		if (causeClass != null) {
			assertEquals(causeClass, e.getCause().getClass());
		}

	}

	private static void assertPplException(PplException e, Class<? extends Throwable> causeClass) {
		assertPplException(e, null, causeClass);
	}

	private static void assertPplException(PplException e, String message) {
		assertPplException(e, message, null);
	}

	@Test
	public void testPplException() {
		assertPplException(new PplException("msg"), "msg");
		assertPplException(new PplException(new RuntimeException("msgRuntime")), RuntimeException.class);
		assertPplException(new PplException("msg", new RuntimeException("msgRuntime")), "msg", RuntimeException.class);
	}

	@Test
	public void testPplParseException() {
		PplParseException e = new PplParseException("meta", "msg", String.class);
		assertPplException(new PplParseException("msg"), "msg");
		assertPplException(new PplParseException(e), PplParseException.class);
		assertPplException(new PplParseException("msg", new RuntimeException("msgRuntime")), "msg",
				RuntimeException.class);
	}

	@Test
	public void testPplSerializeException() {
		assertPplException(new PplSerializeException("msg"), "msg");
		assertPplException(new PplSerializeException(new RuntimeException("msgRuntime")), RuntimeException.class);
		assertPplException(new PplSerializeException("msg", new RuntimeException("msgRuntime")), "msg",
				RuntimeException.class);
	}

	@Test
	public void testPplReflectionException() {
		assertPplException(new PplReflectionException("method", String.class, new RuntimeException()), RuntimeException.class);
		assertPplException(new PplReflectionException("msg"), "msg");
		assertPplException(new PplReflectionException(new RuntimeException("msgRuntime")), RuntimeException.class);
		assertPplException(new PplReflectionException("msg", new RuntimeException("msgRuntime")), "msg",
				RuntimeException.class);
	}

	@Test
	public void testPplTypeMismatchException() {
		PplTypeMismatchException e = new PplTypeMismatchException(String.class, "string", Integer.class, Byte.class);
		assertTrue(e.getMessage().contains("string"));
		assertPplException(new PplTypeMismatchException("msg"), "msg");
		assertPplException(new PplTypeMismatchException(new RuntimeException("msgRuntime")), RuntimeException.class);
		assertPplException(new PplTypeMismatchException("msg", new RuntimeException("msgRuntime")), "msg",
				RuntimeException.class);
	}

	@Test
	public void testPplMetaclassViolationException() {
		assertPplException(new PplMetaclassViolationException("msg"), "msg");
	}

	@Test
	public void testPplConstraintViolationException() {
		PplConstraintViolationException e = new PplConstraintViolationException("red", "[white,black]");
		assertTrue(e.getMessage().contains("red"));
		assertTrue(e.getMessage().contains("[white,black]"));
		assertPplException(new PplConstraintViolationException("msg"), "msg");
	}

}
