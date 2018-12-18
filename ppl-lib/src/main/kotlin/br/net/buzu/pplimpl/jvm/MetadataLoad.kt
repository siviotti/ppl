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

import br.net.buzu.java.exception.PplMetaclassViolationException
import br.net.buzu.java.lang.PATH_SEP
import br.net.buzu.java.model.*
import br.net.buzu.pplimpl.metadata.CreateMetadata
import br.net.buzu.pplimpl.metadata.genericCreateMetadata
import java.util.*

@JvmOverloads
fun loadMetadata(rootInstance: Any, fieldAdapter: FieldAdapter, createMetadata: CreateMetadata = genericCreateMetadata): Metadata {
    val loadNode = LoadNode(rootInstance, fieldAdapter)
    val maxMap = MaxMap(fieldAdapter.nodeCount() + 1)
    val metaInfo = fieldAdapter.metaInfo
    val max = getMax(maxMap, loadNode, metaInfo)
    return createMetadata(metaInfo.update(max.maxSize, max.maxOccurs), createMetaChildren(loadNode, maxMap, createMetadata))
}

private fun createMetaChildren(node: LoadNode, maxMap: MaxMap, createMetadata: CreateMetadata): List<Metadata> {
    val typeAdapter = node.fieldAdapter
    if (!typeAdapter.hasChildren) {
        return listOf()
    }
    val typeList = typeAdapter.children
    val children = arrayOfNulls<Metadata>(typeList.size)
    var fieldValue: Any?
    var childAdapter: FieldAdapter
    for (itemValue in node.value) {
        for (i in typeList.indices) {
            childAdapter = typeList[i]
            fieldValue = if (itemValue != null) childAdapter.getFieldValue(itemValue) else null
            children[i] = loadChild(fieldValue, childAdapter, node, maxMap, createMetadata)
        }
    }
    return Arrays.asList<Metadata>(*children)
}

private fun loadChild(fieldValue: Any?, typeMapper: FieldAdapter, parentNode: LoadNode, maxMap: MaxMap,
                      createMetadata: CreateMetadata): Metadata {
    val fieldNode = LoadNode(fieldValue, typeMapper)
    var metaInfo = typeMapper.metaInfo
    val max = getMax(maxMap, fieldNode, metaInfo)
    metaInfo = metaInfo.update(max.maxSize, max.maxOccurs)
    val children = createMetaChildren(fieldNode, maxMap, createMetadata)
    return createMetadata(metaInfo, children)
}

private fun getMax(maxMap: MaxMap, node: LoadNode, metaInfo: MetaInfo): Max {
    val fieldPath = node.fieldAdapter.fieldPath
    val max = maxMap.getMaxByIndex(node.fieldAdapter.treeIndex)
    val size = max.tryNewMaxSize(node.calcMaxSize()).maxSize
    val maxOccurs = max.tryNewMaxOccurs(node.occurs).maxOccurs
    if (metaInfo.hasSize()) {
        checkLimit("size", fieldPath, metaInfo.size, size)
    }
    if (metaInfo.hasMaxOccurs()) {
        checkLimit("maxOccurs", fieldPath, metaInfo.maxOccurs, maxOccurs)
    }
    return max
}

private fun checkLimit(info: String, fieldPath: String, maxValue: Int, newValue: Int) {
    if (newValue > maxValue) {
        val sb = StringBuilder()
        sb.append(info).append(" violation on field '").append(fieldPath).append("'. Max value:'").append(maxValue)
                .append("' calculated value:'").append(newValue).append("'.")
        throw PplMetaclassViolationException(sb.toString())
    }
}

internal class LoadNode(originalValue: Any?, val fieldAdapter: FieldAdapter) {

    val value: Array<Any?>
    val subtype: Subtype = fieldAdapter.metaInfo.subtype
    val occurs: Int
        get() = value.size

    init {
        if (originalValue == null) {
            this.value = arrayOf(1)
        } else if (fieldAdapter.isCollection) {
            if ((originalValue as Collection<*>).isEmpty()) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue.toTypedArray()
            }
        } else if (fieldAdapter.isArray) {
            if ((originalValue as Array<Any>).size == 0) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue as Array<Any?>
            }
        } else {
            this.value = arrayOf(1)
            this.value[0] = originalValue
        }
    }

    fun calcMaxSize(): Int {
        if (subtype.dataType.sizeType == SizeType.CUSTOM) {
            var max = 0
            var tmp = 0
            for (obj in value) {
                tmp = fieldAdapter.getValueSize(obj!!)
                if (tmp > max) {
                    max = tmp
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
    private val map = Array(size, { Max() })
    fun getMaxByIndex(index: Int): Max {
        return map[index]
    }
}