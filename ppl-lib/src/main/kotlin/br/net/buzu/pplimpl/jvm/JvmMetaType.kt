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

import br.net.buzu.model.*
import br.net.buzu.lib.fill
import br.net.buzu.lib.fit
import java.lang.reflect.Field
import java.math.BigDecimal


abstract class JvmMetaType(fullName: String, metaName: String, val fieldType: Class<*>, val elementType: Class<*>,
                           metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, val field: Field, val valueMapper: ValueMapper) :
        MetaType(fullName, metaName, metaInfo, treeIndex, children) {

    override val hasChildren: Boolean = children.isNotEmpty()
    private val isArray: Boolean = fieldType.isArray
    private val isCollection: Boolean = Collection::class.java.isAssignableFrom(fieldType)
    private val isComplex = metaInfo.subtype.dataType.isComplex
    private val childrenMap = children.map { it.metaName to it }.toMap()

    internal fun getChildByMetaName(name: String): MetaType = childrenMap[name]
            ?: throw IllegalArgumentException("MetaType child '$name' not found at ${toString()}. Children:$children")


    override fun nodeCount(): Int {
        return if (children.isEmpty()) 1 else {
            var count = 1
            for (it in children) count += it.nodeCount()
            count
        }
    }

    override fun getFieldValue(parentObject: Any): Any? = getValue(field, parentObject)

    override fun setFieldValue(parentObject: Any, paramValue: Any?) = setValue(field, parentObject, paramValue)

    override fun getValueSize(value: Any?): Int {
        if (value == null) {
            return 0
        }
        val str = if (PplSerializable::class.java.isAssignableFrom(value.javaClass)) {
            (value as PplSerializable).asPplSerial()
        } else {
            if (value is BigDecimal) {
                value.toPlainString()
            } else {
                value.toString()
            }
        }
        return str?.length ?: 0
    }

    internal fun maxArrayToValue(array: Array<Any?>): Any {
        return when {
            isArray -> array
            isCollection -> if (Set::class.java.isAssignableFrom(elementType)) array.toSet() else array.toList()
            else -> array[0]!!
        }
    }

    internal fun valueToMaxArray(value: Any?, size: Int): Array<Any?> {
        return when {
            value is Collection<*> -> value.toTypedArray()
            isArray -> value as Array<Any?>
            else -> arrayOf(value)
        }
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

    internal fun createAndFillArray(size: Int): Array<Any?> {
        return if (isComplex) Array(size) { newInstance(elementType) } else Array(size) {}
    }

    override fun toString(): String = "[$treeIndex] $fieldFullName: ${fieldType.simpleName}<${elementType.simpleName}> ($metaName) $metaInfo"

    internal fun parseAtomic(text: String, metadata: StaticMetadata): Any? {
        val metaInfo: MetaInfo = metadata.info()
        return if (isNull(text, metaInfo.nullChar))
            if (metaInfo.hasDefaultValue()) valueMapper.toValue(metadata.info().defaultValue, metaInfo) else null
        else
            valueMapper.toValue(text.substring(0, metaInfo.size), metaInfo)
    }

    internal fun serializeAtomic(value: Any?, metadata: StaticMetadata): String {
        return if (value == null) serializeNull(metadata.info()) else serializeValue(value, metadata.info())
    }

    internal fun serializeValue(value: Any, metaInfo: MetaInfo): String {
        return fit(metaInfo.align, valueMapper.toText(value), metaInfo.size, metaInfo.fillChar)
    }

    internal fun serializeNull(metaInfo: MetaInfo): String {
        return fill(metaInfo.align, metaInfo.defaultValue, metaInfo.size, metaInfo.nullChar)
    }

    internal fun isNull(text: String, nullChar: Char): Boolean {
        for (i in 0 until text.length) if (text[i] != nullChar) return false
        return true
    }

}