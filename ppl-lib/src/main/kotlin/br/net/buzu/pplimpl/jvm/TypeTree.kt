package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.jvm.fromType
import br.net.buzu.pplimpl.jvm.getElementType
import br.net.buzu.pplimpl.jvm.isMultiple
import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.exception.PplException
import br.net.buzu.pplspec.model.PplSerializable
import br.net.buzu.pplspec.model.SizeType
import br.net.buzu.pplspec.model.Subtype
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.util.HashMap


fun loadNodeOf(rootInstance: Any): LoadNode {
    val elementType = extractElementType(rootInstance.javaClass)
    val pplMetadata = elementType.javaClass.getAnnotation(PplMetadata::class.java)
    return if (isMultiple(rootInstance.javaClass)) {
        LoadNode(rootInstance, rootInstance.javaClass, elementType, "", "", pplMetadata)
    } else {
        LoadNode(rootInstance, rootInstance.javaClass, rootInstance.javaClass, "", "", pplMetadata)
    }
}

fun loadNodeOf(fieldValue: Any?, field: Field, fieldPath: String): LoadNode {
    return LoadNode(fieldValue, field.type, getElementType(field), fieldPath, field.name, field.getAnnotation(PplMetadata::class.java))
}


class LoadNode(originalValue: Any?, val fieldType: Class<*>, val elementType: Class<*>, val fieldPath: String, val fieldName: String, val pplMetadata: PplMetadata?) {

    val value: Array<Any?>
    val subtype: Subtype
    val hasChildren: Boolean

    val isEnum: Boolean
        get() = elementType.isEnum

    val occurs: Int
        get() = value.size

    init {
        subtype = fromType(elementType)
        hasChildren = subtype.dataType().isComplex && !elementType.isEnum
        if (originalValue == null) {
            this.value = arrayOf(1)
        } else if (Collection::class.java.isAssignableFrom(fieldType)) {
            if ((originalValue as Collection<*>).isEmpty()) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue.toTypedArray()
            }
        } else if (fieldType.isArray) {
            if ((originalValue as Array<Any>).size == 0) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue as Array<Any?>
            }
        } else {
            this.value = arrayOf(1)
            this.value[0] = originalValue
        }
    }



    fun calcMaxSize(): Int {
        if (subtype.dataType().sizeType() == SizeType.CUSTOM) {
            var max = 0
            var tmp = 0
            for (obj in value) {
                tmp = getValueSize(obj!!)
                if (tmp > max) {
                    max = tmp
                }
            }
            return max
        }
        return subtype.fixedSize()
    }

}


data class Max(var maxSize: Int = 0, var maxOccurs: Int = 0) {

    fun tryNewMaxOccurs(newValue: Int): Max {
        if (newValue > maxOccurs) {
            maxOccurs = newValue
        }
        return this
    }

    fun tryNewMaxSize(newValue: Int): Max {
        if (newValue > maxSize) {
            maxSize = newValue
        }
        return this
    }

}

class MaxMap {

    private val map = HashMap<String, Max>()

    fun getOrCreate(fieldPath: String): Max {
        if (!map.containsKey(fieldPath)) {
            map[fieldPath] = Max()
        }
        return map[fieldPath]!!
    }
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

internal fun getValueSize(value: Any): Int {
    if (value == null) {
        return 0
    }
    val str: String?
    if (PplSerializable::class.java.isAssignableFrom(value.javaClass)) {
        str = (value as PplSerializable).asPplSerial()
    } else {
        str = value.toString()
    }
    return str?.length ?: 0
}
