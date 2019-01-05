package br.net.buzu.pplimpl.jvm

import br.net.buzu.lang.EMPTY
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Subtype
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import java.math.BigDecimal
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.time.*
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

        assertEquals("1", OneCharMapper.toText('1', metaInfo))
        assertEquals(" ", OneCharMapper.toText(' ', metaInfo))
    }

    @Test
    fun testCharMapper() {
        assertEquals("123", CharMapper.toValue("123", metaInfo))
        assertEquals(" 123", CharMapper.toValue(" 123", metaInfo))
        assertEquals("123 ", CharMapper.toValue("123 ", metaInfo))
        assertEquals(" 123 ", CharMapper.toValue(" 123 ", metaInfo))
        assertEquals(" 123 ", CharMapper.toValue(" 123 ", metaInfo))

        assertEquals("123", CharMapper.toText("123",metaInfo ))
        assertEquals(" 123", CharMapper.toText(" 123", metaInfo))
        assertEquals("123 ", CharMapper.toText("123 ",metaInfo ))
        assertEquals(" 123 ", CharMapper.toText(" 123 ",metaInfo ))
    }

    @Test
    fun testStringMapper() {
        assertEquals("123", StringMapper.toValue("123", metaInfo))
        assertEquals("123", StringMapper.toValue(" 123", metaInfo))
        assertEquals("123", StringMapper.toValue("123 ", metaInfo))
        assertEquals("123", StringMapper.toValue(" 123 ", metaInfo))
        assertEquals("123", StringMapper.toText("123", metaInfo))
        assertEquals(" 123", StringMapper.toText(" 123",metaInfo ))
        assertEquals("123 ", StringMapper.toText("123 ", metaInfo))
        assertEquals(" 123 ", StringMapper.toText(" 123 ", metaInfo))
    }

    // DECIMAL

    @Test
    fun testDoubleMapper() {
        assertEquals(23.78, DoubleMapper.toValue("23.78", metaInfo))
        assertEquals("23.78", DoubleMapper.toText(23.78, metaInfo))
    }

    @Test
    fun testFloatMapper() {
        assertEquals(23.78f, FloatMapper.toValue("23.78", metaInfo))
        assertEquals("23.78", FloatMapper.toText(23.78f, metaInfo))
    }

    @Test
    fun testBigDecimalMapper() {
        val metaInfo = MetaInfo(0, EMPTY, Subtype.STRING, 0, 2, 0, 0)
        assertEquals(BigDecimal("23.78"), BigDecimalMapper.toValue("23.78", metaInfo))
        assertEquals("23.78", BigDecimalMapper.toText(BigDecimal("23.78"), metaInfo))
    }

    // INTEGER

    @Test
    fun testIntegerMapper() {
        assertEquals(123, IntegerMapper.toValue("123", metaInfo))
        assertEquals("123", IntegerMapper.toText(123, metaInfo))
    }

    // LONG

    @Test
    fun testLongMapper() {
        assertEquals(123L, LongMapper.toValue("123", metaInfo))
        assertEquals("123", LongMapper.toText(123L, metaInfo))
    }
    @Test
    fun testBigIntegerMapper() {
        val bigInt = BigInteger.valueOf(123L)
        assertEquals(bigInt, BigIntegerMapper.toValue("123", metaInfo))
        assertEquals("123", BigIntegerMapper.toText(bigInt, metaInfo))
    }

    // BOOLEAN

    @Test
    fun testBooleanMapper() {
        assertEquals(true, BooleanMapper.toValue("true", metaInfo))
        assertEquals(true, BooleanMapper.toValue("True", metaInfo))
        assertEquals(true, BooleanMapper.toValue("TRUE", metaInfo))
        assertEquals(false, BooleanMapper.toValue("false", metaInfo))
        assertEquals(false, BooleanMapper.toValue("etc", metaInfo))
        assertEquals("true", BooleanMapper.toText(true, metaInfo))
        assertEquals("false", BooleanMapper.toText(false, metaInfo))
        assertEquals("true", BooleanMapper.toText(1==1, metaInfo))
        assertEquals("false", BooleanMapper.toText(1==2, metaInfo))
    }

    @Test
    fun testBozMapper() {
        assertEquals(true, BozMapper.toValue("1", metaInfo))
        assertEquals("1", BozMapper.toText(true, metaInfo))
        assertEquals(false, BozMapper.toValue("0", metaInfo))
        assertEquals("0", BozMapper.toText(false, metaInfo))
        assertEquals(false, BozMapper.toValue("X", metaInfo))
    }

    @Test
    fun testBtfMapper() {
        assertEquals(true, BtfMapper.toValue("T", metaInfo))
        assertEquals("T", BtfMapper.toText(true, metaInfo))
        assertEquals(false, BtfMapper.toValue("F", metaInfo))
        assertEquals("F", BtfMapper.toText(false, metaInfo))
        assertEquals(false, BtfMapper.toValue("X", metaInfo))
    }

    @Test
    fun testBynMapper() {
        assertEquals(true, BynMapper.toValue("Y", metaInfo))
        assertEquals("Y", BynMapper.toText(true, metaInfo))
        assertEquals(false, BynMapper.toValue("N", metaInfo))
        assertEquals("N", BynMapper.toText(false, metaInfo))
        assertEquals(false, BynMapper.toValue("X", metaInfo))
    }

    @Test
    fun testBsnMapper() {
        assertEquals(true, BsnMapper.toValue("S", metaInfo))
        assertEquals("S", BsnMapper.toText(true, metaInfo))
        assertEquals(false, BsnMapper.toValue("N", metaInfo))
        assertEquals("N", BsnMapper.toText(false, metaInfo))
        assertEquals(false, BsnMapper.toValue("X", metaInfo))
    }

    // DATE

    @Test
    fun testDateMapper() {
        val date = LocalDate.of(2018, 12, 26)
        assertEquals(date, DateMapper.toValue("20181226", metaInfo))
        assertEquals("20181226", DateMapper.toText(date, metaInfo))
    }

    @Test
    fun testIsoDateMapper() {
        val date = LocalDate.of(2018, 12, 26)
        assertEquals(date, IsoDateMapper.toValue("2018-12-26", metaInfo))
        assertEquals("2018-12-26", IsoDateMapper.toText(date, metaInfo))
    }

    // TIME

    @Test
    fun testTimeMapper() {
        val date = LocalTime.of(11, 22, 33)
        assertEquals(date, TimeMapper.toValue("112233", metaInfo))
        assertEquals("112233", TimeMapper.toText(date, metaInfo))
    }

    @Test
    fun testTimeAndMillisMapper() {
        val date = LocalTime.of(11, 22, 33,  444 * 1000000)
        assertEquals(date, TimeAndMillisMapper.toValue("112233444", metaInfo))
        assertEquals("112233444", TimeAndMillisMapper.toText(date, metaInfo))
    }

    @Test
    fun testIsoTimeMapper() {
        val date = LocalTime.of(11, 22, 33)
        assertEquals(date, IsoTimeMapper.toValue("11:22:33", metaInfo))
        assertEquals("11:22:33", IsoTimeMapper.toText(date, metaInfo))
    }

    @Test
    fun testUtcTimeMapper() {
        val localTime = LocalTime.of( 11, 22, 33)
        val date = OffsetTime.of(localTime, ZoneOffset.ofHours(-2))
        assertEquals(date, UtcTimeMapper.toValue("11:22:33-02:00", metaInfo))
        assertEquals("11:22:33-02:00", UtcTimeMapper.toText(date, metaInfo))
    }

    // TIMESTAMP

    @Test
    fun testTimestampMapper() {
        val date = LocalDateTime.of(2018, 12, 26, 11, 22, 33)
        assertEquals(date, TimestampMapper.toValue("20181226112233", metaInfo))
        assertEquals("20181226112233", TimestampMapper.toText(date, metaInfo))
    }

    @Test
    fun testTimestampAndMillisMapper() {
        val localDateTime = LocalDateTime.of(2018, 12, 26, 11, 22, 33,  444 * 1000000)
        assertEquals(localDateTime, TimestampAndMillisMapper.toValue("20181226112233444", metaInfo))
        assertEquals("20181226112233444", TimestampAndMillisMapper.toText(localDateTime, metaInfo))
    }

    @Test
    fun testIsoTimestampMapper() {
        val date = LocalDateTime.of(2018, 12, 26, 11, 22, 33)
        assertEquals(date, IsoTimestampMapper.toValue("2018-12-26T11:22:33", metaInfo))
        assertEquals("2018-12-26T11:22:33", IsoTimestampMapper.toText(date, metaInfo))
    }

    @Test
    fun testUtcTimestampMapper() {
        val localDateTime = LocalDateTime.of(2018, 12, 26, 11, 22, 33)
        val date = OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(-2))
        val parsed = UtcTimestampMapper.toValue("2018-12-26T11:22:33-02:00", metaInfo)as OffsetDateTime
        assertEquals(date, parsed)
        assertEquals("2018-12-26T11:22:33-02:00", UtcTimestampMapper.toText(date, metaInfo))
    }


    // OLD DATE

    @Test
    fun testOldDateMapper() {
        val calendar = GregorianCalendar(2018, Calendar.DECEMBER, 26)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldDateMapper.toValue("20181226", metaInfo))
        assertEquals("20181226", OldDateMapper.toText(date, metaInfo))
    }

    @Test
    fun testOldIsoDateMapper() {
        val calendar = GregorianCalendar(2018, Calendar.DECEMBER, 26)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldIsoDateMapper.toValue("2018-12-26", metaInfo))
        assertEquals("2018-12-26", OldIsoDateMapper.toText(date, metaInfo))
    }

    @Test
    fun testOldUtcDateMapper() {
        val offset = SimpleDateFormat("XXX").format(Date())
        val textDate = "2018-12-26$offset"
        val sdf = SimpleDateFormat("yyyy-MM-ddXXX")

        val date = sdf.parse(textDate)
        val parsed = OldUtcDateMapper.toValue(textDate, metaInfo)
        assertEquals(date, parsed)
        assertEquals(textDate, OldUtcDateMapper.toText(date, metaInfo))
    }


    // OLD TIME

    @Test
    fun testOldTimeMapper() {
        val calendar = GregorianCalendar(1970,0,1,11,22,33)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldTimeMapper.toValue("112233", metaInfo))
        assertEquals("112233", OldTimeMapper.toText(date, metaInfo))
    }

    @Test
    fun testOldTimeAndMillisMapper() {
        val cal = GregorianCalendar(1970,0,1,11,22,33)
        cal.set(Calendar.MILLISECOND, 444)
        val date = Date(cal.timeInMillis)
        assertEquals(date, OldTimeAndMillisMapper.toValue("112233444", metaInfo))
        assertEquals("112233444", OldTimeAndMillisMapper.toText(date, metaInfo))
    }

    @Test
    fun testOldIsoTimeMapper() {
        val date = Date(GregorianCalendar(1970,0,1,11,22,33).timeInMillis)
        assertEquals(date, OldIsoTimeMapper.toValue("11:22:33", metaInfo))
        assertEquals("11:22:33", OldIsoTimeMapper.toText(date, metaInfo))
    }

    @Disabled
    @Test
    fun testOldUtcTimeMapper() {
        val offset = SimpleDateFormat("XXX").format(Date())
        val textTime = "11:22:33$offset"
        val sdf = SimpleDateFormat("HH:mm:ssXXX")
        val date = sdf.parse(textTime)
        assertEquals(date, OldUtcTimeMapper.toValue(textTime, metaInfo))
        assertEquals(textTime, OldUtcTimeMapper.toText(date, metaInfo))
    }

    // OLD TIMESTAMP

    @Test
    fun testOldTimestampMapper() {
        val calendar = GregorianCalendar(2018,Calendar.DECEMBER,26,11,22,33)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldTimestampMapper.toValue("20181226112233", metaInfo))
        assertEquals("20181226112233", OldTimestampMapper.toText(date, metaInfo))
    }

    @Test
    fun testOldTimestampAndMillisMapper() {
        val calendar = GregorianCalendar(2018,Calendar.DECEMBER,26,11,22,33)
        calendar.set(Calendar.MILLISECOND, 444)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldTimestampAndMillisMapper.toValue("20181226112233444", metaInfo))
        assertEquals("20181226112233444", OldTimestampAndMillisMapper.toText(date, metaInfo))
    }

    @Test
    fun testOldIsoTimestampMapper() {
        val calendar = GregorianCalendar(2018,Calendar.DECEMBER,26,11,22,33)
        val date = Date(calendar.timeInMillis)
        assertEquals(date, OldIsoTimestampMapper.toValue("2018-12-26T11:22:33", metaInfo))
        assertEquals("2018-12-26T11:22:33", OldIsoTimestampMapper.toText(date, metaInfo))
    }

    @Test
    fun testOldUtcTimestampMapper() {
        val offset = SimpleDateFormat("XXX").format(Date())
        val textDate = "2018-12-26T11:22:33$offset"
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
        val date = sdf.parse(textDate)
        val parsed = OldUtcTimestampMapper.toValue(textDate, metaInfo)
        assertEquals(date, parsed)
        assertEquals(textDate, OldUtcTimestampMapper.toText(date, metaInfo))
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
        // TIME
        assertEquals(TimeMapper, getValueMapper(Subtype.TIME, LocalTime::class.java))
        assertEquals(OldTimeMapper, getValueMapper(Subtype.TIME, Date::class.java))
        assertEquals(TimeAndMillisMapper, getValueMapper(Subtype.TIME_AND_MILLIS, LocalTime::class.java))
        assertEquals(OldTimeAndMillisMapper, getValueMapper(Subtype.TIME_AND_MILLIS, Date::class.java))
        assertEquals(IsoTimeMapper, getValueMapper(Subtype.ISO_TIME, LocalTime::class.java))
        assertEquals(OldIsoTimeMapper, getValueMapper(Subtype.ISO_TIME, Date::class.java))
        assertEquals(UtcTimeMapper, getValueMapper(Subtype.UTC_TIME, LocalTime::class.java))
        assertEquals(OldUtcTimeMapper, getValueMapper(Subtype.UTC_TIME, Date::class.java))
        // TIMESTAMP
        assertEquals(TimestampMapper, getValueMapper(Subtype.TIMESTAMP, LocalDateTime::class.java))
        assertEquals(OldTimestampMapper, getValueMapper(Subtype.TIMESTAMP, Date::class.java))
        assertEquals(TimestampAndMillisMapper, getValueMapper(Subtype.TIMESTAMP_AND_MILLIS, LocalDateTime::class.java))
        assertEquals(OldTimestampAndMillisMapper, getValueMapper(Subtype.TIMESTAMP_AND_MILLIS, Date::class.java))
        assertEquals(IsoTimestampMapper, getValueMapper(Subtype.ISO_TIMESTAMP, LocalDateTime::class.java))
        assertEquals(OldIsoTimestampMapper, getValueMapper(Subtype.ISO_TIMESTAMP, Date::class.java))
        assertEquals(UtcTimestampMapper, getValueMapper(Subtype.UTC_TIMESTAMP, LocalDateTime::class.java))
        assertEquals(OldUtcTimestampMapper, getValueMapper(Subtype.UTC_TIMESTAMP, Date::class.java))
    }
}