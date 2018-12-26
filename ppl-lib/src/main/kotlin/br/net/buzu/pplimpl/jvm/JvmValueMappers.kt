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
import br.net.buzu.model.Subtype
import br.net.buzu.model.ValueMapper
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.naming.OperationNotSupportedException

typealias ValueParser = (text: String, metaInfo: MetaInfo) -> Any
typealias ValueSerializer = (value: Any) -> String

private val INTERNAL_ADAPTER_MAP: Map<Class<*>, ValueMapper> = mapOf(
        String::class.java to StringTrimMapper,
        LocalDate::class.java to DateMapper,
        Double::class.java to DoubleMapper,
        Boolean::class.java to BooleanMapper
)

fun getValueMapper(elementType: Class<*>, subType: Subtype): ValueMapper {


    return if (subType == Subtype.CHAR) CharMapper
    else
        INTERNAL_ADAPTER_MAP[elementType] ?: ComplexMapper
}

abstract class JvmValueMapper(val type: Class<*>, subType: Subtype) : ValueMapper {
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
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = BigDecimal(text).setScale(metaInfo.scale)
    override fun toText(value: Any): String = value.toString()
}

// INTEGER: 1 Class (Int) -> 1 Subtype: INTEGER

object IntegerMapper : JvmValueMapper(Int::class.java, Subtype.INTEGER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toInt()
    override fun toText(value: Any): String = value.toString()
}

// LONG: 1 Class (Long) -> 1 Subtype: LONG

object LongMapper : JvmValueMapper(Long::class.java, Subtype.LONG) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toLong()
    override fun toText(value: Any): String = value.toString()
}

// DATE

object DateMapper : JvmValueMapper(LocalDate::class.java, Subtype.DATE) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalDate.parse(text, DateTimeFormatter.BASIC_ISO_DATE)
    override fun toText(value: Any): String = (value as LocalDate).format(DateTimeFormatter.BASIC_ISO_DATE)
}

object IsoDateMapper : JvmValueMapper(LocalDate::class.java, Subtype.ISO_DATE) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
    override fun toText(value: Any): String = (value as LocalDate).format(DateTimeFormatter.ISO_DATE)
}

object UtcDateMapper : JvmValueMapper(LocalDate::class.java, Subtype.UTC_DATE) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalDate.parse(text, DateTimeFormatter.ISO_OFFSET_DATE)
    override fun toText(value: Any): String = (value as LocalDate).format(DateTimeFormatter.ISO_OFFSET_DATE)
}

// TIME

object TimeMapper : JvmValueMapper(LocalTime::class.java, Subtype.TIME) {
    private val formatter = DateTimeFormatter.ofPattern("HHmmss")
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalTime.parse(text, formatter)
    override fun toText(value: Any): String = (value as LocalTime).format(DateTimeFormatter.BASIC_ISO_DATE)
}

object TimeAndMillisMapper : JvmValueMapper(LocalTime::class.java, Subtype.TIME_AND_MILLIS) {
    private val formatter = DateTimeFormatter.ofPattern("HHmmssSSS")
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalTime.parse(text, formatter)
    override fun toText(value: Any): String = (value as LocalTime).format(DateTimeFormatter.BASIC_ISO_DATE)
}

object IsoTimeMapper : JvmValueMapper(LocalTime::class.java, Subtype.ISO_TIME) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME)
    override fun toText(value: Any): String = (value as LocalTime).format(DateTimeFormatter.ISO_LOCAL_TIME)
}

object UtcTimeMapper : JvmValueMapper(LocalTime::class.java, Subtype.UTC_TIME) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalTime.parse(text, DateTimeFormatter.ISO_OFFSET_TIME)
    override fun toText(value: Any): String = (value as LocalTime).format(DateTimeFormatter.ISO_OFFSET_TIME)
}



object BooleanMapper : JvmValueMapper(Boolean::class.java, Subtype.BOOLEAN) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toBoolean()
    override fun toText(value: Any): String = value.toString()
}