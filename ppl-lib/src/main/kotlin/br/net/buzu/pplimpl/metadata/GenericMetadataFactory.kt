package br.net.buzu.pplimpl.metadata

import br.net.buzu.ext.MetadataFactory
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Metadata
import br.net.buzu.model.StaticStructure

object GenericMetadataFactory : MetadataFactory {
    override fun create(metaInfo: MetaInfo, children: List<Metadata>): Metadata {
        return if (children.isEmpty())
            if (metaInfo.isStatic)
                SimpleStaticMetadata(metaInfo)
            else
                SimpleMetadata(metaInfo)
        else {
            if (isStaticChildren(children))
                ComplexStaticMetadata(metaInfo, children)
            else
                ComplexMetadata(metaInfo, children)
        }
    }
    private fun isStaticChildren(children: List<Metadata>): Boolean {
        for (child in children) {
            if (child !is StaticStructure) {
                return false
            }
        }
        return true
    }
}

