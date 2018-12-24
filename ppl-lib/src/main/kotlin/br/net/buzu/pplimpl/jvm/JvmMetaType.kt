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
import br.net.buzu.java.model.MetaType
import br.net.buzu.java.model.TypeAdapter
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.util.*

val genericSkip: (Field) -> Boolean = { skip(it) }

class JvmMetaType(fieldPath: String, fieldName: String, val fieldType: Class<*>, val elementType: Class<*>,
                  metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, val field: Field, adapter: TypeAdapter<*>) :
        MetaType(fieldPath, fieldName, metaInfo, treeIndex, adapter, children) {

    val isArray: Boolean = fieldType.isArray
    val isCollection: Boolean = Collection::class.java.isAssignableFrom(fieldType)
    val isComplex = metaInfo.subtype.dataType.isComplex
    val multiple: Boolean
    private val parser: ValueParser
    private val serializer: ValueSerializer
    private val valueSizer: (Any?) -> Int


    init {
        multiple = isArray || isCollection
        parser = { text, metaInfo -> text }
        serializer = getPayloadSerializer(metaInfo.subtype, Date::class.java.isAssignableFrom(elementType))
        valueSizer = { 0 }
    }

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

    override fun asSingleObject(positionalText: String): Any? = parser(positionalText, metaInfo)

    override fun asStringFromNotNull(value: Any): String = serializer(value)


    override fun maxArrayToValue(array: Array<Any?>): Any {
        return when {
            isArray -> array
            isCollection -> if (Set::class.java.isAssignableFrom(elementType)) array.toSet() else array.toList()
            else -> array[0]!!
        }
    }

    override fun valueToMaxArray(value: Any?, size: Int): Array<Any?> {
        return when {
            size == 0 -> arrayOf(0)
            value is Collection<*> -> value.toTypedArray()
            isArray -> value as Array<Any?>
            else -> arrayOf(value)
        }
    }

    override fun valueToArray(value: Any?): Array<Any?> {
        return when {
            value == null -> arrayOf(1)
            isCollection -> if ((value as Collection<*>).isEmpty()) {
                arrayOf()
            } else {
                value.toTypedArray()
            }
            isArray -> if ((value as Array<Any>).size == 0) {
                arrayOf()
            } else {
                value as Array<Any?>
            }
            else -> arrayOf(value)
        }
    }

    override fun createAndFillArray(size: Int): Array<Any?> {
        return if (isComplex) Array(size) { newInstance(elementType) } else arrayOf()
    }



    override fun toString(): String = "[$treeIndex] $fieldFullName: ${fieldType.simpleName}<${elementType.simpleName}> ($metaName) $metaInfo"
}

@JvmOverloads
fun readMetaType(type: Class<*>, skip: (Field) -> Boolean = genericSkip): MetaType {
    return readMetaType(type, extractElementType(type), skip)
}

@JvmOverloads
fun readMetaType(type: Class<*>, elementType: Class<*>, skip: (Field) -> Boolean = genericSkip): MetaType {
    val seq = IndexSequence()
    val pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    val index = seq.next()
    val metaInfo = createMetaInfo(pplMetadata, elementType, EMPTY, index)
    val typeAdapter = typeAdapterOf(elementType, metaInfo.subtype)
    val fields = getAllFields({ }::class.java)
    return JvmMetaType(EMPTY, EMPTY, type, elementType, metaInfo,
            createChildren(metaInfo, EMPTY, elementType, skip, seq), index, fields[0], typeAdapter)
}

private fun readFromField(parentFullName: String, field: Field, index: Int, skip: (Field) -> Boolean, seq: IndexSequence): MetaType {
    // Precedence 1: Field Annotation
    var pplMetadata: PplMetadata? = field.getAnnotation(PplMetadata::class.java)
    // Precedence 2: If null, use field Type Annotation
    val elementType = getElementType(field)
    if (pplMetadata == null) {
        pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    }
    val fullName = if (parentFullName.isEmpty()) field.name else parentFullName + PATH_SEP + field.name
    val metaInfo = createMetaInfo(pplMetadata, elementType, field.name, index)
    val typeAdapter = typeAdapterOf(elementType, metaInfo.subtype)
    return JvmMetaType(fullName, field.name, field.type, elementType, metaInfo,
            createChildren(metaInfo, fullName, elementType, skip, seq), index, field, typeAdapter)
}

private fun createMetaInfo(pplMetadata: PplMetadata?, elementType: Class<*>, fieldName: String, index: Int): MetaInfo {
    val subtype = subTypeOf(elementType)
    return if (pplMetadata != null)
        MetaInfo(pplMetadata, fieldName, subtype)
    else
        MetaInfo(index, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}

private fun createChildren(parentMetaInfo: MetaInfo, parentPath: String, parentType: Class<*>, skip: (Field) -> Boolean, seq: IndexSequence): List<MetaType> {
    if (isSimpleType(parentType)) {
        return listOf()
    }
    val children = mutableListOf<MetaType>()
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
