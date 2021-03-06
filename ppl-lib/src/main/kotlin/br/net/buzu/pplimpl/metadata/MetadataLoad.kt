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
package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.exception.PplMetaclassViolationException
import br.net.buzu.pplspec.ext.MetadataFactory
import br.net.buzu.pplspec.model.*
import java.util.*

@JvmOverloads
fun loadMetadata(rootInstance: Any, metaType: MetaType, factory: MetadataFactory = GenericMetadataFactory): Metadata {
    val loadNode = LoadNode(rootInstance, metaType)
    val maxMap = MaxMap(metaType.treeSize() + 1)
    val metaInfo = metaType.metaInfo
    val max = getMax(maxMap, loadNode, metaInfo)
    return factory.create(metaInfo.update(max.maxSize, max.maxOccurs), createMetaChildren(loadNode, maxMap, factory))
}

internal fun createMetaChildren(node: LoadNode, maxMap: MaxMap, factory: MetadataFactory): List<Metadata> {
    val metaType = node.metaType
    if (!metaType.hasChildren) {
        return listOf()
    }
    val metaTypeList = metaType.children
    val children = arrayOfNulls<Metadata>(metaTypeList.size)
    var fieldValue: Any?
    var childMetaType: MetaType
    for (itemValue in node.value) {
        for (i in metaTypeList.indices) {
            childMetaType = metaTypeList[i]
            fieldValue = if (itemValue != null) childMetaType.typeAdapter.getFieldValue(itemValue) else null
            children[i] = loadChild(fieldValue, childMetaType, maxMap, factory)
        }
    }
    return Arrays.asList<Metadata>(*children)
}

internal fun loadChild(fieldValue: Any?, metaType: MetaType, maxMap: MaxMap,
                       factory: MetadataFactory): Metadata {
    val fieldNode = LoadNode(fieldValue, metaType)
    var metaInfo = metaType.metaInfo
    val max = getMax(maxMap, fieldNode, metaInfo)
    metaInfo = metaInfo.update(max.maxSize, max.maxOccurs)
    val children = createMetaChildren(fieldNode, maxMap, factory)
    return factory.create(metaInfo, children)
}

internal fun getMax(maxMap: MaxMap, node: LoadNode, metaInfo: MetaInfo): Max {
    val fieldPath = node.metaType.fullName
    val max = maxMap.getMaxByIndex(node.metaType.treeIndex)
    val size = max.tryNewMaxSize(node.calcMaxSize()).maxSize
    val maxOccurs = max.tryNewMaxOccurs(node.occurs).maxOccurs
    if (metaInfo.hasSize()) {
        checkLimit("size", fieldPath, metaInfo.size, size)
        max.tryNewMaxSize(metaInfo.size)
    }
    if (metaInfo.hasMaxOccurs()) {
        checkLimit("maxOccurs", fieldPath, metaInfo.maxOccurs, maxOccurs)
        max.tryNewMaxOccurs(metaInfo.maxOccurs)
    }
    return max
}

internal fun checkLimit(info: String, fieldPath: String, maxValue: Int, newValue: Int) {
    if (newValue > maxValue) {
        val sb = StringBuilder()
        sb.append(info).append(" violation on field '").append(fieldPath).append("'. Max value:'").append(maxValue)
                .append("' calculated value:'").append(newValue).append("'.")
        throw PplMetaclassViolationException(sb.toString())
    }
}

internal class LoadNode(originalValue: Any?, val metaType: MetaType) {

    val value: Array<Any?> = metaType.typeAdapter.valueToArray(originalValue)
    val subtype: Subtype = metaType.metaInfo.subtype
    val occurs: Int
        get() = value.size

    fun calcMaxSize(): Int {
        if (subtype.dataType.sizeType == SizeType.CUSTOM) {
            var max = 0
            var tmp: Int
            for (obj in value) {
                if (obj != null) {
                    tmp = metaType.valueMapper.getValueSize(obj, metaType.metaInfo)
                    if (tmp > max) {
                        max = tmp
                    }
                }
            }
            return max
        }
        return subtype.fixedSize()
    }
}

internal data class Max(var maxSize: Int = 0, var maxOccurs: Int = 0) {

    fun tryNewMaxOccurs(newValue: Int): Max {
        if (newValue > maxOccurs) maxOccurs = newValue
        return this
    }

    fun tryNewMaxSize(newValue: Int): Max {
        if (newValue > maxSize) maxSize = newValue
        return this
    }
}

internal class MaxMap(val size: Int) {
    private val map = Array(size) { Max() }
    fun getMaxByIndex(index: Int): Max = map[index]
}