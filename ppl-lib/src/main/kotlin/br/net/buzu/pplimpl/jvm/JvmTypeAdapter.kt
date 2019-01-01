package br.net.buzu.pplimpl.jvm

import br.net.buzu.exception.PplParseException
import br.net.buzu.model.TypeAdapter
import java.lang.reflect.Field

class JvmTypeAdapter(val fieldType: Class<*>, val elementType: Class<*>, private val field: Field, private val isComplex: Boolean) : TypeAdapter {

    private val isArray: Boolean = fieldType.isArray
    private val isCollection: Boolean = Collection::class.java.isAssignableFrom(fieldType)

    override fun getFieldValue(parentObject: Any): Any? = getValue(field, parentObject)

    override fun setFieldValue(parentObject: Any, paramValue: Any?) = setValue(field, parentObject, paramValue)

    override fun maxArrayToValue(array: Array<Any?>): Any? {
        return when {
            isArray -> array
            isCollection -> if (Set::class.java.isAssignableFrom(elementType)) array.toSet() else array.toList()
            else -> array[0]
        }
    }

    override fun valueToMaxArray(value: Any?, size: Int): Array<Any?> {
        return when {
            value is Collection<*> -> value.toTypedArray()
            isArray -> value as Array<Any?>
            else -> arrayOf(value)
        }
    }

    override fun createAndFillArray(size: Int): Array<Any?> {
        return if (isComplex) Array(size) { newInstance(elementType) } else Array(size) {}
    }

    override fun valueToArray(value: Any?): Array<Any?> {
        return when {
            value == null -> arrayOf()
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

    override fun toString(): String = if (fieldType != elementType) "${fieldType.simpleName}<${elementType.simpleName}>"
    else "${elementType.simpleName}"

    override fun enumConstantToValue(constName: String): Any {
        val fields = getAllFields(elementType)
        val constName = constName.trim { it <= ' ' }
        for (field in fields) {
            if (field.isEnumConstant() && field.getName() == constName) {
                return field.get(null)
            }
        }
        throw PplParseException("The text '" + constName + "' is missing at enum " + elementType)
    }

}