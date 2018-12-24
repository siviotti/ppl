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
import br.net.buzu.lib.fill
import br.net.buzu.lib.fit

fun serializePayload(value: Any?, metadata: StaticMetadata, metaType: MetaType): String =
        when (metadata.kind()) {
            Kind.ATOMIC -> serializeAtomic(value, metadata, metaType)
            Kind.ARRAY -> serializeArray(value, metadata, metaType)
            else -> {
                serializeTable(value, metadata, metaType)
            }
        }


internal fun serializeAtomic(atomicValue: Any?, metadata: StaticMetadata, metaType: MetaType): String {
    return if (atomicValue == null) {
        serializeNull(metadata.info())
    } else {
        serializeValue(atomicValue, metadata.info(), metaType)
    }
}

internal fun serializeArray(arrayValue: Any?, metadata: StaticMetadata, metaType: MetaType): String {
    val sb = StringBuilder()
    val array = metaType.valueToMaxArray(arrayValue, metadata.info().maxOccurs)
    for (element in array) {
        if (element == null) {
            sb.append(serializeNull(metadata.info()))
        } else {
            sb.append(serializeValue(element, metadata.info(), metaType))
        }
    }
    return sb.toString()
}

internal fun serializeTable(tableValue: Any?, metadata: StaticMetadata, metaType: MetaType): String {
    val sb = StringBuilder()
    val array = metaType.valueToMaxArray(tableValue, metadata.info().maxOccurs)
    var childMetaType: MetaType
    for (element in array) {
        for (childMetadata in metadata.children<StaticMetadata>()) {
            childMetaType = metaType.getChildByMetaName(childMetadata.name())
            if (element != null) {
                sb.append(serializePayload(childMetaType.getFieldValue(element), childMetadata, childMetaType))
            } else {
                sb.append(serializeNull(childMetadata.info()))
            }
        }
    }
    return sb.toString()
}


internal fun serializeValue(value: Any, metaInfo: MetaInfo, fieldAdapter: MetaType): String {
    return fit(metaInfo.align, fieldAdapter.asStringFromNotNull(value), metaInfo.size, metaInfo.fillChar)
}

internal fun serializeNull(metaInfo: MetaInfo): String {
    return fill(metaInfo.align, metaInfo.defaultValue, metaInfo.size, metaInfo.nullChar)
}
