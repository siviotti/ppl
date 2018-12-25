package br.net.buzu.pplimpl.jvm

import br.net.buzu.exception.PplParseException
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.ValueMapper
import java.lang.reflect.Field

class EnumJvmMetaType(fieldPath: String, fieldName: String, fieldType: Class<*>, elementType: Class<*>,
                      metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, field: Field, valueMapper: ValueMapper)
    : JvmMetaType(fieldPath, fieldName, fieldType, elementType, metaInfo, children, treeIndex, field, valueMapper) {


    override fun parse(text: String, metadata: StaticMetadata): Any? {
        val fields = getAllFields(elementType)
        val constName = text.trim { it <= ' ' }
        for (field in fields) {
            if (field.isEnumConstant() && field.getName() == constName) {
                return field.get(null)
            }
        }
        throw PplParseException("The text '" + text + "' is missing at enum " + elementType)

    }

    override fun serialize(value: Any?, metadata: StaticMetadata): String {
        return if (value == null) serializeNull(metadata.info()) else (value as Enum<*>).name
    }


}
