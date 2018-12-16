/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplimpl.jvm.genericSkip
import br.net.buzu.pplimpl.jvm.read
import br.net.buzu.pplspec.exception.PplMetaclassViolationException
import br.net.buzu.pplspec.lang.PATH_SEP
import br.net.buzu.pplspec.model.*
import java.lang.reflect.Field
import java.util.*


// ********** API **********

fun loadMetadata(rootInstance: Any): Metadata {
    return loadMetadata(rootInstance, genericCreateMetadata, genericSkip)
}

fun loadMetadata(rootInstance: Any, createMetadata: CreateMetadata = genericCreateMetadata, skip: (Field) -> Boolean = genericSkip): Metadata {
    val typeInfo = read(rootInstance.javaClass, skip)
    val node = LoadNode(rootInstance, typeInfo, "")
    val maxMap = MaxMap(typeInfo.nodeCount() + 1)
    val metaInfo = typeInfo.metaInfo
    val max = getMax(maxMap, node, metaInfo)
    return createMetadata(metaInfo.update(max.maxSize, max.maxOccurs), createMetaChildren(node, maxMap, createMetadata, skip))
}

// ********** INTERNAL **********
private fun createMetaChildren(node: LoadNode, maxMap: MaxMap, createMetadata: CreateMetadata, skip: (Field) -> Boolean): List<Metadata> {
    val typeInfo = node.typeInfo
    if (!typeInfo.hasChildren) {
        return listOf()
    }
    val typeList = typeInfo.children
    val children = arrayOfNulls<Metadata>(typeList.size)
    var fieldValue: Any?
    var childTypeInfo: TypeInfo
    for (itemValue in node.value) {
        for (i in typeList.indices) {
            childTypeInfo = typeList[i]
            fieldValue = if (itemValue != null) childTypeInfo.getValue(itemValue) else null
            children[i] = loadChild(fieldValue, childTypeInfo, node, maxMap, createMetadata, skip)
        }
    }
    return Arrays.asList<Metadata>(*children)
}

private fun loadChild(fieldValue: Any?, typeInfo: TypeInfo, parentNode: LoadNode, maxMap: MaxMap,
                      createMetadata: CreateMetadata, skip: (Field) -> Boolean): Metadata {
    val fieldNode = LoadNode(fieldValue, typeInfo, getFieldPath(typeInfo.fieldName, parentNode))
    var metaInfo = typeInfo.metaInfo
    val max = getMax(maxMap, fieldNode, metaInfo)
    metaInfo = metaInfo.update(max.maxSize, max.maxOccurs)
    val children = createMetaChildren(fieldNode, maxMap, createMetadata, skip)
    return createMetadata(metaInfo, children)
}

private fun getMax(maxMap: MaxMap, node: LoadNode, metaInfo: MetaInfo): Max {
    val fieldPath = node.fieldPath
    val max = maxMap.getMaxByIndex(node.typeInfo.treeIndex)
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

private fun getFieldPath(fieldName: String, node: LoadNode): String {
    return if (node.fieldPath.isEmpty()) fieldName else node.fieldPath + PATH_SEP + fieldName
}


internal class LoadNode(originalValue: Any?, val typeInfo: TypeInfo, val fieldPath: String) {

    val value: Array<Any?>
    val subtype: Subtype = typeInfo.metaInfo.subtype
    val occurs: Int
        get() = value.size

    init {
        if (originalValue == null) {
            this.value = arrayOf(1)
        } else if (typeInfo.isCollection) {
            if ((originalValue as Collection<*>).isEmpty()) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue.toTypedArray()
            }
        } else if (typeInfo.isArray) {
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
                tmp = typeInfo.getValueSize(obj!!)
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
