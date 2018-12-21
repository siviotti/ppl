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
import br.net.buzu.lib.fill
import br.net.buzu.lib.fit
import java.lang.IllegalArgumentException

fun serializePayload(value: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String =
        when (metadata.kind()) {
            Kind.ATOMIC -> serializeAtomic(value, metadata, fieldAdapter)
            Kind.ARRAY -> serializeArray(value, metadata, fieldAdapter)
            else -> {
                serializeTable(value, metadata, fieldAdapter)
            }
        }


internal fun serializeAtomic(atomicValue: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String {
    return if (atomicValue == null) {
        serializeNull(metadata.info())
    } else {
        serializeValue(atomicValue, metadata.info(), fieldAdapter)
    }
}

internal fun serializeArray(arrayValue: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String {
    val sb = StringBuilder()
    val array = valueToMaxArray(arrayValue, metadata.info().maxOccurs, fieldAdapter)
    for (element in array) {
        if (element == null) {
            sb.append(serializeNull(metadata.info()))
        } else {
            sb.append(serializeValue(element, metadata.info(), fieldAdapter))
        }
    }
    return sb.toString()
}

internal fun serializeTable(tableValue: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String {
    val sb = StringBuilder()
    val array = valueToMaxArray(tableValue, metadata.info().maxOccurs, fieldAdapter)
    var childFieldAdapter: FieldAdapter
    for (element in array) {
        for (childMetadata in metadata.children<StaticMetadata>()) {
            childFieldAdapter = fieldAdapter.getChildByMetaName(childMetadata.name())
            if (element != null) {
                sb.append(serializePayload(childFieldAdapter.getFieldValue(element), childMetadata, childFieldAdapter))
            } else {
                sb.append(serializeNull(childMetadata.info()))
            }
        }
    }
    return sb.toString()
}

internal fun valueToMaxArray(value: Any?, size: Int, fieldAdapter: FieldAdapter): Array<Any?> {
    return when {
        size == 0 -> arrayOf(0)
        value is Collection<*> -> value.toTypedArray()
        fieldAdapter.isArray -> value as Array<Any?>
        else -> arrayOf(value)
    }
}

internal fun serializeValue(value: Any, metaInfo: MetaInfo, fieldAdapter: FieldAdapter): String {
    return fit(metaInfo.align, fieldAdapter.asStringFromNotNull(value), metaInfo.size, metaInfo.fillChar)
}

internal fun serializeNull(metaInfo: MetaInfo): String {
    return fill(metaInfo.align, metaInfo.defaultValue, metaInfo.size, metaInfo.nullChar)
}
