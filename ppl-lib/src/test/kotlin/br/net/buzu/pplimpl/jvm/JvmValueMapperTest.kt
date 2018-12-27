package br.net.buzu.pplimpl.jvm

import br.net.buzu.lang.EMPTY
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Subtype
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

internal class JvmValueMapperTest {

    val metaInfo = MetaInfo(0, EMPTY, Subtype.STRING, 0, 0, 0, 0)

    // TEXT

    @Test
    fun testOneCharMapper() {
        assertEquals('1', OneCharMapper.toValue("1", metaInfo))
        assertEquals('A', OneCharMapper.toValue("A", metaInfo))
        assertEquals('1', OneCharMapper.toValue("123 ", metaInfo))
        assertEquals(' ', OneCharMapper.toValue("  1 ", metaInfo))

        assertEquals("1", OneCharMapper.toText('1'))
        assertEquals(" ", OneCharMapper.toText(' '))
    }

    @Test
    fun testCharMapper() {
        assertEquals("123", CharMapper.toValue("123", metaInfo))
        assertEquals(" 123", CharMapper.toValue(" 123", metaInfo))
        assertEquals("123 ", CharMapper.toValue("123 ", metaInfo))
        assertEquals(" 123 ", CharMapper.toValue(" 123 ", metaInfo))
        assertEquals(" 123 ", CharMapper.toValue(" 123 ", metaInfo))

        assertEquals("123", CharMapper.toText("123"))
        assertEquals(" 123", CharMapper.toText(" 123"))
        assertEquals("123 ", CharMapper.toText("123 "))
        assertEquals(" 123 ", CharMapper.toText(" 123 "))
    }

    @Test
    fun testStringMapper() {
        assertEquals("123", StringMapper.toValue("123", metaInfo))
        assertEquals("123", StringMapper.toValue(" 123", metaInfo))
        assertEquals("123", StringMapper.toValue("123 ", metaInfo))
        assertEquals("123", StringMapper.toValue(" 123 ", metaInfo))
        assertEquals("123", StringMapper.toText("123"))
        assertEquals(" 123", StringMapper.toText(" 123"))
        assertEquals("123 ", StringMapper.toText("123 "))
        assertEquals(" 123 ", StringMapper.toText(" 123 "))
    }

    // DECIMAL

    @Test
    fun testDoubleMapper() {
        assertEquals(23.78, DoubleMapper.toValue("23.78", metaInfo))
        assertEquals("23.78", DoubleMapper.toText(23.78))
    }

    @Test
    fun testFloatMapper() {
        assertEquals(23.78f, FloatMapper.toValue("23.78", metaInfo))
        assertEquals("23.78", FloatMapper.toText(23.78f))
    }

    @Test
    fun testBigDecimalMapper() {
        val metaInfo = MetaInfo(0, EMPTY, Subtype.STRING, 0, 2, 0, 0)
        assertEquals(BigDecimal("23.78"), BigDecimalMapper.toValue("23.78", metaInfo))
        assertEquals("23.78", BigDecimalMapper.toText(BigDecimal("23.78")))
    }

    // INTEGER

    @Test
    fun testIntegerMapper() {
        assertEquals(123, IntegerMapper.toValue("123", metaInfo))
        assertEquals("123", IntegerMapper.toText(123))
    }

    // LONG

    @Test
    fun testLongMapper() {
        assertEquals(123L, LongMapper.toValue("123", metaInfo))
        assertEquals("123", LongMapper.toText(123L))
    }
    @Test
    fun testBigIntegerMapper() {
        val bigInt = BigInteger.valueOf(123L)
        assertEquals(bigInt, BigIntegerMapper.toValue("123", metaInfo))
        assertEquals("123", BigIntegerMapper.toText(bigInt))
    }

    // BOOLEAN

    @Test
    fun testBooleanMapper() {
        assertEquals(true, BooleanMapper.toValue("true", metaInfo))
        assertEquals(true, BooleanMapper.toValue("True", metaInfo))
        assertEquals(true, BooleanMapper.toValue("TRUE", metaInfo))
        assertEquals(false, BooleanMapper.toValue("false", metaInfo))
        assertEquals(false, BooleanMapper.toValue("etc", metaInfo))
        assertEquals("true", BooleanMapper.toText(true))
        assertEquals("false", BooleanMapper.toText(false))
        assertEquals("true", BooleanMapper.toText(1==1))
        assertEquals("false", BooleanMapper.toText(1==2))
    }

    @Test
    fun testBozMapper() {
        assertEquals(true, BozMapper.toValue("1", metaInfo))
        assertEquals("1", BozMapper.toText(true))
        assertEquals(false, BozMapper.toValue("0", metaInfo))
        assertEquals("0", BozMapper.toText(false))
        assertEquals(false, BozMapper.toValue("X", metaInfo))
    }

    @Test
    fun testBtfMapper() {
        assertEquals(true, BtfMapper.toValue("T", metaInfo))
        assertEquals("T", BtfMapper.toText(true))
        assertEquals(false, BtfMapper.toValue("F", metaInfo))
        assertEquals("F", BtfMapper.toText(false))
        assertEquals(false, BtfMapper.toValue("X", metaInfo))
    }

    @Test
    fun testBynMapper() {
        assertEquals(true, BynMapper.toValue("Y", metaInfo))
        assertEquals("Y", BynMapper.toText(true))
        assertEquals(false, BynMapper.toValue("N", metaInfo))
        assertEquals("N", BynMapper.toText(false))
        assertEquals(false, BynMapper.toValue("X", metaInfo))
    }

    @Test
    fun testBsnMapper() {
        assertEquals(true, BsnMapper.toValue("S", metaInfo))
        assertEquals("S", BsnMapper.toText(true))
        assertEquals(false, BsnMapper.toValue("N", metaInfo))
        assertEquals("N", BsnMapper.toText(false))
        assertEquals(false, BsnMapper.toValue("X", metaInfo))
    }

    // DATE

    @Test
    fun testDateMapper() {
        val date = LocalDate.of(2018, 12, 26)
        assertEquals(date, DateMapper.toValue("20181226", metaInfo))
        assertEquals("20181226", DateMapper.toText(date))
    }

    @Test
    fun testIsoDateMapper() {
        val date = LocalDate.of(2018, 12, 26)
        assertEquals(date, IsoDateMapper.toValue("2018-12-26", metaInfo))
        assertEquals("2018-12-26", IsoDateMapper.toText(date))
    }

    @Test
    fun testUtcDateMapper() {
        val date = LocalDate.of(2018, 12, 26)
        assertEquals(date, UtcDateMapper.toValue("2018-12-26+03:00", metaInfo))
        //TODO
        //assertEquals("2018-12-26+03:00", UtcDateMapper.toText(date))
    }

    // TIME

    @Test
    fun testTimeMapper() {
        val date = LocalTime.of(11, 22, 33)
        assertEquals(date, TimeMapper.toValue("112233", metaInfo))
        assertEquals("112233", TimeMapper.toText(date))
    }

    @Test
    fun testTimeAndMillisMapper() {
        val date = LocalTime.of(11, 22, 33,  444 * 1000000)
        assertEquals(date, TimeAndMillisMapper.toValue("112233444", metaInfo))
        assertEquals("112233444", TimeAndMillisMapper.toText(date))
    }

    @Test
    fun testIsoTimeMapper() {
        val date = LocalTime.of(11, 22, 33)
        assertEquals(date, IsoTimeMapper.toValue("11:22:33", metaInfo))
        assertEquals("11:22:33", IsoTimeMapper.toText(date))
    }

    @Test
    fun testUtcTimeMapper() {
        val date = LocalTime.of(11, 22, 33)
        assertEquals(date, UtcTimeMapper.toValue("11:22:33+03:00", metaInfo))
        //assertEquals("11:22:33+03:00", UtcTimeMapper.toText(date))
    }

    // TIMESTAMP

    @Test
    fun testTimestampMapper() {
        val date = LocalDateTime.of(2018, 12, 26, 11, 22, 33)
        assertEquals(date, TimestampMapper.toValue("20181226112233", metaInfo))
        assertEquals("20181226112233", TimestampMapper.toText(date))
    }

    @Test
    fun testTimestampAndMillisMapper() {
        val date = LocalDateTime.of(2018, 12, 26, 11, 22, 33,  444 * 1000000)
        assertEquals(date, TimestampAndMillisMapper.toValue("20181226112233444", metaInfo))
        assertEquals("20181226112233444", TimestampAndMillisMapper.toText(date))
    }

    @Test
    fun testIsoTimestampMapper() {
        val date = LocalDateTime.of(2018, 12, 26, 11, 22, 33)
        assertEquals(date, IsoTimestampMapper.toValue("2018-12-26T11:22:33", metaInfo))
        assertEquals("2018-12-26T11:22:33", IsoTimestampMapper.toText(date))
    }

    @Test
    fun testUtcTimestampMapper() {
        val date = LocalDateTime.of(2018, 12, 26, 11, 22, 33)
        assertEquals(date, UtcTimestampMapper.toValue("2018-12-26T11:22:33+03:00", metaInfo))
        //assertEquals("2018-12-26T11:22:33+03:00", UtcTimestampMapper.toText(date))
    }


    // OLD DATE

    @Test
    fun testOldDateMapper() {
        val calendar = GregorianCalendar(2018, Calendar.DECEMBER, 26)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldDateMapper.toValue("20181226", metaInfo))
        assertEquals("20181226", OldDateMapper.toText(date))
    }

    @Test
    fun testOldIsoDateMapper() {
        val calendar = GregorianCalendar(2018, Calendar.DECEMBER, 26)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldIsoDateMapper.toValue("2018-12-26", metaInfo))
        assertEquals("2018-12-26", OldIsoDateMapper.toText(date))
    }

    @Test
    fun testOldUtcDateMapper() {
        val calendar = GregorianCalendar(2018, Calendar.DECEMBER, 26)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldUtcDateMapper.toValue("2018-12-26-02:00", metaInfo))
        assertEquals("2018-12-26-02:00", OldUtcDateMapper.toText(date))
    }

    @Test
    fun testGetValueMapper(){
        // TEXT
        assertEquals(OneCharMapper, getValueMapper(Subtype.CHAR, Char::class.java))
        assertEquals(CharMapper, getValueMapper(Subtype.CHAR, String::class.java))
        assertEquals(StringMapper, getValueMapper(Subtype.STRING, Any::class.java))
        // NUMBER
        assertEquals(DoubleMapper, getValueMapper(Subtype.NUMBER, Double::class.java))
        assertEquals(DoubleMapper, getValueMapper(Subtype.NUMBER, Double::class.javaPrimitiveType!!))
        assertEquals(BigDecimalMapper, getValueMapper(Subtype.NUMBER, BigDecimal::class.java))
        assertEquals(FloatMapper, getValueMapper(Subtype.NUMBER, Float::class.java))
        assertEquals(FloatMapper, getValueMapper(Subtype.NUMBER, Float::class.javaPrimitiveType!!))
        // INTEGER
        assertEquals(IntegerMapper, getValueMapper(Subtype.INTEGER, Int::class.java))
        assertEquals(IntegerMapper, getValueMapper(Subtype.INTEGER, Int::class.javaPrimitiveType!!))
        // LONG
        assertEquals(LongMapper, getValueMapper(Subtype.LONG, Long::class.java))
        assertEquals(LongMapper, getValueMapper(Subtype.LONG, Long::class.javaPrimitiveType!!))
        assertEquals(BigIntegerMapper, getValueMapper(Subtype.LONG, BigInteger::class.java))
        // BOOELAN
        assertEquals(BooleanMapper, getValueMapper(Subtype.BOOLEAN, Any::class.java))
        assertEquals(BozMapper, getValueMapper(Subtype.BOZ, Any::class.java))
        assertEquals(BtfMapper, getValueMapper(Subtype.BTF, Any::class.java))
        assertEquals(BynMapper, getValueMapper(Subtype.BYN, Any::class.java))
        assertEquals(BsnMapper, getValueMapper(Subtype.BSN, Any::class.java))
        // DATE
        assertEquals(DateMapper, getValueMapper(Subtype.DATE, LocalDate::class.java))
        assertEquals(OldDateMapper, getValueMapper(Subtype.DATE, Date::class.java))
        assertEquals(IsoDateMapper, getValueMapper(Subtype.ISO_DATE, LocalDate::class.java))
        assertEquals(OldIsoDateMapper, getValueMapper(Subtype.ISO_DATE, Date::class.java))
        assertEquals(UtcDateMapper, getValueMapper(Subtype.UTC_DATE, LocalDate::class.java))
        assertEquals(OldUtcDateMapper, getValueMapper(Subtype.UTC_DATE, Date::class.java))
        // TIME
        assertEquals(TimeMapper, getValueMapper(Subtype.TIME, LocalTime::class.java))
        assertEquals(OldTimeMapper, getValueMapper(Subtype.TIME, Date::class.java))

    }
}