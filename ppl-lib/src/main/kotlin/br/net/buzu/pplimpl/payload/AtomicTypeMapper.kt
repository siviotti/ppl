package br.net.buzu.pplimpl.payload

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.TypeAdapter
import br.net.buzu.model.ValueMapper
import br.net.buzu.pplimpl.core.*

class AtomicTypeMapper(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter,valueMapper: ValueMapper)
    : AbstractTypeMapper (metaInfo, serialSize, typeAdapter,valueMapper){

    override fun parse(text: String): Any? = atomicParse(text, metaInfo, valueMapper)

    override fun serialize(value: Any): String = atomicSerialize(value, metaInfo, valueMapper)

}