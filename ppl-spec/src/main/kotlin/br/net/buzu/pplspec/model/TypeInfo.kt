package br.net.buzu.pplspec.model

import java.lang.reflect.Field
import java.math.BigDecimal

abstract class TypeInfo(val fieldName: String, val fieldType: Class<*>, val elementType: Class<*>, val metaInfo: MetaInfo,
                        val children: List<TypeInfo>, val field: Field, val treeIndex: Int) {

    val hasChildren: Boolean = children.isNotEmpty()
    val isArray: Boolean = fieldType.isArray
    val isCollection: Boolean = Collection::class.java.isAssignableFrom(fieldType)
    val multiple: Boolean

    init {
        multiple = isArray || isCollection
    }

    fun nodeCount(): Int {
        return if (children.isEmpty()) 1 else {
            var count = 1
            for (child in children) {
                count = count + child.nodeCount()
            }
            count
        }
    }

    abstract fun getValue(sourceObject: Any): Any?

    abstract fun setValue(targetObject: Any, paramValue: Any?)

    abstract fun getValueSize(value: Any?): Int

    override fun toString(): String =  "[${treeIndex}] ${fieldName}: ${fieldType.simpleName}<${elementType.simpleName}> ${metaInfo}"
}
