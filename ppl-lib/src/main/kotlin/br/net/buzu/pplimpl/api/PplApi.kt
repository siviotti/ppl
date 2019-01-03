@file:JvmName("PplApi")
package br.net.buzu.pplimpl.api

import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.loadMetadata
import br.net.buzu.pplimpl.metadata.parseMetadata

////////////////////////////////////////
// PARSING (Text -> Object)
////////////////////////////////////////

fun fromPpl(text: String, type: Class<*>): Any? {
    val pplString = pplStringOf(text)
    val metadata = parseMetadata(pplString) as StaticMetadata
    val metaType = readMetaType(type)
    return positionalMapperOf (metadata, metaType).parse(pplString.payload)
}

////////////////////////////////////////
// SERIALIZATION (Object -> Text)
////////////////////////////////////////

fun toPpl(obj: Any): String {
    val metaType = readMetaType(obj.javaClass)
    val metadata = loadMetadata(obj, metaType) as StaticMetadata
    return metaType.serialize(obj, metadata)
}

fun toPpl(obj: Any, metaType: MetaType= readMetaType(obj.javaClass), metadata: StaticMetadata = loadMetadata(obj,
        metaType) as StaticMetadata): String {
    return metaType.serialize(obj, metadata)
}