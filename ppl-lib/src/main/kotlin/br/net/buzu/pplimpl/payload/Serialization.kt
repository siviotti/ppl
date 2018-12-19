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
            Kind.RECORD -> serializeRecord(value, metadata, fieldAdapter)
            Kind.ARRAY -> serializeTable(value, metadata, fieldAdapter)
            else -> {
                throw IllegalArgumentException()
            }
        }


fun serializeAtomic(value: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String {
    val sb = StringBuilder()
    if (value == null) {
        sb.append(serializeNull(metadata.info()))
    } else {
        sb.append(serializeValue(metadata.info(), value, fieldAdapter))
    }
    return sb.toString()
}

fun serializeArray(value: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String {
    val sb = StringBuilder()
    val array = valueToArray(value)
    for (i in array.indices) {
        if (array[i] == null) {
            sb.append(serializeNull(metadata.info()))
        } else {
            sb.append(serializeValue(metadata.info(), array[i]!!, fieldAdapter))
        }
    }
    return sb.toString()
}

fun valueToArray(value: Any?): Array<Any?> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

fun serializeRecord(value: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String {
    val sb = StringBuilder()
    var childFieldAdapter: FieldAdapter
    val staticMetadataChildren = metadata.children<StaticMetadata>()
    for (childMetadata in metadata.children<StaticMetadata>()) {
        childFieldAdapter = fieldAdapter.getChildByMetaName(metadata.name())
        sb.append(serializePayload(value, childMetadata, childFieldAdapter))
    }
    return sb.toString()
}

fun serializeTable(value: Any?, metadata: StaticMetadata, fieldAdapter: FieldAdapter): String {
    val sb = StringBuilder()
    val array = valueToArray(value)
    var childFieldAdapter: FieldAdapter
    val staticMetadataChildren = metadata.children<StaticMetadata>()
    for (i in array.indices) {
        for (childMetadata in metadata.children<StaticMetadata>()) {
            childFieldAdapter = fieldAdapter.getChildByMetaName(metadata.name())
            sb.append(serializePayload(array[i], childMetadata, childFieldAdapter))
        }
    }
    return sb.toString()
}

fun serializeValue(metaInfo: MetaInfo, value: Any, fieldAdapter: FieldAdapter): String {
    return fit(metaInfo.align, fieldAdapter.asStringFromNotNull(value), metaInfo.size, metaInfo.fillChar)
}

fun serializeNull(metaInfo: MetaInfo): String {
    return fill(metaInfo.align, metaInfo.defaultValue, metaInfo.size, metaInfo.nullChar)
}
