/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.Subtype
import br.net.buzu.model.ValueMapper
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.naming.OperationNotSupportedException

typealias ValueParser = (text: String, metaInfo: MetaInfo) -> Any
typealias ValueSerializer = (value: Any) -> String

private val INTERNAL_ADAPTER_MAP: Map<Class<*>, ValueMapper> = mapOf(
        String::class.java to StringTrimMapper,
        LocalDate::class.java to DateMapper,
        Double::class.java to DoubleMapper,
        Boolean::class.java to BooleanMapper
)

fun getValueMapper(spec: Pair<Class<*>, Subtype>): ValueMapper {


    return if (spec.second == Subtype.CHAR) CharMapper
    else
        INTERNAL_ADAPTER_MAP[spec.first] ?: ComplexMapper
}

abstract class JvmValueMapper(val jvmType: Class<*>, subType: Subtype) : ValueMapper {
    override val subtype: Subtype
        get() = subtype

    override fun getValueSize(value: Any?): Int = if (value == null) 0 else toText(value).length
}

object ComplexMapper : JvmValueMapper(Any::class.java, Subtype.OBJ) {
    override fun toValue(positionalText: String, metaInfo: MetaInfo): Any? = throw OperationNotSupportedException()
    override fun toText(value: Any): String = throw OperationNotSupportedException()
}

// TEXT

object CharMapper : JvmValueMapper(Char::class.java, Subtype.CHAR) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text[0]
    override fun toText(value: Any): String = (value as Char).toString()
}

object StringFullMapper : JvmValueMapper(String::class.java, Subtype.CHAR) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text
    override fun toText(value: Any): String = value.toString()
}

object StringTrimMapper : JvmValueMapper(String::class.java, Subtype.STRING) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.trim { fillChar -> fillChar == metaInfo.fillChar }
    override fun toText(value: Any): String = value.toString()
}

// DECIMAL

object DoubleMapper : JvmValueMapper(Double::class.java, Subtype.NUMBER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toDouble()
    override fun toText(value: Any): String = value.toString()
}

object FloatMapper : JvmValueMapper(Float::class.java, Subtype.NUMBER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toFloat()
    override fun toText(value: Any): String = value.toString()
}

object BigDecimalMapper : JvmValueMapper(BigDecimal::class.java, Subtype.NUMBER) {
    private val round = BigDecimal.ROUND_DOWN
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = BigDecimal(text).setScale(metaInfo.scale, round)
    override fun toText(value: Any): String = value.toString()
}

// INTEGER

object IntegerMapper : JvmValueMapper(Int::class.java, Subtype.INTEGER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toInt()
    override fun toText(value: Any): String = value.toString()
}

// LONG

object LongMapper : JvmValueMapper(Long::class.java, Subtype.LONG) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toLong()
    override fun toText(value: Any): String = value.toString()
}

// BOOLEAN

object BooleanMapper : JvmValueMapper(Boolean::class.java, Subtype.BOOLEAN) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toBoolean()
    override fun toText(value: Any): String = value.toString()
}

open class BooleanCharMapper (subType: Subtype, val trueChar: Char, val falseChar: Char) : JvmValueMapper(Boolean::class.java, subType) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text[0]==trueChar
    override fun toText(value: Any): String = if (value as Boolean) trueChar.toString() else falseChar.toString()
}

object BozMapper : BooleanCharMapper(Subtype.BOZ, '1', '0')
object BtfMapper : BooleanCharMapper(Subtype.BTF, 'T', 'F')
object BynMapper : BooleanCharMapper(Subtype.BYN, 'Y', 'N')
object BsnMapper : BooleanCharMapper(Subtype.BSN, 'S', 'N')

// DATE

open class LocalDateMapper(subType: Subtype, private val formatter: DateTimeFormatter): JvmValueMapper(LocalDate::class.java, subType){
    override fun toValue(text: String, metaInfo: MetaInfo): Any?= LocalDate.parse(text, formatter)
    override fun toText(value: Any): String = (value as LocalDate).format(formatter)
}

object DateMapper : LocalDateMapper(Subtype.DATE, DateTimeFormatter.BASIC_ISO_DATE)
object IsoDateMapper : LocalDateMapper(Subtype.ISO_DATE, DateTimeFormatter.ISO_DATE)
object UtcDateMapper : LocalDateMapper(Subtype.UTC_DATE, DateTimeFormatter.ISO_OFFSET_DATE)

// TIME

open class LocalTimeMapper(subType: Subtype, private val formatter: DateTimeFormatter): JvmValueMapper(LocalTime::class.java, subType){
    override fun toValue(text: String, metaInfo: MetaInfo): Any?= LocalTime.parse(text, formatter)
    override fun toText(value: Any): String = (value as LocalTime).format(formatter)
}

object TimeMapper : LocalTimeMapper(Subtype.TIME, DateTimeFormatter.ofPattern("HHmmss"))
object TimeAndMillisMapper : LocalTimeMapper(Subtype.TIME_AND_MILLIS, DateTimeFormatter.ofPattern("HHmmssSSS"))
object IsoTimeMapper : LocalTimeMapper(Subtype.ISO_TIME, DateTimeFormatter.ISO_LOCAL_TIME)
object UtcTimeMapper : LocalTimeMapper(Subtype.UTC_TIME, DateTimeFormatter.ISO_OFFSET_TIME)

// TIMESTAMP

open class LocalTimestampMapper(subType: Subtype, private val formatter: DateTimeFormatter): JvmValueMapper(LocalDateTime::class.java, subType){
    override fun toValue(text: String, metaInfo: MetaInfo): Any?= LocalDateTime.parse(text, formatter)
    override fun toText(value: Any): String = (value as LocalDateTime).format(formatter)
}

object TimestampMapper : LocalTimestampMapper(Subtype.TIMESTAMP, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
object TimestampAndMillisMapper : LocalTimestampMapper(Subtype.TIMESTAMP_AND_MILLIS, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
object IsoTimestampMapper : LocalTimestampMapper(Subtype.ISO_TIMESTAMP, DateTimeFormatter.ISO_DATE_TIME)
object UtcTimestampMapper : LocalTimestampMapper(Subtype.UTC_TIMESTAMP, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

// OLD DATE

open class OldJavaDateMapper(subType: Subtype, private val format: SimpleDateFormat): JvmValueMapper(Date::class.java, subType){
    override fun toValue(text: String, metaInfo: MetaInfo): Any?= format.parse(text)
    override fun toText(value: Any): String = format.format(value as Date)
}

object OldDateMapper: OldJavaDateMapper(Subtype.DATE, SimpleDateFormat("yyyyMMdd"))
object OldIsoDateMapper: OldJavaDateMapper(Subtype.ISO_DATE, SimpleDateFormat("yyyy-MM-dd"))
object OldUtcDateMapper: OldJavaDateMapper(Subtype.UTC_DATE, SimpleDateFormat("yyyy-MM-ddXXX"))

object OldTimeMapper: OldJavaDateMapper(Subtype.TIME, SimpleDateFormat("HHmmss"))
object OldTimeAndMillisMapper: OldJavaDateMapper(Subtype.TIME_AND_MILLIS, SimpleDateFormat("HHmmssSSS"))
object OldIsoTimeMapper: OldJavaDateMapper(Subtype.ISO_TIME, SimpleDateFormat("HH-mm-ss"))
object OldUtcTimeMapper: OldJavaDateMapper(Subtype.UTC_TIME, SimpleDateFormat("HH-mm-ss+XXX"))

object OldTimestampMapper: OldJavaDateMapper(Subtype.TIMESTAMP, SimpleDateFormat("yyyyMMddHHmmss"))
object OldTimestampAndMillisMapper: OldJavaDateMapper(Subtype.TIMESTAMP_AND_MILLIS, SimpleDateFormat("yyyyMMddHHmmssSSS"))
object OldIsoTimestampMapper: OldJavaDateMapper(Subtype.ISO_TIMESTAMP, SimpleDateFormat("yyyy-MM-ddTHH-mm-ss"))
object OldUtcTimestampMapper: OldJavaDateMapper(Subtype.UTC_TIMESTAMP, SimpleDateFormat("yyyy-MM-ddTHH-mm-ss+XXX"))
