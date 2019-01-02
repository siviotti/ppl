package br.net.buzu.pplimpl.payload

import br.net.buzu.model.*
import br.net.buzu.pplimpl.core.*

class ComplexTypeMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, valueMapper: ValueMapper,
                        val children: List<AbstractTypeMapper>)
    : AbstractTypeMapper (metaInfo, serialSize, typeAdapter,valueMapper){

    override fun parse(text: String): Any? {
        var beginIndex = 0
        var endIndex = 0
        var parsed: Any?
        var childMetaType: MetaType
        val array = typeAdapter.createAndFillArray(metaInfo.maxOccurs)
        for (i in array.indices) {
            for (child in children) {
                endIndex += child.serialSize
                parsed = child.parse (text.substring(beginIndex, endIndex))
                beginIndex += child.serialSize
                typeAdapter.setFieldValue(array[i]!!, parsed)
            }
        }
        return typeAdapter.maxArrayToValue(array)
    }

    override fun serialize(value: Any): String {
        val sb = StringBuilder()
        val array = typeAdapter.valueToMaxArray(value, metaInfo.maxOccurs)
        for (element in array) {
            for (child in children) {
                sb.append(child.serialize(child.typeAdapter.getFieldValue(element!!)!!))
            }
        }
        return sb.toString()

    }

}