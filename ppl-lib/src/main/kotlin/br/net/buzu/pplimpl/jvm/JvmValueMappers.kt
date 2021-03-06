/*
 *	This file is part of DefaultPplMapper.
 *
 *   DefaultPplMapper is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DefaultPplMapper is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with DefaultPplMapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplspec.exception.PplParseException
import br.net.buzu.pplspec.ext.ValueMapperKit
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Subtype
import br.net.buzu.pplspec.model.ValueMapper
import java.math.BigDecimal
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*
import javax.naming.OperationNotSupportedException

const val OLD_TIME_OFFSET = 11

object JvmValueMapperKit : ValueMapperKit {
    override fun getMapper(metaInfo: MetaInfo, type: Class<*>): ValueMapper {
        return getValueMapper(metaInfo.subtype, type)
    }
}

private val MAPPERS = createArrayOfMapper()

internal fun getValueMapper(subtype: Subtype, elementType: Class<*>): ValueMapper {
    return when {
        elementType.isEnum -> EnumMapper(elementType)
        elementType == Char::class.java || elementType == Char::class.javaPrimitiveType -> OneCharMapper
        subtype == Subtype.NUMBER -> when (elementType) {
            BigDecimal::class.java -> BigDecimalMapper
            Double::class.java, Double::class.javaPrimitiveType -> DoubleMapper
            else -> FloatMapper
        }
        subtype == Subtype.LONG -> if (elementType == BigInteger::class.java) BigIntegerMapper else LongMapper
        elementType == Date::class.java -> MAPPERS[subtype.ordinal + OLD_TIME_OFFSET]
        else -> MAPPERS[subtype.ordinal]
    }
}

// Superclass of All Mappers

abstract class JvmValueMapper(val jvmType: Class<*>, val subType: Subtype) : ValueMapper {
    override fun getValueSize(value: Any?, metaInfo: MetaInfo): Int = if (value == null) 0 else toText(value, metaInfo).length
    override fun toString(): String = "${javaClass.simpleName}(subType:$subType jvm-type:${jvmType.simpleName})"
}

abstract class FixedJvmValueMapper(jvmType: Class<*>, subType: Subtype) : JvmValueMapper(jvmType,  subType) {
    override fun getValueSize(value: Any?, metaInfo: MetaInfo): Int = metaInfo.subtype.fixedSize
}

// Error Mapper - Complex type has no Mapper

object ErrorMapper : JvmValueMapper(Any::class.java, Subtype.OBJ) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = throw OperationNotSupportedException()
    override fun toText(value: Any, metaInfo: MetaInfo): String = throw OperationNotSupportedException()
}

// ENUM
class EnumMapper(jvmType: Class<*>) : JvmValueMapper(jvmType, Subtype.STRING) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? {
        val fields = getAllFields(jvmType)
        val constName = text.trim { it <= ' ' }
        for (field in fields) {
            if (field.isEnumConstant && field.name == constName) {
                return field.get(null)
            }
        }
        throw PplParseException("The constant name '$constName' is missing at enum $jvmType")

    }
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as Enum<*>).name
}

// TEXT

object OneCharMapper : JvmValueMapper(Char::class.java, Subtype.CHAR) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text[0]
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as Char).toString()
}

object CharMapper : JvmValueMapper(String::class.java, Subtype.CHAR) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text
    override fun toText(value: Any, metaInfo: MetaInfo): String = value.toString()
}

object StringMapper : JvmValueMapper(String::class.java, Subtype.STRING) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.trim { it == metaInfo.fillChar }
    override fun toText(value: Any, metaInfo: MetaInfo): String = value.toString()
}

// DECIMAL

object DoubleMapper : JvmValueMapper(Double::class.java, Subtype.NUMBER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toDouble()
    override fun toText(value: Any, metaInfo: MetaInfo): String = value.toString()
}

object FloatMapper : JvmValueMapper(Float::class.java, Subtype.NUMBER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toFloat()
    override fun toText(value: Any, metaInfo: MetaInfo): String = value.toString()
}

object BigDecimalMapper : JvmValueMapper(BigDecimal::class.java, Subtype.NUMBER) {
    private const val round = BigDecimal.ROUND_DOWN
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = BigDecimal(text).setScale(metaInfo.scale, round)
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as BigDecimal).toPlainString()
}

// INTEGER

object IntegerMapper : JvmValueMapper(Int::class.java, Subtype.INTEGER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toInt()
    override fun toText(value: Any, metaInfo: MetaInfo): String = value.toString()
}

// LONG

object LongMapper : JvmValueMapper(Long::class.java, Subtype.LONG) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toLong()
    override fun toText(value: Any, metaInfo: MetaInfo): String = value.toString()
}

object BigIntegerMapper : JvmValueMapper(Long::class.java, Subtype.LONG) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toBigInteger()
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as BigInteger).toString()
}

// BOOLEAN

object BooleanMapper : FixedJvmValueMapper(Boolean::class.java, Subtype.BOOLEAN) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toBoolean()
    override fun toText(value: Any, metaInfo: MetaInfo): String = value.toString()
}

open class BooleanCharMapper(subType: Subtype, private val trueChar: Char, private val falseChar: Char)
    : FixedJvmValueMapper(Boolean::class.java, subType) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text[0] == trueChar
    override fun toText(value: Any, metaInfo: MetaInfo): String = if (value as Boolean) trueChar.toString() else falseChar.toString()
}

object BozMapper : BooleanCharMapper(Subtype.BOZ, '1', '0')
object BtfMapper : BooleanCharMapper(Subtype.BTF, 'T', 'F')
object BynMapper : BooleanCharMapper(Subtype.BYN, 'Y', 'N')
object BsnMapper : BooleanCharMapper(Subtype.BSN, 'S', 'N')

// DATE

open class LocalDateMapper(subType: Subtype, private val formatter: DateTimeFormatter, jvmDateType: Class<*> = LocalDate::class.java)
    : FixedJvmValueMapper(jvmDateType, subType) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalDate.parse(text, formatter)
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as LocalDate).format(formatter)
}

object DateMapper : LocalDateMapper(Subtype.DATE, DateTimeFormatter.BASIC_ISO_DATE)
object IsoDateMapper : LocalDateMapper(Subtype.ISO_DATE, DateTimeFormatter.ISO_DATE)
object UtcDateMapper : LocalDateMapper(Subtype.UTC_DATE, DateTimeFormatter.ISO_OFFSET_DATE)

// TIME

open class LocalTimeMapper(subType: Subtype, private val formatter: DateTimeFormatter)
    : FixedJvmValueMapper(LocalTime::class.java, subType) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalTime.parse(text, formatter)
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as LocalTime).format(formatter)
}

object TimeMapper : LocalTimeMapper(Subtype.TIME, DateTimeFormatter.ofPattern("HHmmss"))
object TimeAndMillisMapper : LocalTimeMapper(Subtype.TIME_AND_MILLIS, DateTimeFormatter.ofPattern("HHmmssSSS"))
object IsoTimeMapper : LocalTimeMapper(Subtype.ISO_TIME, DateTimeFormatter.ISO_LOCAL_TIME)

object UtcTimeMapper : LocalTimeMapper(Subtype.UTC_TIME, DateTimeFormatter.ISO_OFFSET_TIME) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = OffsetTime.parse(text, DateTimeFormatter.ISO_OFFSET_TIME)
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as OffsetTime).format(DateTimeFormatter.ISO_OFFSET_TIME)
}

// TIMESTAMP

open class LocalTimestampMapper(subType: Subtype, private val formatter: DateTimeFormatter)
    : FixedJvmValueMapper(LocalDateTime::class.java, subType) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalDateTime.parse(text, formatter)
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as LocalDateTime).format(formatter)
}

object TimestampMapper : LocalTimestampMapper(Subtype.TIMESTAMP, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
// JDK-BUG: object TimestampAndMillisMapper : LocalTimestampMapper(Subtype.TIMESTAMP_AND_MILLIS, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
// https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8031085
object TimestampAndMillisMapper : LocalTimestampMapper(Subtype.TIMESTAMP_AND_MILLIS,
        DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmmss").appendValue(ChronoField.MILLI_OF_SECOND, 3).toFormatter()) // Workaround

object IsoTimestampMapper : LocalTimestampMapper(Subtype.ISO_TIMESTAMP, DateTimeFormatter.ISO_DATE_TIME)

object UtcTimestampMapper : LocalTimestampMapper(Subtype.UTC_TIMESTAMP, DateTimeFormatter.ISO_OFFSET_DATE_TIME) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    override fun toText(value: Any, metaInfo: MetaInfo): String = (value as OffsetDateTime).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}


// OLD DATE

open class OldJavaDateMapper(subType: Subtype, private val format: SimpleDateFormat)
    : FixedJvmValueMapper(Date::class.java, subType) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = format.parse(text)
    override fun toText(value: Any, metaInfo: MetaInfo): String = format.format(value as Date)
}

object OldDateMapper : OldJavaDateMapper(Subtype.DATE, SimpleDateFormat("yyyyMMdd"))
object OldIsoDateMapper : OldJavaDateMapper(Subtype.ISO_DATE, SimpleDateFormat("yyyy-MM-dd"))
object OldUtcDateMapper : OldJavaDateMapper(Subtype.UTC_DATE, SimpleDateFormat("yyyy-MM-ddXXX"))

object OldTimeMapper : OldJavaDateMapper(Subtype.TIME, SimpleDateFormat("HHmmss"))
object OldTimeAndMillisMapper : OldJavaDateMapper(Subtype.TIME_AND_MILLIS, SimpleDateFormat("HHmmssSSS"))
object OldIsoTimeMapper : OldJavaDateMapper(Subtype.ISO_TIME, SimpleDateFormat("HH:mm:ss"))
object OldUtcTimeMapper : OldJavaDateMapper(Subtype.UTC_TIME, SimpleDateFormat("HH:mm:ssXXX"))

object OldTimestampMapper : OldJavaDateMapper(Subtype.TIMESTAMP, SimpleDateFormat("yyyyMMddHHmmss"))
object OldTimestampAndMillisMapper : OldJavaDateMapper(Subtype.TIMESTAMP_AND_MILLIS, SimpleDateFormat("yyyyMMddHHmmssSSS"))
object OldIsoTimestampMapper : OldJavaDateMapper(Subtype.ISO_TIMESTAMP, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))
object OldUtcTimestampMapper : OldJavaDateMapper(Subtype.UTC_TIMESTAMP, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"))

fun createArrayOfMapper(): Array<JvmValueMapper> {
    val array = Array<JvmValueMapper>(Subtype.values().size + OLD_TIME_OFFSET) { ErrorMapper }

    // Var Size (1 ValueParser per Subtype)
    array[Subtype.CHAR.ordinal] = CharMapper
    array[Subtype.STRING.ordinal] = StringMapper
    array[Subtype.NUMBER.ordinal] = BigDecimalMapper
    array[Subtype.INTEGER.ordinal] = IntegerMapper
    array[Subtype.LONG.ordinal] = LongMapper

    // Fixed Size (Many parsers per Subtype)

    // Boolean
    array[Subtype.BOOLEAN.ordinal] = BooleanMapper
    array[Subtype.BOZ.ordinal] = BozMapper
    array[Subtype.BTF.ordinal] = BtfMapper
    array[Subtype.BYN.ordinal] = BynMapper
    array[Subtype.BSN.ordinal] = BsnMapper

    // Date
    array[Subtype.DATE.ordinal] = DateMapper
    array[Subtype.ISO_DATE.ordinal] = IsoDateMapper
    array[Subtype.UTC_DATE.ordinal] = UtcDateMapper
    // Time
    array[Subtype.TIME.ordinal] = TimeMapper
    array[Subtype.TIME_AND_MILLIS.ordinal] = TimeAndMillisMapper
    array[Subtype.ISO_TIME.ordinal] = IsoTimeMapper
    array[Subtype.UTC_TIME.ordinal] = UtcTimeMapper
    // Timestamp
    array[Subtype.TIMESTAMP.ordinal] = TimestampMapper
    array[Subtype.TIMESTAMP_AND_MILLIS.ordinal] = TimestampAndMillisMapper
    array[Subtype.ISO_TIMESTAMP.ordinal] = IsoTimestampMapper
    array[Subtype.UTC_TIMESTAMP.ordinal] = UtcTimestampMapper

    // OLD Date
    array[OLD_TIME_OFFSET + Subtype.DATE.ordinal] = OldDateMapper
    array[OLD_TIME_OFFSET + Subtype.ISO_DATE.ordinal] = OldIsoDateMapper
    array[OLD_TIME_OFFSET + Subtype.UTC_DATE.ordinal] = OldUtcDateMapper
    // OLD Time
    array[OLD_TIME_OFFSET + Subtype.TIME.ordinal] = OldTimeMapper
    array[OLD_TIME_OFFSET + Subtype.TIME_AND_MILLIS.ordinal] = OldTimeAndMillisMapper
    array[OLD_TIME_OFFSET + Subtype.ISO_TIME.ordinal] = OldIsoTimeMapper
    array[OLD_TIME_OFFSET + Subtype.UTC_TIME.ordinal] = OldUtcTimeMapper
    // OLD Timestamp
    array[OLD_TIME_OFFSET + Subtype.TIMESTAMP.ordinal] = OldTimestampMapper
    array[OLD_TIME_OFFSET + Subtype.TIMESTAMP_AND_MILLIS.ordinal] = OldTimestampAndMillisMapper
    array[OLD_TIME_OFFSET + Subtype.ISO_TIMESTAMP.ordinal] = OldIsoTimestampMapper
    array[OLD_TIME_OFFSET + Subtype.UTC_TIMESTAMP.ordinal] = OldUtcTimestampMapper

    return array
}
