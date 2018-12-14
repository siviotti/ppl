package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.annotation.PplParser
import br.net.buzu.pplspec.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.pplspec.lang.PATH_SEP
import br.net.buzu.pplspec.model.MetaInfo
import java.lang.reflect.Field

data class TypeInfo(val fieldName: String, val fieldType: Class<*>, val elementType: Class<*>, val metaInfo: MetaInfo,
                    val children: List<TypeInfo>, val field: Field) {

    val multiple: Boolean
    val hasChildren: Boolean
    val isArray: Boolean
    val isCollection: Boolean

    init {
        hasChildren = children.isNotEmpty()
        isArray = fieldType.isArray
        isCollection = Collection::class.java.isAssignableFrom(fieldType)
        multiple = isArray || isCollection
    }

    fun toTree(level: Int): String {
        val sb = StringBuilder()
        for (i in 0 until level) {
            sb.append(PATH_SEP)
        }
        sb.append("$fieldName : $fieldType<$elementType> ($metaInfo)").append("\n")
        if (children.isNotEmpty()) {
            for (child in children) {
                sb.append(child.toTree(level + 1))
            }
        }
        return sb.toString()
    }

    fun getValue(instance: Any): Any? {
        try {
            field.setAccessible(true)
            return field.get(instance)
        } catch (e: IllegalArgumentException) {
            return findAndInvokeGet(instance, field.name)
        } catch (e: IllegalAccessException) {
            return findAndInvokeGet(instance, field.name)
        }
    }

    fun setValue(instance: Any, param: Any?) {
        try {
            field.setAccessible(true)
            field.set(instance, param)
        } catch (e: IllegalArgumentException) {
            findAndInvokeSet(instance, field.name, field.type, param)
        } catch (e: IllegalAccessException) {
            findAndInvokeSet(instance, field.name, field.type, param)
        }
    }


}

fun read(type: Class<*>, skip: (Field) -> Boolean): TypeInfo {
    return read(type, extractElementType(type), skip)
}

fun read(type: Class<*>, elementType: Class<*>, skip: (Field) -> Boolean): TypeInfo {
    val pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    val pplParser = elementType.getAnnotation(PplParser::class.java)
    val parserType = pplParser?.value
    val metaInfo = createMetaInfo("", pplMetadata, elementType, "", 0)
    val fields = getAllFields({ var field: String? = null }::class.java)
    return TypeInfo("", type, elementType, metaInfo, createChildren("", elementType, 0, skip), fields[0])
}

private fun readFromField(parentId: String, field: Field, index: Int, skip: (Field) -> Boolean): TypeInfo {
    // Precedence 1: Field Annotation
    var pplMetadata: PplMetadata? = field.getAnnotation(PplMetadata::class.java)
    var pplParser: PplParser? = field.getAnnotation(PplParser::class.java)
    // Precedence 2: If null, use field Type Annotation
    val elementType = getElementType(field)
    if (pplMetadata == null) {
        pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    }
    if (pplParser == null) {
        pplParser = elementType.getAnnotation(PplParser::class.java)
    }
    val metaInfo = createMetaInfo(parentId, pplMetadata, elementType, field.name, index)
    return TypeInfo(field.name, field.type, elementType, metaInfo, createChildren(parentId, elementType, index, skip), field)
}

private fun createMetaInfo(parentId: String, pplMetadata: PplMetadata?, elementType: Class<*>, fieldName: String, index: Int): MetaInfo {
    val subtype = fromType(elementType)
    return if (pplMetadata != null)
        MetaInfo(parentId, pplMetadata, fieldName, subtype)
    else
        MetaInfo(parentId, index, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}

private fun createChildren(parentId: String, parentType: Class<*>, index: Int, skip: (Field) -> Boolean): List<TypeInfo> {
    if (isSimple(parentType)) {
        return listOf()
    }
    val children = mutableListOf<TypeInfo>()
    var count = 0
    for (field in getAllFields(parentType).filterNot(skip)) {
        children.add(readFromField(parentId, field, count++, skip))
    }
    return children.sortedBy { it.metaInfo.index }
}
