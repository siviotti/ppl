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
package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.metadata.CreateMetadata
import br.net.buzu.pplimpl.metadata.createSpecificMetadata
import br.net.buzu.pplspec.exception.PplMetaclassViolationException
import br.net.buzu.pplspec.lang.PATH_SEP
import br.net.buzu.pplspec.model.*
import java.util.*


// ********** API **********

fun load(instance: Any?, metaclass: Metaclass, createMetadata: CreateMetadata = createSpecificMetadata): Metadata {
    val node = LoadNode(instance, metaclass, "")
    val maxMap = MaxMap()
    val max = getMax(maxMap, node, metaclass.info())
    return createMetadata(metaclass.info().update(max.maxSize, max.maxOccurs),
            createChildren(node, maxMap, createMetadata))
}

// ********** INTERNAL **********
private fun createChildren(node: LoadNode, maxMap: MaxMap, createMetadata: CreateMetadata): List<Metadata> {
    if (!node.isComplex || node.isEnum) {
        return listOf()
    }
    val metaclassList = node.metaclass.children<Metaclass>()
    val children = arrayOfNulls<Metadata>(metaclassList.size)
    var fieldValue: Any?
    var childMetaclass: Metaclass
    for (itemValue in node.value) {
        for (i in metaclassList.indices) {
            childMetaclass = metaclassList[i]
            fieldValue = if (itemValue != null) childMetaclass[itemValue] else null
            children[i] = loadChild(fieldValue, childMetaclass, node, maxMap, createMetadata)
        }
    }
    return Arrays.asList<Metadata>(*children)
}

private fun loadChild(fieldValue: Any?, metaclass: Metaclass, parentNode: LoadNode, maxMap: MaxMap, createMetadata: CreateMetadata): Metadata {
    val fieldPath = MaxMap.getFieldPath(metaclass, parentNode)
    val fieldNode = LoadNode(fieldValue, metaclass, fieldPath)
    var metaInfo = metaclass.info()
    val max = getMax(maxMap, fieldNode, metaInfo)
    metaInfo = metaInfo.update(max.maxSize, max.maxOccurs)
    return createMetadata(metaInfo, createChildren(fieldNode, maxMap, createMetadata))
}


private fun getMax(maxMap: MaxMap, node: LoadNode, metaInfo: MetaInfo): Max {
    val fieldPath = node.fieldPath
    val max = maxMap[fieldPath]
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

fun getFieldPath(metaclass: Metaclass, node: LoadNode): String {
    return if (node.fieldPath.isEmpty()) {
        metaclass.fieldName()
    } else node.fieldPath + PATH_SEP + metaclass.fieldName()
}

private data class Max(var maxSize: Int = 0, var maxOccurs: Int = 0) {

    fun tryNewMaxOccurs(newValue: Int): Max {
        if (newValue > maxOccurs) {
            maxOccurs = newValue
        }
        return this
    }

    fun tryNewMaxSize(newValue: Int): Max {
        if (newValue > maxSize) {
            maxSize = newValue
        }
        return this
    }

}

private class MaxMap {

    private val map = HashMap<String, Max>()

    operator fun get(fieldPath: String): Max {
        if (!map.containsKey(fieldPath)) {
            map[fieldPath] = Max()
        }
        return map[fieldPath]!!
    }

    companion object {

        internal fun getFieldPath(metaclass: Metaclass, node: LoadNode): String {
            return if (node.fieldPath.isEmpty()) {
                metaclass.fieldName()
            } else node.fieldPath + PATH_SEP + metaclass.fieldName()
        }
    }

}

class LoadNode (originalValue: Any?, val metaclass: Metaclass, val fieldPath: String) {

    val value: Array<Any?>
    val subtype: Subtype

    internal val isNull: Boolean
        get() = value[0] == null && value.size == 1

    val isComplex: Boolean
        get() = subtype.dataType().isComplex

    val isEnum: Boolean
        get() = metaclass.isEnum

    val occurs: Int
        get() = value.size

    init {
        this.subtype = metaclass.info().subtype
        if (originalValue == null) {
            this.value = arrayOf(1)
        } else if (metaclass.isCollection) {
            if ((originalValue as Collection<*>).isEmpty()) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue.toTypedArray()
            }
        } else if (metaclass.isArray) {
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
        if (subtype.dataType().sizeType() == SizeType.CUSTOM) {
            var max = 0
            var tmp = 0
            for (obj in value) {
                tmp = metaclass.getValueSize(obj!!)
                if (tmp > max) {
                    max = tmp
                }
            }
            return max
        }
        return subtype.fixedSize()
    }

}
