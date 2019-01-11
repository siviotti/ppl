package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.metadata.IndexSequence
import br.net.buzu.pplimpl.metatype.GenericMetaTypeFactory
import br.net.buzu.pplspec.annotation.PplIgnore
import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.annotation.PplUse
import br.net.buzu.pplspec.exception.PplReflectionException
import br.net.buzu.pplspec.ext.MetaTypeFactory
import br.net.buzu.pplspec.ext.SubtypeResolver
import br.net.buzu.pplspec.ext.ValueMapperKit
import br.net.buzu.pplspec.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.pplspec.lang.EMPTY
import br.net.buzu.pplspec.lang.PATH_SEP
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.MetaType
import br.net.buzu.pplspec.model.Subtype
import java.lang.reflect.Field
import java.lang.reflect.Modifier

private val EMPTY_CHILDREN: List<MetaType> = listOf()

val genericSkip: (Field) -> Boolean = { skip(it) }


@JvmOverloads
fun readMetaType(type: Class<*>, factory: MetaTypeFactory = GenericMetaTypeFactory, resolver: SubtypeResolver = JvmSubtypeResolver,
                 kit: ValueMapperKit = JvmValueMapperKit, skip: (Field) -> Boolean = genericSkip): MetaType {
    return readMetaType(type, extractElementType(type), factory, resolver, kit, skip)
}

@JvmOverloads
fun readMetaType(type: Class<*>, elementType: Class<*>, factory: MetaTypeFactory = GenericMetaTypeFactory,
                 resolver: SubtypeResolver = JvmSubtypeResolver, kit: ValueMapperKit = JvmValueMapperKit,
                 skip: (Field) -> Boolean = genericSkip): MetaType {
    val seq = IndexSequence()
    val pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    val index = seq.next()
    val subtype = resolver.resolve(elementType)
    val fakeField = getAllFields({ }::class.java)[0]
    val typeAdapter = JvmTypeAdapter(type, elementType, fakeField, subtype)
    val metaInfo = createMetaInfo(pplMetadata, subtype, EMPTY, index)
    val valueMapper = typeAdapter.getValueMapper(metaInfo, kit)
    val children = if (subtype.dataType.isComplex)
        createChildren(EMPTY, elementType, factory, resolver, kit, skip, seq) else EMPTY_CHILDREN
    return factory.create(EMPTY, metaInfo, children, index, typeAdapter, valueMapper, kit)
}

private fun readFromField(parentFullName: String, field: Field, index: Int, factory: MetaTypeFactory, resolver: SubtypeResolver,
                          kit: ValueMapperKit, skip: (Field) -> Boolean, seq: IndexSequence): MetaType {
    val elementType = getElementType(field)
    val pplMetadata = getPplMetadata(field)
    val fullName = if (parentFullName.isEmpty()) field.name else parentFullName + PATH_SEP + field.name
    val subtype = resolver.resolve(elementType)
    val typeAdapter = JvmTypeAdapter(field.type, elementType, field, subtype)
    val metaInfo = createMetaInfo(pplMetadata, subtype, field.name, index)
    val valueMapper = typeAdapter.getValueMapper(metaInfo, kit)
    val children = if (subtype.dataType.isComplex)
        createChildren(fullName, elementType, factory, resolver, kit, skip, seq) else EMPTY_CHILDREN
    return factory.create(fullName, metaInfo, children, index, typeAdapter, valueMapper, kit)
}

private fun createMetaInfo(pplMetadata: PplMetadata?, subtype: Subtype, fieldName: String, index: Int): MetaInfo {
    return if (pplMetadata != null)
        MetaInfo(pplMetadata, fieldName, subtype)
    else
        MetaInfo(index, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}

private fun createChildren(parentPath: String, parentType: Class<*>, factory: MetaTypeFactory, resolver: SubtypeResolver, kit: ValueMapperKit,
                           skip: (Field) -> Boolean, seq: IndexSequence): List<MetaType> {
    val children = mutableListOf<MetaType>()
    for (field in getAllFields(parentType).filterNot(skip)) {
        children.add(readFromField(parentPath, field, seq.next(),factory, resolver, kit, skip, seq))
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
    val fieldClass: Class<*> = try {
        getElementType(field)
    } catch (pre: PplReflectionException) {
        field.javaClass
    }

    return fieldClass.isAnnotationPresent(PplIgnore::class.java)
}