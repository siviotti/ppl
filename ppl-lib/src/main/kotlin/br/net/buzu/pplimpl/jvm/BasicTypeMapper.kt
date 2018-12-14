package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplspec.model.TypeMapper
import br.net.buzu.pplspec.model.Kind
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import java.lang.reflect.Field


class BasicTypeMapper (val field:Field, val kind: Kind, metaInfo: MetaInfo) : TypeMapper {

    override fun toMetadata(createMetadata: (MetaInfo, List<Metadata>) -> Metadata) {
    }

    override fun toPayload(typedObject: Any): String {
        return ""

    }

    override fun fromPayload(payload: String): Any {
        return ""
    }



}