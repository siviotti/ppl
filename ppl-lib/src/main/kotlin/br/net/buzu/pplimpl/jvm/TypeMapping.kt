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

import br.net.buzu.java.model.Subtype
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.util.*

private val INTERNAL_SIMPLE_MAPPING: Map<Class<*>, Subtype> = mapOf(
        String::class.java to Subtype.STRING,
        Int::class.java to Subtype.INTEGER,
        Int::class.javaPrimitiveType!! to Subtype.INTEGER,
        Integer::class.java to Subtype.INTEGER,
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

val genericSubTypeOf: (Class<*>) -> Subtype = { type -> subTypeOf(type) }
/**
 * Creates a instance of `SubType` based on a Class.
 *
 * @param typeClass
 * The class
 * @return The correct instance relative to the class.
 */
fun subTypeOf(typeClass: Class<*>): Subtype = if (typeClass.isEnum) Subtype.STRING
else INTERNAL_SIMPLE_MAPPING[typeClass] ?: Subtype.DEFAULT_COMPLEX

fun isSimpleType(type: Class<*>): Boolean = INTERNAL_SIMPLE_MAPPING.containsKey(type) || type.isEnum