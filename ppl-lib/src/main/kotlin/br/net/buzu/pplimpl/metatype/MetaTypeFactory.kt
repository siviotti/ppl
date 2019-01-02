package br.net.buzu.pplimpl.metatype

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.TypeAdapter
import br.net.buzu.model.ValueMapper

typealias  CreateMetaType = (String, String, MetaInfo, List<MetaType>, Int, TypeAdapter, ValueMapper) -> MetaType

val genericCreateMetaType: CreateMetaType = { fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper ->
    createMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)
}

fun createMetaType(fullName: String, metaName: String, metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int,
                   typeAdapter: TypeAdapter, valueMapper: ValueMapper): MetaType {
    return when {
        typeAdapter.isComplex -> ComplexMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)
        metaInfo.isMultiple -> SimpleMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)
        else -> AtomicMetaType(fullName, metaName, metaInfo, children, treeIndex, typeAdapter, valueMapper)
    }

}