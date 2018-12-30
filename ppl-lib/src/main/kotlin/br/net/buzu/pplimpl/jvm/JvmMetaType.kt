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
import br.net.buzu.exception.PplSerializeException
import br.net.buzu.model.*
import br.net.buzu.pplimpl.util.fill
import br.net.buzu.pplimpl.util.fit
import java.lang.reflect.Field


abstract class JvmMetaType(fullName: String, metaName: String, val fieldType: Class<*>, val elementType: Class<*>,
                           metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, val field: Field,
                           private val valueMapper: ValueMapper) :
        MetaType(fullName, metaName, metaInfo, treeIndex, children) {

    override val hasChildren: Boolean = children.isNotEmpty()
    private val isArray: Boolean = fieldType.isArray
    private val isCollection: Boolean = Collection::class.java.isAssignableFrom(fieldType)
    private val isComplex = metaInfo.subtype.dataType.isComplex
    private val childrenMap = children.map { it.metaName to it }.toMap()

    override fun parse(text: String, metadata: StaticMetadata): Any? {
        try {
            return doParse(text, metadata)
        } catch (e: Exception) {
            throw PplParseException(this.javaClass.simpleName, text, elementType, e)
        }
    }

    override fun serialize(value: Any?, metadata: StaticMetadata): String {
        try {
            return doSerialize(value, metadata)
        } catch (e: Exception) {
            throw PplSerializeException("Serialization error at ${toString()} \n valueMapper:${valueMapper.javaClass.simpleName}", e)
        }
    }

    abstract fun doParse(text: String, metadata: StaticMetadata): Any?

    abstract fun doSerialize(value: Any?, metadata: StaticMetadata): String


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

    override fun getValueSize(value: Any?): Int = valueMapper.getValueSize(value)

    internal fun maxArrayToValue(array: Array<Any?>): Any? {
        return when {
            isArray -> array
            isCollection -> if (Set::class.java.isAssignableFrom(elementType)) array.toSet() else array.toList()
            else -> array[0]
        }
    }

    internal fun valueToMaxArray(value: Any?, size: Int): Array<Any?> {
        return when {
            value is Collection<*> -> value.toTypedArray()
            isArray -> value as Array<Any?>
            else -> arrayOf(value)
        }
    }

    internal fun createAndFillArray(size: Int): Array<Any?> {
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

    override fun toString(): String = "[$treeIndex] $fullName: ${fieldType.simpleName}<${elementType.simpleName}> ($metaName) $metaInfo"

    internal open fun parseAtomic(text: String, metadata: StaticMetadata): Any? {
        val info: MetaInfo = metadata.info()
        return if (isNull(text, info.nullChar))
            if (info.hasDefaultValue()) valueMapper.toValue(metadata.info().defaultValue, info) else null
        else
            valueMapper.toValue(text, info)
    }

    internal open fun serializeAtomic(value: Any?, metadata: StaticMetadata): String {
        val info: MetaInfo = metadata.info()
        return if (value == null) serializeNull(info)
        else fit(info.align, valueMapper.toText(value), info.size, info.fillChar)
    }

    internal fun serializeNull(info: MetaInfo): String = fill(info.align, info.defaultValue, info.size, info.nullChar)

    private fun isNull(text: String, nullChar: Char): Boolean {
        for (i in 0 until text.length) if (text[i] != nullChar) return false
        return true
    }

}