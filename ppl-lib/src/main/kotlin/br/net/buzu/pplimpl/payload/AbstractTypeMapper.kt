package br.net.buzu.pplimpl.payload

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.TypeAdapter
import br.net.buzu.model.TypeMapper
import br.net.buzu.model.ValueMapper

abstract class AbstractTypeMapper (val metaInfo: MetaInfo, val serialSize: Int, val typeAdapter: TypeAdapter,
                                   val valueMapper: ValueMapper) : TypeMapper{
}