package br.net.buzu.pplimpl.jvm

import br.net.buzu.annotation.PplIgnore
import br.net.buzu.annotation.PplMetadata
import br.net.buzu.annotation.PplUse
import br.net.buzu.exception.PplReflectionException
import br.net.buzu.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.lang.EMPTY
import br.net.buzu.lang.PATH_SEP
import br.net.buzu.model.*
import br.net.buzu.pplimpl.metadata.IndexSequence
import br.net.buzu.pplimpl.metatype.AtomicJvmMetaType
import br.net.buzu.pplimpl.metatype.ComplexJvmMetaType
import br.net.buzu.pplimpl.metatype.SimpleJvmMetaType
import java.lang.reflect.Field
import java.lang.reflect.Modifier

private val EMPTY_CHILDREN: List<MetaType> = listOf()

val genericSkip: (Field) -> Boolean = { skip(it) }


@JvmOverloads
fun readMetaType(type: Class<*>, skip: (Field) -> Boolean = genericSkip): MetaType {
    return readMetaType(type, extractElementType(type), skip)
}

@JvmOverloads
fun readMetaType(type: Class<*>, elementType: Class<*>, skip: (Field) -> Boolean = genericSkip): MetaType {
    val seq = IndexSequence()
    val pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    val index = seq.next()
    val fakeField = getAllFields({ }::class.java)[0]
    val typeAdapter = JvmTypeAdapter(type, elementType, fakeField)
    val metaInfo = createMetaInfo(pplMetadata, typeAdapter.defaultSubtype, EMPTY, index)
    val valueMapper = getValueMapper(metaInfo.subtype, elementType)
    return createJvmMetaType(EMPTY, EMPTY, metaInfo,
            createChildren(EMPTY, elementType, skip, seq), index, typeAdapter, valueMapper)
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
    val typeAdapter = JvmTypeAdapter(field.type, elementType, field)
    val metaInfo = createMetaInfo(pplMetadata, typeAdapter.defaultSubtype, field.name, index)
    val valueMapper = getValueMapper(metaInfo.subtype, elementType)
    return createJvmMetaType(fullName, field.name, metaInfo,
            createChildren(fullName, elementType, skip, seq), index, typeAdapter, valueMapper)
}

private fun createMetaInfo(pplMetadata: PplMetadata?, subtype: Subtype, fieldName: String, index: Int): MetaInfo {
    return if (pplMetadata != null)
        MetaInfo(pplMetadata, fieldName, subtype)
    else
        MetaInfo(index, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}

private fun createChildren(parentPath: String, parentType: Class<*>, skip: (Field) -> Boolean, seq: IndexSequence): List<MetaType> {
    if (JvmTypeAdapter.isSimpleType(parentType)) {
        return EMPTY_CHILDREN
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

    // Precedence 3: Static or Transient
    if (Modifier.isStatic(field.modifiers) || Modifier.isTransient(field.modifiers)) {
        return true
    }

    // Precedence 4: Field Class
    var fieldClass: Class<*>
    fieldClass = try {
        getElementType(field)
    } catch (pre: PplReflectionException) {
        field.javaClass
    }

    return fieldClass.isAnnotationPresent(PplIgnore::class.java)
}

fun createJvmMetaType(fullName: String, metaName: String, metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int,
                      typeAdapter: TypeAdapter, valueMapper: ValueMapper): MetaType {
    return when {
        typeAdapter.isComplex -> ComplexJvmMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)
        metaInfo.isMultiple -> SimpleJvmMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)
        else -> AtomicJvmMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)
    }

}