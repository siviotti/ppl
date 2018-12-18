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

import br.net.buzu.pplimpl.metadata.IndexSequence
import br.net.buzu.java.annotation.PplIgnore
import br.net.buzu.java.annotation.PplMetadata
import br.net.buzu.java.annotation.PplUse
import br.net.buzu.java.exception.PplReflectionException
import br.net.buzu.java.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.java.lang.EMPTY
import br.net.buzu.java.lang.PATH_SEP
import br.net.buzu.java.model.MetaInfo
import br.net.buzu.java.model.PplSerializable
import br.net.buzu.java.model.FieldAdapter
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.math.BigDecimal

val genericSkip: (Field) -> Boolean = { skip(it) }

class JvmFieldAdapter(fieldPath: String, fieldName: String, fieldType: Class<*>, elementType: Class<*>, metaInfo: MetaInfo, children: List<FieldAdapter>, treeIndex: Int, val field: Field) :
        FieldAdapter(fieldPath, fieldName, fieldType, elementType, metaInfo, children, treeIndex) {

    override fun getFieldValue(parentObject: Any): Any? = getValue(field, parentObject)

    override fun setFieldValue(parentObject: Any, paramValue: Any?) = setValue(field, parentObject, paramValue)

    override fun getValueSize(value: Any?): Int {
        if (value == null) {
            return 0
        }
        val str = if (PplSerializable::class.java.isAssignableFrom(value.javaClass)) {
            (value as PplSerializable).asPplSerial()
        } else {
            if (value is BigDecimal) {
                value.toPlainString()
            } else {
                value.toString()
            }
        }
        return str?.length ?: 0
    }

}

@JvmOverloads
fun readTypeMapper(type: Class<*>, skip: (Field) -> Boolean= genericSkip): FieldAdapter {
    return readTypeMapper(type, extractElementType(type), skip)
}

@JvmOverloads
fun readTypeMapper(type: Class<*>, elementType: Class<*>, skip: (Field) -> Boolean= genericSkip): FieldAdapter {
    val seq = IndexSequence()
    val pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    val index = seq.next()
    val metaInfo = createMetaInfo(pplMetadata, elementType, EMPTY, index)
    val fields = getAllFields({ }::class.java)
    return JvmFieldAdapter(EMPTY, EMPTY, type, elementType, metaInfo,
            createChildren(metaInfo, EMPTY, elementType, skip, seq), index, fields[0])
}

private fun readFromField(parentPath: String, field: Field, index: Int, skip: (Field) -> Boolean, seq: IndexSequence): FieldAdapter {
    // Precedence 1: Field Annotation
    var pplMetadata: PplMetadata? = field.getAnnotation(PplMetadata::class.java)
    // Precedence 2: If null, use field Type Annotation
    val elementType = getElementType(field)
    if (pplMetadata == null) {
        pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    }
    val fieldPath = if (parentPath.isEmpty()) field.name else parentPath + PATH_SEP + field.name
    val metaInfo = createMetaInfo(pplMetadata, elementType, field.name, index)
    return JvmFieldAdapter(fieldPath, field.name, field.type, elementType, metaInfo,
            createChildren(metaInfo, fieldPath, elementType, skip, seq), index, field)
}

private fun createMetaInfo(pplMetadata: PplMetadata?, elementType: Class<*>, fieldName: String, index: Int): MetaInfo {
    val subtype = subTypeOf(elementType)
    return if (pplMetadata != null)
        MetaInfo(pplMetadata, fieldName, subtype)
    else
        MetaInfo(index, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}

private fun createChildren(parentMetaInfo: MetaInfo, parentPath: String, parentType: Class<*>, skip: (Field) -> Boolean, seq: IndexSequence): List<FieldAdapter> {
    if (isSimpleType(parentType)) {
        return listOf()
    }
    val children = mutableListOf<FieldAdapter>()
    var count = 0
    for (field in getAllFields(parentType).filterNot(skip)) {
        children.add(readFromField(parentPath, field, seq.next(), skip, seq))
    }
    // Warning: the index used on this sort must be the "metaInfo.index"
    // It has precedence over treeIndex because it comes from the PplMetadata annotation
    return children.sortedBy { it.metaInfo.index }
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