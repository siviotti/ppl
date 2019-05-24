package br.net.buzu.pplspec.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Douglas Siviotti
 */
class PplExceptionTest {

    private fun assertPplException(e: PplException, message: String?, causeClass: Class<out Throwable>? = null) {
        if (message != null) {
            assertEquals(message, e.message)
        }
        if (causeClass != null) {
            if (e.cause != null) {
                assertEquals(causeClass, e.cause?.javaClass)
            }
        }

    }

    private fun assertPplException(e: PplException, causeClass: Class<out Throwable>) {
        assertPplException(e, null, causeClass)
    }

    @Test
    fun testPplException() {
        assertPplException(PplException("msg"), "msg")
        assertPplException(PplException(RuntimeException("msgRuntime")), RuntimeException::class.java)
        assertPplException(PplException("msg", RuntimeException("msgRuntime")), "msg", RuntimeException::class.java)
    }

    @Test
    fun testPplParseException() {
        val e = PplParseException("meta", "msg", String::class.java)
        assertPplException(PplParseException("msg"), "msg")
        assertPplException(PplParseException(e), PplParseException::class.java)
        assertPplException(PplParseException("msg", RuntimeException("msgRuntime")), "msg",
                RuntimeException::class.java)
    }

    @Test
    fun testPplSerializeException() {
        assertPplException(PplSerializeException("msg"), "msg")
        assertPplException(PplSerializeException(RuntimeException("msgRuntime")), RuntimeException::class.java)
        assertPplException(PplSerializeException("msg", RuntimeException("msgRuntime")), "msg",
                RuntimeException::class.java)
    }

    @Test
    fun testPplReflectionException() {
        assertPplException(PplReflectionException("method", String::class.java, RuntimeException()), RuntimeException::class.java)
        assertPplException(PplReflectionException("msg"), "msg")
        assertPplException(PplReflectionException(RuntimeException("msgRuntime")), RuntimeException::class.java)
        assertPplException(PplReflectionException("msg", RuntimeException("msgRuntime")), "msg",
                RuntimeException::class.java)
    }

    @Test
    fun testPplTypeMismatchException() {
        val e = PplTypeMismatchException(String::class.java, "string", Int::class.java, Byte::class.java)
        val message = e.message ?: ""
        assertTrue(message.contains("string"))
        assertPplException(PplTypeMismatchException("msg"), "msg")
        assertPplException(PplTypeMismatchException(RuntimeException("msgRuntime")), RuntimeException::class.java)
        assertPplException(PplTypeMismatchException("msg", RuntimeException("msgRuntime")), "msg",
                RuntimeException::class.java)
    }

    @Test
    fun testPplMetaclassViolationException() {
        assertPplException(PplMetaclassViolationException("msg"), "msg")
    }

    @Test
    fun testPplConstraintViolationException() {
        val e = PplConstraintViolationException("red", "[white,black]")
        val message = e.message ?: ""
        assertTrue(message.contains("red"))
        assertTrue(message.contains("[white,black]"))
        assertPplException(PplConstraintViolationException("msg"), "msg")
    }

}
