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


abstract class JvmMetaType(fullName: String, metaName: String,  metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, val typeAdapter: TypeAdapter,
                           private val valueMapper: ValueMapper) :
        MetaType(fullName, metaName, metaInfo, treeIndex, children) {

    override val hasChildren: Boolean = children.isNotEmpty()
    private val childrenMap = children.map { it.metaName to it }.toMap()

    override fun parse(text: String, metadata: StaticMetadata): Any? {
        try {
            return doParse(text, metadata)
        } catch (e: Exception) {
            throw PplParseException("Parsing error \n[from text]:$text\n[  to type]:$typeAdapter\n[  mapper ]:$valueMapper")
        }
    }

    override fun serialize(value: Any?, metadata: StaticMetadata): String {
        try {
            return doSerialize(value, metadata)
        } catch (e: Exception) {
            throw PplSerializeException("Serialization error at ${toString()} \n valueMapper:$valueMapper", e)
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

    override fun getFieldValue(parentObject: Any): Any? = typeAdapter.getFieldValue(parentObject)

    override fun setFieldValue(parentObject: Any, paramValue: Any?) = typeAdapter.setFieldValue(parentObject, paramValue)

    override fun getValueSize(value: Any?): Int = valueMapper.getValueSize(value)

    override fun maxArrayToValue(array: Array<Any?>): Any? = typeAdapter.maxArrayToValue(array)

    override fun valueToMaxArray(value: Any?, size: Int): Array<Any?> = typeAdapter.valueToMaxArray(value,size)

    override fun createAndFillArray(size: Int): Array<Any?> = typeAdapter. createAndFillArray(size)

    override fun valueToArray(value: Any?): Array<Any?> =typeAdapter.valueToArray(value)

    override fun toString(): String = "[$treeIndex] $fullName: $typeAdapter ($metaName) $metaInfo"

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

    override fun enumConstantToValue(constName: String): Any = typeAdapter.enumConstantToValue(constName)

}