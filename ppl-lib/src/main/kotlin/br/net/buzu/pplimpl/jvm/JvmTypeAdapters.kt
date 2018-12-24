package br.net.buzu.pplimpl.jvm

import br.net.buzu.java.model.MetaInfo
import br.net.buzu.java.model.Subtype
import br.net.buzu.java.model.TypeAdapter
import java.lang.IllegalArgumentException

typealias ValueParser = (text: String, metaInfo: MetaInfo) -> Any
typealias ValueSerializer = (value: Any) -> String

private val INTERNAL_ADAPTER_MAP: Map<Class<*>, JvmTypeAdapter<*>> = mapOf(
        String::class.java to StringAdapter
)

fun typeAdapterOf(elementType: Class<*>, subType: Subtype): TypeAdapter<*> {
    return if (subType == Subtype.CHAR) CharAdapter
    else
        INTERNAL_ADAPTER_MAP[elementType] ?: ComplexAdapter
}

open class JvmTypeAdapter<T>(val subtype: Subtype, val parser: ValueParser, val serializer: ValueSerializer) : TypeAdapter<T> {

    override fun getValueSize(value: T?): Int = if (value == null) 0 else valueToString(value).length

    override fun stringToValue(positionalText: String, metaInfo: MetaInfo): T? = parser(positionalText, metaInfo) as T

    override fun valueToString(value: T): String = serializer(value!!)
}

object ComplexAdapter : JvmTypeAdapter<Any>(Subtype.OBJ, charParser, charSerializer)
object CharAdapter : JvmTypeAdapter<String>(Subtype.CHAR, charParser, charSerializer)
object StringAdapter : JvmTypeAdapter<String>(Subtype.STRING, stringParser, stringSerializer)
