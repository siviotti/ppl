package br.net.buzu.pplimpl.jvm

import br.net.buzu.exception.PplParseException
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.Subtype
import br.net.buzu.model.TypeAdapter
import java.lang.IllegalArgumentException
import java.time.LocalDate

typealias ValueParser = (text: String, metaInfo: MetaInfo) -> Any
typealias ValueSerializer = (value: Any) -> String

private val INTERNAL_ADAPTER_MAP: Map<Class<*>, TypeAdapter> = mapOf(
        String::class.java to StringAdapter,
        LocalDate::class.java to DateAdapter,
        Double::class.java to NumberAdapter,
        Boolean::class.java to BooleanAdapter
)

fun typeAdapterOf(elementType: Class<*>, subType: Subtype): TypeAdapter {


    return if (subType == Subtype.CHAR) CharAdapter
    else
        INTERNAL_ADAPTER_MAP[elementType] ?: ComplexAdapter
}

open class JvmTypeAdapter(val subtype: Subtype, val parser: ValueParser, val serializer: ValueSerializer) : TypeAdapter {

    override fun getValueSize(value: Any?): Int = if (value == null) 0 else valueToString(value).length

    override fun stringToValue(positionalText: String, metaInfo: MetaInfo): Any? = parser(positionalText, metaInfo)

    override fun valueToString(value: Any): String = serializer(value!!)
}

object ComplexAdapter : JvmTypeAdapter(Subtype.OBJ, charParser, charSerializer)
object CharAdapter : JvmTypeAdapter(Subtype.CHAR, charParser, charSerializer)
object StringAdapter : JvmTypeAdapter(Subtype.STRING, stringParser, stringSerializer)
object DateAdapter : JvmTypeAdapter(Subtype.DATE, dateParser, dateSerializer)
object NumberAdapter : JvmTypeAdapter(Subtype.DATE, numberParser, numberSerializer)
object BooleanAdapter : JvmTypeAdapter(Subtype.DATE, booleanParser, booleanSerializer)