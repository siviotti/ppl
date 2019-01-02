package br.net.buzu.pplimpl.api

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.TypeAdapter
import br.net.buzu.model.TypeMapper
import br.net.buzu.model.ValueMapper
import br.net.buzu.pplimpl.core.fill
import br.net.buzu.pplimpl.core.fit

abstract class GenericTypeMapper<T>(val metaInfo: MetaInfo, val serialSize: Int, val typeAdapter: TypeAdapter,
                                    val valueMapper: ValueMapper) : TypeMapper<T> {
}

class AtomicTypeMApper<T>(metaInfo: MetaInfo, serialSize: Int, typeAdapter: TypeAdapter, valueMapper: ValueMapper)
    : GenericTypeMapper<T>(metaInfo, serialSize, typeAdapter, valueMapper) {

    override fun parse(text: String): T? {
        return null
    }

    override fun serialize(value: T?): String {
        return if (value == null) serializeNull()
        else fit(metaInfo.align, valueMapper.toText(value), metaInfo.size, metaInfo.fillChar)
    }

    private fun serializeNull(): String = fill(metaInfo.align, metaInfo.defaultValue, metaInfo.size, metaInfo.nullChar)
}