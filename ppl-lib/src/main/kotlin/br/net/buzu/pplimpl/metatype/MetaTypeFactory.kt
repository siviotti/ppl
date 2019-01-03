package br.net.buzu.pplimpl.metatype

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.TypeAdapter
import br.net.buzu.model.ValueMapper

fun createMetaType(fullName: String, metaName: String, metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int,
                   typeAdapter: TypeAdapter, valueMapper: ValueMapper): MetaType {
    return GenericMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)

}