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

import br.net.buzu.exception.PplParseException
import br.net.buzu.model.Subtype
import br.net.buzu.model.TypeAdapter
import br.net.buzu.model.ValueMapper
import java.lang.reflect.Field
import java.math.BigDecimal
import java.math.BigInteger
import java.time.*
import java.util.*

/**
 * Implementation of TypeAdapter for JVM (Java, Kotlin etc). This class knows about the JVM types.
 *
 * @author Douglas Siviotti
 * @since 1.0
 * @see TypeAdapter
 */
class JvmTypeAdapter(val fieldType: Class<*>, val elementType: Class<*>, private val field: Field) : TypeAdapter {

    override val isComplex: Boolean = !isSimpleType(elementType)
    override val isEnum: Boolean = elementType.isEnum
    override val defaultSubtype: Subtype = defaultSubTypeOf(elementType)
    private val isArray: Boolean = fieldType.isArray
    private val isCollection: Boolean = Collection::class.java.isAssignableFrom(fieldType)

    override fun getFieldValue(parentObject: Any): Any? = getValue(field, parentObject)

    override fun setFieldValue(parentObject: Any, paramValue: Any?) = setValue(field, parentObject, paramValue)

    override fun maxArrayToValue(array: Array<Any?>): Any? {
        return when {
            isArray -> array
            isCollection -> if (Set::class.java.isAssignableFrom(elementType)) array.toSet() else array.toList()
            else -> array[0]
        }
    }

    override fun valueToMaxArray(value: Any?, size: Int): Array<Any?> {
        return when {
            value is Collection<*> -> value.toTypedArray()
            isArray -> value as Array<Any?>
            else -> arrayOf(value)
        }
    }

    override fun createAndFillArray(size: Int): Array<Any?> {
        return if (isComplex) Array(size) { newInstance(elementType) } else Array(size) {}
    }

    override fun valueToArray(value: Any?): Array<Any?> {
        return when {
            value == null -> arrayOf()
            isCollection -> if ((value as Collection<*>).isEmpty()) {
                arrayOf()
            } else {
                value.toTypedArray()
            }
            isArray -> if ((value as Array<Any>).size == 0) {
                arrayOf()
            } else {
                value as Array<Any?>
            }
            else -> arrayOf(value)
        }
    }

    override fun toString(): String = if (fieldType != elementType) "${fieldType.simpleName}<${elementType.simpleName}>"
    else "${elementType.simpleName}"

    override fun enumConstantToValue(constName: String): Any {
        val fields = getAllFields(elementType)
        val constName = constName.trim { it <= ' ' }
        for (field in fields) {
            if (field.isEnumConstant() && field.getName() == constName) {
                return field.get(null)
            }
        }
        throw PplParseException("The text '" + constName + "' is missing at enum " + elementType)
    }

    override fun getValueMapper(subtype: Subtype): ValueMapper = getValueMapper(subtype, elementType)

    companion object {
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

        fun defaultSubTypeOf(typeClass: Class<*>): Subtype = if (typeClass.isEnum) Subtype.STRING
        else DEFAULT_SUBTYPE_MAPPING[typeClass] ?: Subtype.DEFAULT_COMPLEX

        fun isSimpleType(type: Class<*>): Boolean = DEFAULT_SUBTYPE_MAPPING.containsKey(type) || type.isEnum
    }

}