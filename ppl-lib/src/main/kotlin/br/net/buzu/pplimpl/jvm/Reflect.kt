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

import br.net.buzu.annotation.PplMetadata
import br.net.buzu.exception.PplException
import br.net.buzu.exception.PplReflectionException
import java.io.*
import java.lang.Exception
import java.lang.reflect.*
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

internal const val UNSAFE_COLLECTION = "Unsafe collection '"


// **************************************************
// Multiple
// **************************************************

fun isMultiple(type: Class<*>): Boolean {
    return type.isArray || Collection::class.java.isAssignableFrom(type)
}

// **************************************************
// Method Invoke
// **************************************************

fun findGet(attributeName: String, obj: Any): Method {
    val methodGet = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1)
    try {
        return findMethod(methodGet, obj)
    } catch (e: NoSuchMethodException) {
        val methodIs = "is" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1)
        try {
            return findMethod(methodIs, obj)
        } catch (e1: NoSuchMethodException) {
            throw PplReflectionException("$methodGet()/$methodIs()", obj.javaClass, e)
        }
    }
}

fun findSet(attributeName: String, obj: Any, paramType: Class<*>): Method {
    val methodName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1)
    try {
        return findMethod(methodName, obj, paramType)
    } catch (e: NoSuchMethodException) {
        throw PplReflectionException(methodName + "(" + paramType.simpleName + ")", obj.javaClass, e)
    }
}

@Throws(NoSuchMethodException::class)
internal fun findMethod(methodName: String, obj: Any, vararg params: Class<*>): Method {
    try {
        return obj.javaClass.getMethod(methodName, *params)
    } catch (e: SecurityException) {
        throw PplReflectionException(e)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> findAndInvokeGet(instance: Any, fieldName: String): T {
    try {
        return findGet(fieldName, instance).invoke(instance) as T
    } catch (e: IllegalAccessException) {
        throw PplReflectionException(e)
    } catch (e: IllegalArgumentException) {
        throw PplReflectionException(e)
    } catch (e: InvocationTargetException) {
        throw PplReflectionException(e)
    }
}

fun findAndInvokeSet(instance: Any, fieldName: String, fieldType: Class<*>, param: Any?) {
    try {
        findSet(fieldName, instance, fieldType).invoke(instance, param)
    } catch (e: IllegalAccessException) {
        throw PplReflectionException(e)
    } catch (e: InvocationTargetException) {
        throw PplReflectionException(e)
    } catch (e: IllegalArgumentException) {
        throw PplReflectionException(
                "Bad param: class:" + instance.javaClass.simpleName + " field:" + fieldName + "("
                        + fieldType.simpleName + ") param(" + param!!.javaClass.simpleName + "):" + param,
                e)
    }
}

// **************************************************
// Fields
// **************************************************

/**
 * Returns the internal elementType domainOf a Collection or Array if is multiple. If not
 * returns the field elementType.
 *
 * @param field
 * The field
 * @return The single elementType relative to the field.
 */
fun getElementType(field: Field): Class<*> {
    val fieldType = field.type
    if (Collection::class.java.isAssignableFrom(fieldType)) {
        if (field.genericType is ParameterizedType) {
            val parType = field.genericType as ParameterizedType
            if (parType.actualTypeArguments.isNotEmpty()) {
                val itemType = parType.actualTypeArguments[0]
                return itemType as Class<*>
            }
        }
        throw PplReflectionException(UNSAFE_COLLECTION + field.name + "'. Use a generic elementType.")
    } else if (fieldType.isArray) {
        return fieldType.componentType
    }
    return fieldType
}

/**
 * Returna all fields presents in a class including the superclasses fields.
 *
 * @param type
 * The class
 * @return The field list.
 */
fun getAllFields(type: Class<*>): List<Field> {
    if (type.superclass == Any::class.java || type.superclass == JvmType.Object::class.java || type.superclass == null) {
        return Arrays.asList(*type.declaredFields)
    }
    val fields = ArrayList<Field>()
    fields.addAll(getAllFields(type.superclass))
    fields.addAll(Arrays.asList(*type.declaredFields))
    return fields
}

fun getPplMetadata(field: Field): PplMetadata? {
    if (field.isAnnotationPresent(PplMetadata::class.java)) {
        return field.getAnnotation(PplMetadata::class.java)
    }
    val elementType = getElementType(field)
    return if (elementType.isAnnotationPresent(PplMetadata::class.java)) {
        elementType.getAnnotation(PplMetadata::class.java)
    } else null
}

fun getValue(field: Field, instance: Any): Any? {
    return try {
        field.isAccessible = true
        field.get(instance)
    } catch (e: IllegalArgumentException) {
        throw PplReflectionException("Illegal argument for field '${field.name}' Instance:$instance", e)
    } catch (e: IllegalAccessException) {
        try {
            findAndInvokeGet(instance, field.name)
        } catch (e: Exception) {
            throw PplReflectionException("Field ${field.name} cannot be accessed", e)
        }

    }

}

fun setValue(field: Field, instance: Any, param: Any?) {
    try {
        field.isAccessible = true
        field.set(instance, param)
    } catch (e: IllegalArgumentException) {
        throw PplReflectionException("Illegal argument for field '${field.name}' param:$param \n Instance:$instance", e)
    } catch (e: IllegalAccessException) {
        try {
            findAndInvokeSet(instance, field.name, field.type, param)
        } catch (e: Exception) {
            throw PplReflectionException("Field ${field.name} cannot be accessed", e)
        }
    }
}


// **************************************************
// NewInstance
// **************************************************

/**
 * Creates a new instance from a elementType.
 * <P>
 * Constructor Strategy Precedence:<BR></BR>
 * 1 - Try the default constructor <BR></BR>
 * 2 - If the elementType is Serializable try unserialization<BR></BR>
 * 3 - Use the constructor with less parameters
 *
 * @param type
 * The elementType
 * @return The new instance
</P> */
fun newInstance(type: Class<*>): Any {
    try {
        if (type.isInterface) {
            return newInstanceFromInterface(type)
        }
        val c = getMostSimpleConstructor(type)
        if (!Modifier.isPublic(c.modifiers)) {
            c.isAccessible = true
        }
        if (c.parameterCount == 0) {
            return c.newInstance()
        }
        return if (Serializable::class.java.isAssignableFrom(type)) {
            instantiateUsingSerialization(type)
        } else {
            c.newInstance(*getInitialParams(c))
        }
    } catch (e: InstantiationException) {
        throw PplReflectionException(e.message!!, e)
    } catch (e: IllegalAccessException) {
        throw PplReflectionException(e.message!!, e)
    } catch (e: IllegalArgumentException) {
        throw PplReflectionException(e.message!!, e)
    } catch (e: InvocationTargetException) {
        throw PplReflectionException(e.message!!, e)
    }
}


internal fun newInstanceFromInterface(type: Class<*>): Any {
    if (List::class.java == type) {
        return listOf<Any>()
    } else if (Set::class.java == type || Collection::class.java == type) {
        return setOf<Any>()
    } else if (Map::class.java == type) {
        return mapOf<String, Any>()
    }
    throw PplReflectionException("Is not possible create a new instance from the interface $type")
}

internal fun getInitialParams(c: Constructor<*>): Array<Any?> {
    val params = arrayOfNulls<Any>(c.parameterCount)
    for (i in params.indices) {
        if (c.parameterTypes[i].isPrimitive) {
            params[i] = c.parameterTypes[i].kotlin.objectInstance
        } else {
            if (c.parameterTypes[i].isArray) {
                val arrayType = c.parameterTypes[i].componentType
                params[i] = java.lang.reflect.Array.newInstance(arrayType, 0)
            } else {
                params[i] = c.parameterTypes[i].newInstance()
            }
        }
    }
    return params
}

// From XStream
internal fun instantiateUsingSerialization(type: Class<*>): Any {
    try {
        val data: ByteArray
        val bytes = ByteArrayOutputStream()
        val stream = DataOutputStream(bytes)
        stream.writeShort(ObjectStreamConstants.STREAM_MAGIC.toInt())
        stream.writeShort(ObjectStreamConstants.STREAM_VERSION.toInt())
        stream.writeByte(ObjectStreamConstants.TC_OBJECT.toInt())
        stream.writeByte(ObjectStreamConstants.TC_CLASSDESC.toInt())
        stream.writeUTF(type.name)
        stream.writeLong(ObjectStreamClass.lookup(type).serialVersionUID)
        stream.writeByte(2) // classDescFlags (2 = Serializable)
        stream.writeShort(0) // field size
        stream.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA.toInt())
        stream.writeByte(ObjectStreamConstants.TC_NULL.toInt())
        data = bytes.toByteArray()

        val input = ObjectInputStream(ByteArrayInputStream(data))
        return input.readObject()
    } catch (e: IOException) {
        throw PplReflectionException("Cannot create " + type.name + " by JDK serialization", e)
    } catch (e: ClassNotFoundException) {
        throw PplReflectionException("Cannot find class " + e.message)
    }
}

internal fun getMostSimpleConstructor(toClass: Class<*>): Constructor<*> {
    try {
        var c = toClass.declaredConstructors[0]
        for (tmp in toClass.declaredConstructors) {
            if (tmp.parameterCount == 0) {
                return tmp
            }
            if (tmp.parameterCount < c.parameterCount) {
                c = tmp
            }
        }
        return c
    } catch (e: SecurityException) {
        throw PplReflectionException(e.message!!, e)
    }
}

// **************************************************
// Etc
// **************************************************

internal fun extractElementType(fieldType: Class<*>): Class<*> {
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

