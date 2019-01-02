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
        Kind.ATOMIC -> atomicParse(text, metadata, metaType.valueMapper)
        Kind.ARRAY -> arrayParse(text, metadata, metaType.typeAdapter, metaType.valueMapper)
        else -> complexParse(text, metadata, metaType)
    }
}

fun atomicParse(text: String, metadata: StaticMetadata, valueMapper: ValueMapper): Any? {
    val info: MetaInfo = metadata.info()
    return if (isPositionalNull(text, info.nullChar))
        if (info.hasDefaultValue()) valueMapper.toValue(metadata.info().defaultValue, info) else null
    else
        valueMapper.toValue(text, info)
}

fun atomicSerialize(value: Any?, metadata: StaticMetadata, valueMapper: ValueMapper): String {
    val info: MetaInfo = metadata.info()
    return if (value == null) fill(info.align, info.defaultValue, info.size, info.nullChar)
    else fit(info.align, valueMapper.toText(value), info.size, info.fillChar)
}

fun arrayParse(text: String, metadata: StaticMetadata, typeAdapter: TypeAdapter, valueMapper: ValueMapper): Any? {
    val metaInfo: MetaInfo = metadata.info()
    var beginIndex = 0
    var endIndex = 0
    val array = typeAdapter.createAndFillArray(metaInfo.maxOccurs)
    for (i in array.indices) {
        endIndex += metaInfo.size
        array[i] = atomicParse(text.substring(beginIndex, endIndex), metadata, valueMapper)
        beginIndex += metaInfo.size
    }
    return typeAdapter.maxArrayToValue(array)

}

fun arraySerialize(value: Any?, metadata: StaticMetadata, typeAdapter: TypeAdapter, valueMapper: ValueMapper): String {
    val sb = StringBuilder()
    val array = typeAdapter.valueToMaxArray(value, metadata.info().maxOccurs)
    for (element in array) sb.append(atomicSerialize(element, metadata, valueMapper))
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
            parsed = childMetaType.parse (text.substring(beginIndex, endIndex), childMetadata)
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
            sb.append(childMetaType.serialize(childMetaType.typeAdapter.getFieldValue(element!!), childMetadata))
        }
    }
    return sb.toString()
}



fun isPositionalNull(text: String, nullChar: Char): Boolean {
    for (i in 0 until text.length) if (text[i] != nullChar) return false
    return true
}
