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

import br.net.buzu.pplspec.exception.PplParseException
import br.net.buzu.pplspec.ext.ValueMapperKit
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Subtype
import br.net.buzu.pplspec.model.TypeAdapter
import br.net.buzu.pplspec.model.ValueMapper
import java.lang.reflect.Field

/**
 * Implementation of TypeAdapter for JVM
 *
 * @author Douglas Siviotti
 * @since 1.0
 * @see TypeAdapter
 */
class JvmTypeAdapter(val fieldType: Class<*>, val elementType: Class<*>, private val field: Field,
                     override val subtype: Subtype) : TypeAdapter {

    override val isComplex: Boolean = subtype.dataType.isComplex
    override val isEnum: Boolean = elementType.isEnum
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

    override fun getValueMapper(metaInfo: MetaInfo, kit: ValueMapperKit): ValueMapper = kit.getMapper(metaInfo, elementType)

}