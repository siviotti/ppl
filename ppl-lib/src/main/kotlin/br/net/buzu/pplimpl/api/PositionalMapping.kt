/*
 *	This file is part of DefaultPplMapper.
 *
 *   DefaultPplMapper is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DefaultPplMapper is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with DefaultPplMapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.api

import br.net.buzu.pplimpl.core.arrayParse
import br.net.buzu.pplimpl.core.arraySerialize
import br.net.buzu.pplimpl.core.atomicParse
import br.net.buzu.pplimpl.core.atomicSerialize
import br.net.buzu.pplspec.api.PositionalMapper
import br.net.buzu.pplspec.ext.PositionalMapperFactory
import br.net.buzu.pplspec.model.*

fun positionalMapperOf(metadata: StaticMetadata, metaType: MetaType): PositionalMapper {
    val info = metadata.info()
    return when (metadata.kind()) {
        Kind.ATOMIC -> AtomicPositionalMapper(info, metadata.serialMaxSize(), metaType.typeAdapter, metaType.valueMapperOf(info))
        Kind.ARRAY -> ArrayPositionalMapper(info, metadata.serialMaxSize(), metaType.typeAdapter, metaType.valueMapperOf(info))
        else -> ComplexPositionalMapper(info, metadata.serialMaxSize(), metaType.typeAdapter, createChildren(metadata, metaType))
    }
}

private fun createChildren(metadata: StaticMetadata, metaType: MetaType): List<BasePositionalMapper> {
    val children = mutableListOf<BasePositionalMapper>()
    for (childMetadata in metadata.children<StaticMetadata>()) {
        children.add(positionalMapperOf(childMetadata, metaType.getChildByMetaName(childMetadata.name())) as BasePositionalMapper)
    }
    return children
}

abstract class BasePositionalMapper(val metaInfo: MetaInfo, val serialSize: Int, val typeAdapter: TypeAdapter) : PositionalMapper

internal class AtomicPositionalMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, private val valueMapper: ValueMapper)
    : BasePositionalMapper(metaInfo, serialSize, typeAdapter) {

    override fun parse(text: String): Any? = atomicParse(text, metaInfo, valueMapper)
    override fun serialize(value: Any?): String = atomicSerialize(value, metaInfo, valueMapper)
}

internal class ArrayPositionalMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, private val valueMapper: ValueMapper)
    : BasePositionalMapper(metaInfo, serialSize, typeAdapter) {

    override fun parse(text: String): Any? = arrayParse(text, metaInfo, typeAdapter, valueMapper)
    override fun serialize(value: Any?): String = arraySerialize(value, metaInfo, typeAdapter, valueMapper)
}

internal class ComplexPositionalMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, val children: List<BasePositionalMapper>)
    : BasePositionalMapper(metaInfo, serialSize, typeAdapter) {

    override fun parse(text: String): Any? {
        var beginIndex = 0
        var endIndex = 0
        var parsed: Any?
        val array = typeAdapter.createAndFillArray(metaInfo.maxOccurs)
        for (i in array.indices) {
            for (child in children) {
                endIndex += child.serialSize
                parsed = child.parse(text.substring(beginIndex, endIndex))
                beginIndex += child.serialSize
                child.typeAdapter.setFieldValue(array[i]!!, parsed)
            }
        }
        return typeAdapter.maxArrayToValue(array)
    }

    override fun serialize(value: Any?): String {
        val sb = StringBuilder()
        val array = typeAdapter.valueToMaxArray(value, metaInfo.maxOccurs)
        for (element in array) {
            for (child in children) {
                sb.append(child.serialize(child.typeAdapter.getFieldValue(element!!)))
            }
        }
        return sb.toString()
    }
}

object GenericPositionalMapperFactpry : PositionalMapperFactory {
    override fun create(metadata: StaticMetadata, metaType: MetaType): PositionalMapper = positionalMapperOf(metadata, metaType)
}