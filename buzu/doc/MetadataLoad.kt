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

val genericSkip : (Field)-> Boolean = {skip(it)}

// ********** API **********

fun loadMetadata(rootInstance: Any): Metadata {
    return loadMetadata(rootInstance, genericCreateMetadata, genericSkip)
}

fun loadMetadata(rootInstance: Any, createMetadata: CreateMetadata = genericCreateMetadata, skip:(Field)->Boolean= genericSkip): Metadata {
    val node = br.net.buzu.pplimpl.metadata.loadNodeOf(rootInstance)
    val maxMap = MaxMap()
    val rootClass = rootInstance.javaClass
    var pplMetadata: PplMetadata? = rootClass.getAnnotation(PplMetadata::class.java)
    val metaInfo = createMetaInfo(node.fieldPath, node.pplMetadata, node.elementType, node.fieldName)
    val max = getMax(maxMap, node, metaInfo)
    return createMetadata(metaInfo.update(max.maxSize, max.maxOccurs), createChildren(node, maxMap, createMetadata, skip))
}

// ********** INTERNAL **********
private fun createChildren(node: LoadNode, maxMap: MaxMap, createMetadata: CreateMetadata, skip: (Field)-> Boolean): List<Metadata> {
    if (!node.hasChildren) {
        return listOf()
    }
    //println("HAS Children:"+node.elementType + " subtype: ${node.subtype}")
    val fields = getAllFields(node.elementType).filterNot(skip)
    val children = arrayOfNulls<Metadata>(fields.size)
    var fieldValue: Any?
    var field: Field
    for (itemValue in node.value) {
        for (i in fields.indices) {
            field = fields[i]
            fieldValue = if (itemValue != null) getValueFromInstanceField(field, itemValue) else null
            children[i] = loadChild(fieldValue, field, node, maxMap, createMetadata, skip)
        }
    }
    return Arrays.asList<Metadata>(*children).sortedBy { it.info().index }
}

private fun loadChild(fieldValue: Any?, field: Field, parentNode: LoadNode, maxMap: MaxMap, createMetadata: CreateMetadata, skip: (Field)-> Boolean): Metadata {
    val fieldPath = getFieldPath(field.name, parentNode)
    val node = br.net.buzu.pplimpl.metadata.loadNodeOf(fieldValue, field, fieldPath)
    val metaInfo = createMetaInfo(node.fieldPath, node.pplMetadata, node.elementType, node.fieldName)
    val max = getMax(maxMap, node, metaInfo)
    val children= createChildren(node, maxMap, createMetadata, skip)
    return createMetadata(metaInfo.update(max.maxSize, max.maxOccurs), children)
}

private fun createMetaInfo(parentId: String, pplMetadata: PplMetadata?, elementType: Class<*>, fieldName: String): MetaInfo {
    val subtype = fromType(elementType)
    return if (pplMetadata != null)
        MetaInfo(parentId, pplMetadata, fieldName, subtype)
    else
        MetaInfo(parentId, 0, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}

private fun getMax(maxMap: MaxMap, node: LoadNode, metaInfo: MetaInfo): Max {
    val fieldPath = node.fieldPath
    val max = maxMap.getOrCreate(fieldPath)
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

fun getFieldPath(fieldName: String, node: LoadNode): String {
    return if (node.fieldPath.isEmpty()) {
        fieldName
    } else node.fieldPath + PATH_SEP + fieldName
}

fun getFieldPath(fieldName: String, parentPath: String): String {
    return if (parentPath.isEmpty()) {
        fieldName
    } else parentPath+ PATH_SEP + fieldName
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

