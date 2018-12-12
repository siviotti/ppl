package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.metadata.CreateMetadata
import br.net.buzu.pplspec.annotation.PplIgnore
import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.annotation.PplTypeMapper
import br.net.buzu.pplspec.annotation.PplUse
import br.net.buzu.pplspec.api.TypeMapper
import br.net.buzu.pplspec.exception.PplException
import br.net.buzu.pplspec.exception.PplReflectionException
import br.net.buzu.pplspec.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.pplspec.lang.EMPTY
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.util.*
import kotlin.reflect.KClass

val genericSkip: (Field) -> Boolean = { skip(it) }

fun readMetadata(type: Class<*>, createMetadata: CreateMetadata, skip: (Field) -> Boolean= genericSkip): Metadata {
    return readMetadata(type, extractElementType(type), createMetadata, skip)
}

fun readMetadata(type: Class<*>, elementType: Class<*>, createMetadata: CreateMetadata, skip: (Field) -> Boolean= genericSkip): Metadata {
    val pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    val pplTypeMapper = elementType.getAnnotation(PplTypeMapper::class.java)
    val parserType = pplTypeMapper?.value
    return callCreateMetadata(createMetadata, skip, EMPTY, type, elementType, pplMetadata, null, parserType)

}

private fun read(parentId: String, field: Field, createMetadata: CreateMetadata, skip: (Field) -> Boolean): Metadata {
    // Precedence 1: Field Annotation
    var pplMetadata: PplMetadata? = field.getAnnotation(PplMetadata::class.java)
    var pplTypeMapper: PplTypeMapper? = field.getAnnotation(PplTypeMapper::class.java)
    // Precedence 2: If null, use field Type Annotation
    val elementType = getElementType(field)
    if (pplMetadata == null) {
        pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    }
    if (pplTypeMapper == null) {
        pplTypeMapper = elementType.getAnnotation(PplTypeMapper::class.java)
    }
    return callCreateMetadata(createMetadata, skip, parentId, field.type, elementType, pplMetadata, field,
            pplTypeMapper?.value)
}

private fun callCreateMetadata(createMetadata: CreateMetadata, skip: (Field) -> kotlin.Boolean, parentId: String, fieldType: Class<*>, elementType: Class<*>,
                               pplMetadata: PplMetadata?, field: Field?, parserType: KClass<out TypeMapper>?): Metadata {
    val multiple = isMultiple(fieldType)
    val complex = !isSimple(elementType)
    val metaInfo = createMetaInfo(parentId, pplMetadata, elementType, if (field != null) field.name else "")
    val children = createChildren(parentId, elementType, createMetadata, skip)

    return createMetadata(metaInfo, children)
}

private fun createMetaInfo(parentId: String, pplMetadata: PplMetadata?, elementType: Class<*>, fieldName: String): MetaInfo {
    val subtype = fromType(elementType)
    return if (pplMetadata != null)
        MetaInfo(parentId, pplMetadata, fieldName, subtype)
    else
        MetaInfo(parentId, 0, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}


private fun createChildren(parentId: String, parentType: Class<*>, createMetadata: CreateMetadata, skip: (Field) -> Boolean): List<Metadata> {
    if (isSimple(parentType)) {
        return listOf()
    }
    val list = ArrayList<Metadata>()
    for (field in getAllFields(parentType)) {
        if (skip(field)) {
            continue
        }
        list.add(read(parentId, field, createMetadata, skip))
    }
    return list.sortedBy { m -> m.info().index }
}


private fun extractElementType(fieldType: Class<*>): Class<*> {
    if (Collection::class.java.isAssignableFrom(fieldType)) {
        if (fieldType.genericSuperclass !is ParameterizedType) {
            return Any::class.java
        }
        val parType = fieldType.genericSuperclass as ParameterizedType
        if (parType.actualTypeArguments.size < 1) {
            return Any::class.java
        }
        val itemType = parType.actualTypeArguments[0]
        try {
            return Class.forName(itemType.typeName)
        } catch (e: ClassNotFoundException) {
            throw PplException(e)
        }
    } else return if (fieldType.isArray) {
        fieldType.componentType
    } else {
        fieldType
    }
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
