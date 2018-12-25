package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.ValueMapper
import java.lang.reflect.Field

class AtomicJvmMetaType(fieldPath: String, fieldName: String, fieldType: Class<*>, elementType: Class<*>,
                        metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, field: Field, valueMapper: ValueMapper)
    : JvmMetaType(fieldPath, fieldName, fieldType, elementType, metaInfo, children, treeIndex, field, valueMapper) {

    override fun parse(text: String, metadata: StaticMetadata): Any? = parseAtomic(text, metadata)

    override fun serialize(value: Any?, metadata: StaticMetadata): String {
        return serializeAtomic(value, metadata)
    }

}
