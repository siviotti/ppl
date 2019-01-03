package br.net.buzu.pplimpl.api

import br.net.buzu.api.TypeMapper
import br.net.buzu.model.*
import br.net.buzu.pplimpl.core.arrayParse
import br.net.buzu.pplimpl.core.arraySerialize
import br.net.buzu.pplimpl.core.atomicParse
import br.net.buzu.pplimpl.core.atomicSerialize

abstract class BasicTypeMapper (val metaInfo: MetaInfo, val serialSize: Int, val typeAdapter: TypeAdapter) : TypeMapper{
    companion object {
        fun create(metadata: StaticMetadata, metaType: MetaType): TypeMapper {
            val info = metadata.info()
            return when (metadata.kind()){
                Kind.ATOMIC -> AtomicTypeMapper(info, metadata.serialMaxSize(), metaType.typeAdapter, metaType.getValueMapperFor(info))
                Kind.ARRAY -> ArrayTypeMapper(info, metadata.serialMaxSize(), metaType.typeAdapter, metaType.getValueMapperFor(info))
                else -> ComplexTypeMapper(info, metadata.serialMaxSize(), metaType.typeAdapter, createChildren(metadata, metaType))
            }
        }

        private fun createChildren(metadata: StaticMetadata, metaType: MetaType): List<BasicTypeMapper> {
            val children = mutableListOf<BasicTypeMapper>()
            for (childMetadata in metadata.children<StaticMetadata>()){
                children.add(create(childMetadata, metaType.getChildByMetaName(childMetadata.name())) as BasicTypeMapper)
            }
            return children
        }
    }
}

internal class AtomicTypeMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, private val valueMapper: ValueMapper)
    : BasicTypeMapper(metaInfo, serialSize, typeAdapter){

    override fun parse(text: String): Any? = atomicParse(text, metaInfo, valueMapper)
    override fun serialize(value: Any?): String = atomicSerialize(value, metaInfo, valueMapper)
}

internal class ArrayTypeMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, private val valueMapper: ValueMapper)
    : BasicTypeMapper(metaInfo, serialSize, typeAdapter){

    override fun parse(text: String): Any? = arrayParse(text, metaInfo, typeAdapter, valueMapper)
    override fun serialize(value: Any?): String = arraySerialize(value, metaInfo, typeAdapter, valueMapper)
}

internal class ComplexTypeMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, val children: List<BasicTypeMapper>)
    : BasicTypeMapper(metaInfo, serialSize, typeAdapter) {

    override fun parse(text: String): Any? {
        var beginIndex = 0
        var endIndex = 0
        var parsed: Any?
        val array = typeAdapter.createAndFillArray(metaInfo.maxOccurs)
        for (i in array.indices) {
            for (child in children) {
                endIndex += child.serialSize
                parsed = child.parse(text.substring(beginIndex, endIndex))
                beginIndex += child.serialSize
                child.typeAdapter.setFieldValue(array[i]!!, parsed)
            }
        }
        return typeAdapter.maxArrayToValue(array)
    }

    override fun serialize(value: Any?): String {
        val sb = StringBuilder()
        val array = typeAdapter.valueToMaxArray(value, metaInfo.maxOccurs)
        for (element in array) {
            for (child in children) {
                sb.append(child.serialize(child.typeAdapter.getFieldValue(element!!)))
            }
        }
        return sb.toString()

    }

}