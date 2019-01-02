package br.net.buzu.pplimpl.payload

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.TypeAdapter
import br.net.buzu.model.ValueMapper
import br.net.buzu.pplimpl.core.*

class ArrayTypeMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, valueMapper: ValueMapper)
    : AbstractTypeMapper (metaInfo, serialSize, typeAdapter,valueMapper){

    override fun parse(text: String): Any? = arrayParse(text, metaInfo, typeAdapter, valueMapper)

    override fun serialize(value: Any): String = arraySerialize(value, metaInfo, typeAdapter, valueMapper)

}