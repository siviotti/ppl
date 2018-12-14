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
import br.net.buzu.pplimpl.metadata.genericCreateMetadata
import br.net.buzu.pplspec.annotation.PplIgnore
import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.annotation.PplUse
import br.net.buzu.pplspec.exception.PplMetaclassViolationException
import br.net.buzu.pplspec.exception.PplReflectionException
import br.net.buzu.pplspec.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.pplspec.lang.PATH_SEP
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*

val genericSkip: (Field) -> Boolean = { skip(it) }

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

private inline fun getFieldPath(fieldName: String, node: LoadNode): String {
    return if (node.fieldPath.isEmpty()) fieldName else node.fieldPath + PATH_SEP + fieldName
}

private fun skip(field: Field): Boolean {
    // Precedence 1: Field explict ignore
    if (field.isAnnotationPresent(PplIgnore::class.java)) {
        return true
    }

    // Precedence 2: Field explicit use
    if (field.isAnnotationPresent(PplUse::class.java)) {
        return false
    }

    // Precedence 3: Static, Transient or Ignore
    if (Modifier.isStatic(field.modifiers)) {
        return true
    }
    if (Modifier.isTransient(field.modifiers)) {
        return true
    }

    // Precedence 4: Field Class
    var fieldClass: Class<*>
    try {
        fieldClass = getElementType(field)
    } catch (pre: PplReflectionException) {
        fieldClass = field.javaClass
    }

    return fieldClass.isAnnotationPresent(PplIgnore::class.java)
}

