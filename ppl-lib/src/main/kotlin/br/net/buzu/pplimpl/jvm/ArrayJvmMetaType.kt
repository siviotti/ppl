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

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.ValueMapper
import java.lang.reflect.Field

class ArrayJvmMetaType(fieldPath: String, fieldName: String, fieldType: Class<*>, elementType: Class<*>,
                       metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, field: Field, valueMapper: ValueMapper)
    : JvmMetaType(fieldPath, fieldName, fieldType, elementType, metaInfo, children, treeIndex, field, valueMapper) {

    override fun parse(text: String, metadata: StaticMetadata): Any? {
        val metaInfo: MetaInfo = metadata.info()
        var beginIndex = 0
        var endIndex = 0
        val array = createAndFillArray(metaInfo.maxOccurs)
        for (i in array.indices) {
            endIndex += metaInfo.size
            array[i] = parseAtomic(text.substring(beginIndex, endIndex), metadata)
            beginIndex += metaInfo.size
        }
        return maxArrayToValue(array)

    }


    override fun serialize(value: Any?, metadata: StaticMetadata): String {
        val sb = StringBuilder()
        val array = valueToMaxArray(value, metadata.info().maxOccurs)
        for (element in array) serializeAtomic(element, metadata)
        return sb.toString()
    }
}