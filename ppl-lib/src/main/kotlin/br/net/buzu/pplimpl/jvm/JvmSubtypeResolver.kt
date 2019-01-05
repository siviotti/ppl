package br.net.buzu.pplimpl.jvm

import br.net.buzu.ext.SubtypeResolver
import br.net.buzu.model.Subtype
import java.math.BigDecimal
import java.math.BigInteger
import java.time.*
import java.util.*

object JvmSubtypeResolver : SubtypeResolver {

    override fun resolve(type: Class<*>): Subtype = if (type.isEnum) Subtype.STRING
    else DEFAULT_SUBTYPE_MAPPING[type] ?: Subtype.DEFAULT_COMPLEX

    private val DEFAULT_SUBTYPE_MAPPING: Map<Class<*>, Subtype> = mapOf(
            String::class.java to Subtype.STRING,
            Int::class.java to Subtype.INTEGER,
            Int::class.javaPrimitiveType!! to Subtype.INTEGER,
            Integer::class.java to Subtype.INTEGER,
            Boolean::class.java to Subtype.BOOLEAN,
            Boolean::class.javaPrimitiveType!! to Subtype.BOOLEAN,
            Date::class.java to Subtype.TIMESTAMP,
            LocalDateTime::class.java to Subtype.TIMESTAMP,
            OffsetDateTime::class.java to Subtype.UTC_TIMESTAMP,
            LocalDate::class.java to Subtype.DATE,
            LocalTime::class.java to Subtype.TIME,
            OffsetTime::class.java to Subtype.UTC_TIME,
            Period::class.java to Subtype.PERIOD,
            BigDecimal::class.java to Subtype.NUMBER,
            Double::class.java to Subtype.NUMBER,
            Double::class.javaPrimitiveType!! to Subtype.NUMBER,
            Float::class.java to Subtype.NUMBER,
            Float::class.javaPrimitiveType!! to Subtype.NUMBER,
            Long::class.java to Subtype.LONG,
            BigInteger::class.java to Subtype.LONG,
            Long::class.javaPrimitiveType!! to Subtype.LONG,
            Byte::class.java to Subtype.INTEGER,
            Byte::class.javaPrimitiveType!! to Subtype.INTEGER,
            Short::class.java to Subtype.INTEGER,
            Short::class.javaPrimitiveType!! to Subtype.INTEGER,
            Char::class.java to Subtype.CHAR,
            Char::class.javaPrimitiveType!! to Subtype.CHAR
    )
}