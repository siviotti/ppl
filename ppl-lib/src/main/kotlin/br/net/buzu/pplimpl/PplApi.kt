@file:JvmName("PplApi")
package br.net.buzu.pplimpl

import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.loadMetadata
import br.net.buzu.pplimpl.metadata.parseMetadata

fun fromPpl(text: String, type: Class<*>): Any? {
    val pplString = pplStringOf(text)
    val metadata = parseMetadata(pplString) as StaticMetadata
    val metaType = readMetaType(type)
    return metaType.parse(pplString.payload, metadata)
}

fun toPpl(obj: Any): String {
    val metaType = readMetaType(obj.javaClass)
    val metadata = loadMetadata(obj, metaType) as StaticMetadata
    return metaType.serialize(obj, metadata)
}