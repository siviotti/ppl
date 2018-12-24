package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.TypeAdapter
import java.lang.reflect.Field

class ArrayJvmMetaType (fieldPath: String, fieldName: String, fieldType: Class<*>, elementType: Class<*>,
                        metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, field: Field, adapter: TypeAdapter)
    : JvmMetaType(fieldPath, fieldName,fieldType, elementType,metaInfo, children, treeIndex, field, adapter) {

    override fun parse(text: String, metadata: StaticMetadata): Any? {
        val metaInfo: MetaInfo = metadata.info()
        var beginIndex = 0
        var endIndex = 0
        val array = createAndFillArray(metaInfo.maxOccurs)
        for (i in array.indices) {
            endIndex += metaInfo.size
            array[i] = parseAtomic(text.substring(beginIndex, endIndex), metadata)
            beginIndex += metaInfo.size
        }
        return maxArrayToValue(array)

    }


    override fun serialize(value: Any?, metadata: StaticMetadata): String {
        val sb = StringBuilder()
        val array = valueToMaxArray(value, metadata.info().maxOccurs)
        for (element in array) {
            if (element == null) {
                sb.append(serializeNull(metadata.info()))
            } else {
                sb.append(serializeValue(element, metadata.info()))
            }
        }
        return sb.toString()
    }
}