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

import br.net.buzu.java.model.FieldAdapter
import br.net.buzu.java.model.Kind
import br.net.buzu.java.model.MetaInfo
import br.net.buzu.java.model.StaticMetadata

fun parsePayload(text: String, metadata: StaticMetadata, fieldAdapter: FieldAdapter): Any? =
        when (metadata.kind()) {
            Kind.ATOMIC -> parseAtomic(text, metadata, fieldAdapter)
            Kind.ARRAY -> parseArray(text, metadata, fieldAdapter)
            else -> {
                parseTable(text, metadata, fieldAdapter)
            }
        }

internal fun parseAtomic(text: String, metadata: StaticMetadata, fieldAdapter: FieldAdapter): Any? {
    val metaInfo: MetaInfo = metadata.info()
    var s = text
    if (isNull(text, metaInfo.nullChar)) {
        if (metaInfo.hasDefaultValue()) {
            s = metaInfo.defaultValue
        } else {
            return null
        }
    }
    return fieldAdapter.asSingleObject(s.substring(0, metaInfo.size))
}


internal fun parseArray(text: String, metadata: StaticMetadata, fieldAdapter: FieldAdapter): Any? {
    val metaInfo: MetaInfo = metadata.info()
    var s = text
    if (isNull(text, metaInfo.nullChar)) {
        if (metaInfo.hasDefaultValue()) {
            s = metaInfo.defaultValue
        } else {
            return null
        }
    }
    var beginIndex = 0
    var endIndex = 0
    val array = createAndFillArray(fieldAdapter, metaInfo.maxOccurs)
    for (i in array.indices) {
        endIndex += metaInfo.size
        array[i] = fieldAdapter.asSingleObject(s.substring(beginIndex, endIndex))
        beginIndex += metadata.info().size
    }
    return fromArray(array, fieldAdapter)
}

internal fun parseTable(text: String, metadata: StaticMetadata, fieldAdapter: FieldAdapter): Any? {
    println(fieldAdapter.toString())
    val metaInfo: MetaInfo = metadata.info()
    var beginIndex = 0
    var endIndex = 0
    var parsed: Any?
    var childFieldAdapter: FieldAdapter
    val array = createAndFillArray(fieldAdapter, metaInfo.maxOccurs)
    println("array class:"+ array[0]?.javaClass)
    for (i in array.indices) {
        for (childMetadata in metadata.children<StaticMetadata>()) {
            println("childMetadata: ${childMetadata.name()}")
            childFieldAdapter = fieldAdapter.getChildByMetaName(childMetadata.name())
            endIndex += childMetadata.serialMaxSize()
            parsed = parsePayload(text.substring(beginIndex, endIndex), childMetadata, childFieldAdapter)
            println("parsed: $parsed")
            beginIndex += childMetadata.serialMaxSize()
            childFieldAdapter.setFieldValue(array[i]!!, parsed)
        }
    }
    return fromArray(array, fieldAdapter)
}

internal fun createAndFillArray(fieldAdapter: FieldAdapter, size: Int): Array<Any?> {
    return if (fieldAdapter.isComplex) Array(size) {fieldAdapter.createNewInstance()} else arrayOf()
}

internal fun fromArray(array: Array<Any?>, fieldAdapter: FieldAdapter): Any {
    if (fieldAdapter.isArray) {
        return array
    }
    if (fieldAdapter.isCollection) {
        if (Set::class.java.isAssignableFrom(fieldAdapter.elementType)) {
            return array.toSet()
        }
        return array.toList()
    }
    return array[0]!!
}

internal fun isNull(text: String, nullChar: Char): Boolean {
    for (i in 0 until text.length) if (text[i] != nullChar) return false
    return true
}

internal fun parseValue(substring: String, metadata: StaticMetadata, fieldAdapter: FieldAdapter): Any? {
    return fieldAdapter.asSingleObject(substring)
}
