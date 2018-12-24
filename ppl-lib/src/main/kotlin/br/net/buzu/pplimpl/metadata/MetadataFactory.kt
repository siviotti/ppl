package br.net.buzu.pplimpl.metadata

import br.net.buzu.api.MetadataFactory
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Metadata

typealias CreateMetadata = (MetaInfo, List<Metadata>) -> Metadata

val genericCreateMetadata: CreateMetadata = { metaInfo, children -> createMetadata(metaInfo, children) }

fun createMetadata(metaInfo: MetaInfo, children: List<Metadata>): Metadata {
    return if (children.isEmpty())
        if (metaInfo.isStatic)
            SimpleStaticMetadata(metaInfo)
        else
            SimpleMetadata(metaInfo)
    else {
        if (br.net.buzu.lib.isStaticChildren(children))
            ComplexStaticMetadada(metaInfo, children)
        else
            ComplexMetadata(metaInfo, children)
    }
}

class GenericMetadatafactory : MetadataFactory {
    override fun create(metaInfo: MetaInfo, children: List<Metadata>): Metadata {
        return createMetadata(metaInfo, children)
    }
}
