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
package br.net.buzu.pplimpl.payload

import br.net.buzu.java.model.MetaType
import br.net.buzu.java.model.Kind
import br.net.buzu.java.model.MetaInfo
import br.net.buzu.java.model.StaticMetadata

fun parsePayload(text: String, metadata: StaticMetadata, metaType: MetaType): Any? =
        when (metadata.kind()) {
            Kind.ATOMIC -> parseAtomic(text, metadata, metaType)
            Kind.ARRAY -> parseArray(text, metadata, metaType)
            else -> {
                parseTable(text, metadata, metaType)
            }
        }

internal fun parseAtomic(text: String, metadata: StaticMetadata, metaType: MetaType): Any? {
    val metaInfo: MetaInfo = metadata.info()
    return if (isNull(text, metaInfo.nullChar))
        if (metaInfo.hasDefaultValue())
            metaType.asSingleObject(metadata.info().defaultValue)
        else null
    else
        metaType.asSingleObject(text.substring(0, metaInfo.size))
}

internal fun parseArray(text: String, metadata: StaticMetadata, metaType: MetaType): Any? {
    val metaInfo: MetaInfo = metadata.info()
    var beginIndex = 0
    var endIndex = 0
    val array = metaType.createAndFillArray(metaInfo.maxOccurs)
    for (i in array.indices) {
        endIndex += metaInfo.size
        array[i] = parseAtomic(text.substring(beginIndex,endIndex), metadata, metaType)
        beginIndex += metaInfo.size
    }
    return metaType.maxArrayToValue(array)
}

internal fun parseTable(text: String, metadata: StaticMetadata, metaType: MetaType): Any? {
    println(metaType.toString())
    val metaInfo: MetaInfo = metadata.info()
    var beginIndex = 0
    var endIndex = 0
    var parsed: Any?
    var childMetaType: MetaType
    val array = metaType.createAndFillArray(metaInfo.maxOccurs)
    println("array class:" + array[0]?.javaClass)
    for (i in array.indices) {
        for (childMetadata in metadata.children<StaticMetadata>()) {
            println("childMetadata: ${childMetadata.name()}")
            childMetaType = metaType.getChildByMetaName(childMetadata.name())
            endIndex += childMetadata.serialMaxSize()
            parsed = parsePayload(text.substring(beginIndex, endIndex), childMetadata, childMetaType)
            println("parsed: $parsed")
            beginIndex += childMetadata.serialMaxSize()
            childMetaType.setFieldValue(array[i]!!, parsed)
        }
    }
    return metaType.maxArrayToValue(array)
}

internal fun isNull(text: String, nullChar: Char): Boolean {
    for (i in 0 until text.length) if (text[i] != nullChar) return false
    return true
}
