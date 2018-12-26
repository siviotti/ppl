package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Subtype
import br.net.buzu.model.ValueMapper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.naming.OperationNotSupportedException

typealias ValueParser = (text: String, metaInfo: MetaInfo) -> Any
typealias ValueSerializer = (value: Any) -> String

private val INTERNAL_ADAPTER_MAP: Map<Class<*>, ValueMapper> = mapOf(
        String::class.java to StringMapper,
        LocalDate::class.java to DateMapper,
        Double::class.java to DoubleMapper,
        Boolean::class.java to BooleanMapper
)

fun getValueMapper(elementType: Class<*>, subType: Subtype): ValueMapper {


    return if (subType == Subtype.CHAR) CharMapper
    else
        INTERNAL_ADAPTER_MAP[elementType] ?: ComplexMapper
}

abstract class JvmValueMapper(val type: Class<*>, val subType: Subtype) : ValueMapper {
    override fun getValueSize(value: Any?): Int = if (value == null) 0 else toText(value).length
}

object ComplexMapper : JvmValueMapper(Any::class.java, Subtype.OBJ) {
    override fun toValue(positionalText: String, metaInfo: MetaInfo): Any? = throw OperationNotSupportedException()
    override fun toText(value: Any): String = throw OperationNotSupportedException()
}

object CharMapper : JvmValueMapper(String::class.java, Subtype.CHAR) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text
    override fun toText(value: Any): String = value.toString()
}

object StringMapper : JvmValueMapper(String::class.java, Subtype.STRING) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.trim { fillChar -> fillChar == metaInfo.fillChar }
    override fun toText(value: Any): String = value.toString()
}

object DoubleMapper : JvmValueMapper(Double::class.java, Subtype.NUMBER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.trim { fillChar -> fillChar == metaInfo.fillChar }
    override fun toText(value: Any): String = value.toString()
}

object IntegerMapper : JvmValueMapper(Int::class.java, Subtype.NUMBER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toInt()
    override fun toText(value: Any): String = value.toString()
}

object LongMapper : JvmValueMapper(Long::class.java, Subtype.NUMBER) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toLong()
    override fun toText(value: Any): String = value.toString()
}


object DateMapper : JvmValueMapper(LocalDate::class.java, Subtype.DATE) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = LocalDate.parse(text, DateTimeFormatter.BASIC_ISO_DATE)
    override fun toText(value: Any): String = (value as LocalDate).format(DateTimeFormatter.BASIC_ISO_DATE)
}

object BooleanMapper : JvmValueMapper(Boolean::class.java, Subtype.BOOLEAN) {
    override fun toValue(text: String, metaInfo: MetaInfo): Any? = text.toBoolean()
    override fun toText(value: Any): String = value.toString()
}