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
package br.net.buzu.pplimpl.core

import br.net.buzu.model.*

fun positionalParse(text: String, metadata: StaticMetadata, metaType: MetaType): Any?{
    return when (metadata.kind()){
        Kind.ATOMIC -> atomicParse(text, metadata.info(), metaType.valueMapper)
        Kind.ARRAY -> arrayParse(text, metadata.info(), metaType.typeAdapter, metaType.valueMapper)
        else -> complexParse(text, metadata, metaType)
    }
}

fun positionalSerialize(value: Any?, metadata: StaticMetadata, metaType: MetaType): String{
    return when (metadata.kind()){
        Kind.ATOMIC -> atomicSerialize(value, metadata.info(), metaType.valueMapper)
        Kind.ARRAY -> arraySerialize(value, metadata.info(), metaType.typeAdapter, metaType.valueMapper)
        else -> complexSerialize(value, metadata, metaType)
    }
}

fun atomicParse(text: String, metaInfo: MetaInfo, valueMapper: ValueMapper): Any? {
    return if (isPositionalNull(text, metaInfo.nullChar))
        if (metaInfo.hasDefaultValue()) valueMapper.toValue(metaInfo.defaultValue, metaInfo) else null
    else
        valueMapper.toValue(text, metaInfo)
}

fun atomicSerialize(value: Any?, metaInfo: MetaInfo, valueMapper: ValueMapper): String {
    return if (value == null) fill(metaInfo.align, metaInfo.defaultValue, metaInfo.size, metaInfo.nullChar)
    else fit(metaInfo.align, valueMapper.toText(value), metaInfo.size, metaInfo.fillChar)
}

fun arrayParse(text: String, metaInfo: MetaInfo, typeAdapter: TypeAdapter, valueMapper: ValueMapper): Any? {
    var beginIndex = 0
    var endIndex = 0
    val array = typeAdapter.createAndFillArray(metaInfo.maxOccurs)
    for (i in array.indices) {
        endIndex += metaInfo.size
        array[i] = atomicParse(text.substring(beginIndex, endIndex), metaInfo, valueMapper)
        beginIndex += metaInfo.size
    }
    return typeAdapter.maxArrayToValue(array)
}

fun arraySerialize(value: Any?, metaInfo: MetaInfo, typeAdapter: TypeAdapter, valueMapper: ValueMapper): String {
    val sb = StringBuilder()
    val array = typeAdapter.valueToMaxArray(value, metaInfo.maxOccurs)
    for (element in array) sb.append(atomicSerialize(element, metaInfo, valueMapper))
    return sb.toString()
}

fun complexParse(text: String, metadata: StaticMetadata, metaType: MetaType): Any? {
    var beginIndex = 0
    var endIndex = 0
    var parsed: Any?
    var childMetaType: MetaType
    val array = metaType.typeAdapter.createAndFillArray(metadata.info().maxOccurs)
    for (i in array.indices) {
        for (childMetadata in metadata.children<StaticMetadata>()) {
            childMetaType = metaType.getChildByMetaName(childMetadata.name())
            endIndex += childMetadata.serialMaxSize()
            parsed = positionalParse (text.substring(beginIndex, endIndex), childMetadata, childMetaType)
            beginIndex += childMetadata.serialMaxSize()
            childMetaType.typeAdapter.setFieldValue(array[i]!!, parsed)
        }
    }
    return metaType.typeAdapter.maxArrayToValue(array)
}

fun complexSerialize(value: Any?, metadata: StaticMetadata, metaType: MetaType): String {
    val sb = StringBuilder()
    val array = metaType.typeAdapter.valueToMaxArray(value, metadata.info().maxOccurs)
    var childMetaType: MetaType
    for (element in array) {
        for (childMetadata in metadata.children<StaticMetadata>()) {
            childMetaType = metaType.getChildByMetaName(childMetadata.name())
            sb.append(positionalSerialize(childMetaType.typeAdapter.getFieldValue(element!!), childMetadata, childMetaType))
        }
    }
    return sb.toString()
}

fun isPositionalNull(text: String, nullChar: Char): Boolean {
    for (i in 0 until text.length) if (text[i] != nullChar) return false
    return true
}
