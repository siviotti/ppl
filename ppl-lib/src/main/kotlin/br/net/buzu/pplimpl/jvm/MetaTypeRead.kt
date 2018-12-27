package br.net.buzu.pplimpl.jvm

import br.net.buzu.annotation.PplIgnore
import br.net.buzu.annotation.PplMetadata
import br.net.buzu.annotation.PplUse
import br.net.buzu.exception.PplReflectionException
import br.net.buzu.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.lang.EMPTY
import br.net.buzu.lang.PATH_SEP
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.ValueMapper
import br.net.buzu.pplimpl.metadata.IndexSequence
import java.lang.reflect.Field
import java.lang.reflect.Modifier

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
    val metaInfo = createMetaInfo(pplMetadata, elementType, EMPTY, index)
    val valueMapper = getValueMapper(metaInfo.subtype, elementType)
    val fakeField = getAllFields({ }::class.java)[0]
    return createJvmMetaType(EMPTY, EMPTY, type, elementType, metaInfo,
            createChildren(metaInfo, EMPTY, elementType, skip, seq), index, fakeField, valueMapper)
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
    val valueMapper = getValueMapper(metaInfo.subtype, elementType)
    return createJvmMetaType(fullName, field.name, field.type, elementType, metaInfo,
            createChildren(metaInfo, fullName, elementType, skip, seq), index, field, valueMapper)
}

private fun createMetaInfo(pplMetadata: PplMetadata?, elementType: Class<*>, fieldName: String, index: Int): MetaInfo {
    val subtype = defaultSubTypeOf(elementType)
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

fun createJvmMetaType(fullname: String, metaName: String, fieldType: Class<*>, elementType: Class<*>,
                      metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, field: Field, adapter: ValueMapper): MetaType {
    return when {
        elementType.isEnum -> EnumJvmMetaType(fullname, metaName, fieldType, elementType, metaInfo, children, treeIndex, field, adapter)
        metaInfo.subtype.dataType.isComplex -> ComplexJvmMetaType(fullname, metaName, fieldType, elementType, metaInfo, children, treeIndex, field, adapter)
        metaInfo.isMultiple -> MultipleJvmMetaType(fullname, metaName, fieldType, elementType, metaInfo, children, treeIndex, field, adapter)
        else -> AtomicJvmMetaType(fullname, metaName, fieldType, elementType, metaInfo, children, treeIndex, field, adapter)
    }

}