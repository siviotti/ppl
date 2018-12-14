package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.metadata.IndexSequence
import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.annotation.PplParser
import br.net.buzu.pplspec.lang.DEFAULT_MIN_OCCURS
import br.net.buzu.pplspec.lang.EMPTY
import br.net.buzu.pplspec.lang.PATH_SEP
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.PplSerializable
import java.lang.reflect.Field
import java.math.BigDecimal


data class TypeInfo(val fieldName: String, val fieldType: Class<*>, val elementType: Class<*>, val metaInfo: MetaInfo,
                    val children: List<TypeInfo>, val field: Field, val treeIndex: Int) {

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
        sb.append("[$treeIndex] $metaInfo : ${fieldType.simpleName} <${elementType.simpleName}> ").append("\n")
        if (children.isNotEmpty()) {
            for (child in children) {
                sb.append(child.toTree(level + 1))
            }
        }
        return sb.toString()
    }

    fun nodeCount(): Int {
        if (children.isEmpty()) return 1 else {
            var count = 1
            for (child in children){
                count = count + child.nodeCount()
            }
            return count
        }
    }

    fun getValue(sourceObject: Any): Any? {
        try {
            field.setAccessible(true)
            return field.get(sourceObject)
        } catch (e: IllegalArgumentException) {
            return findAndInvokeGet(sourceObject, field.name)
        } catch (e: IllegalAccessException) {
            return findAndInvokeGet(sourceObject, field.name)
        }
    }

    fun setValue(targetObject: Any, paramValue: Any?) {
        try {
            field.setAccessible(true)
            field.set(targetObject, paramValue)
        } catch (e: IllegalArgumentException) {
            findAndInvokeSet(targetObject, field.name, field.type, paramValue)
        } catch (e: IllegalAccessException) {
            findAndInvokeSet(targetObject, field.name, field.type, paramValue)
        }
    }

    fun getValueSize(value: Any?): Int{
        if (value == null) {
            return 0
        }
        val str: String?
        if (PplSerializable::class.java.isAssignableFrom(value.javaClass)) {
            str = (value as PplSerializable).asPplSerial()
        } else {
            if (value is BigDecimal){
                str = value.toPlainString()
            } else {
                str = value.toString()
            }
        }
        return str?.length ?: 0
    }


}

fun read(type: Class<*>, skip: (Field) -> Boolean): TypeInfo {
    return read(type, extractElementType(type), skip)
}

fun read(type: Class<*>, elementType: Class<*>, skip: (Field) -> Boolean): TypeInfo {
    val seq = IndexSequence()
    val pplMetadata = elementType.getAnnotation(PplMetadata::class.java)
    val pplParser = elementType.getAnnotation(PplParser::class.java)
    val parserType = pplParser?.value
    val index = seq.next()
    val metaInfo = createMetaInfo(EMPTY, pplMetadata, elementType, EMPTY, index)
    val fields = getAllFields({ var field: String? = null }::class.java)
    return TypeInfo("", type, elementType, metaInfo,
            createChildren(metaInfo, EMPTY, elementType, skip, seq), fields[0],index)
}

private fun readFromField(parentId: String, field: Field, index: Int, skip: (Field) -> Boolean, seq: IndexSequence): TypeInfo {
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
    val children = createChildren(metaInfo, parentId, elementType, skip, seq)
    return TypeInfo(field.name, field.type, elementType, metaInfo,children , field, index)
}

private fun createMetaInfo(parentId: String, pplMetadata: PplMetadata?, elementType: Class<*>, fieldName: String, index: Int): MetaInfo {
    val subtype = subTypeOf(elementType)
    return if (pplMetadata != null)
        MetaInfo(parentId, pplMetadata, fieldName, subtype)
    else
        MetaInfo(parentId, index, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
                DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER)
}

private fun createChildren(parentMetaInfo:MetaInfo, parentId: String, parentType: Class<*>, skip: (Field) -> Boolean, seq: IndexSequence): List<TypeInfo> {
    if (isSimpleType(parentType)) {
        return listOf()
    }
    val children = mutableListOf<TypeInfo>()
    var count = 0
    for (field in getAllFields(parentType).filterNot(skip)) {
        children.add(readFromField(parentId, field, seq.next(), skip, seq))
    }
    // Warning: the index used on this sort must be the "metaInfo.index"
    // It has precedence over treeIndex because it comes from the PplMetadata annotation
    return children.sortedBy { it.metaInfo.index }
}

