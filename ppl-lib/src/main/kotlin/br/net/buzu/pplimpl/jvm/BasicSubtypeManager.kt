/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplspec.context.SubtypeManager
import br.net.buzu.pplspec.model.Subtype

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.util.Date
import java.util.HashMap

internal val SUBTYPE_NOT_FOUND = "Subtype not found:"

private val DEFAULT_COMPLEX = Subtype.DEFAULT_COMPLEX
private val DEFAULT_SINGLE = Subtype.DEFAULT_SINGLE

private val INTERNAL_MAP :Map<Class<*>, Subtype> = mapOf(
        String::class.java to Subtype.STRING,
        Int::class.java to Subtype.INTEGER,
        BigInteger::class.java to Subtype.INTEGER,
        Int::class.javaPrimitiveType!! to Subtype.INTEGER,
        Boolean::class.java to Subtype.BOOLEAN,
        Boolean::class.javaPrimitiveType!! to Subtype.BOOLEAN,
        Date::class.java to Subtype.TIMESTAMP,
        LocalDateTime::class.java to Subtype.TIMESTAMP,
        LocalDate::class.java to Subtype.DATE,
        LocalTime::class.java to Subtype.TIME,
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

/**
 * Creates a instance domainOf `SubType` based on a Class.
 *
 * @param typeClass
 * The class
 * @return The correct instance relative to the class.
 */
fun fromType(typeClass: Class<*>): Subtype {
    if (typeClass.isEnum) {
        return Subtype.STRING
    }
    val simpleType = INTERNAL_MAP[typeClass]
    return simpleType ?: Subtype.OBJ
}

fun isSimple(type: Class<*>): Boolean {
    return INTERNAL_MAP.containsKey(type) || type.isEnum
}

