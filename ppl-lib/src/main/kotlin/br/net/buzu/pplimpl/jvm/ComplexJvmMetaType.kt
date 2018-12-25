package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.*
import java.lang.reflect.Field

class ComplexJvmMetaType (fieldPath: String, fieldName: String, fieldType: Class<*>, elementType: Class<*>,
                          metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, field: Field, valueMapper: ValueMapper)
    : JvmMetaType(fieldPath, fieldName,fieldType, elementType,metaInfo, children, treeIndex, field, valueMapper) {

    override fun parse(text: String, metadata: StaticMetadata): Any? {
        val metaInfo: MetaInfo = metadata.info()
        var beginIndex = 0
        var endIndex = 0
        var parsed: Any?
        var childMetaType: MetaType
        val array = createAndFillArray(metaInfo.maxOccurs)
        println("array class:" + array[0]?.javaClass)
        for (i in array.indices) {
            for (childMetadata in metadata.children<StaticMetadata>()) {
                println("childMetadata: ${childMetadata.name()}")
                childMetaType = getChildByMetaName(childMetadata.name())
                endIndex += childMetadata.serialMaxSize()
                parsed = childMetaType.parse (text.substring(beginIndex, endIndex), childMetadata)
                println("parsed: $parsed")
                beginIndex += childMetadata.serialMaxSize()
                childMetaType.setFieldValue(array[i]!!, parsed)
            }
        }
        return maxArrayToValue(array)
    }

    override fun serialize(value: Any?, metadata: StaticMetadata): String {
        val sb = StringBuilder()
        val array = valueToMaxArray(value, metadata.info().maxOccurs)
        var childMetaType: MetaType
        for (element in array) {
            for (childMetadata in metadata.children<StaticMetadata>()) {
                childMetaType = getChildByMetaName(childMetadata.name())
                sb.append(childMetaType.serialize(childMetaType.getFieldValue(element!!), childMetadata))
            }
        }
        return sb.toString()
    }
}