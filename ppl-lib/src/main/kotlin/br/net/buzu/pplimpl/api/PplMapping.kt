package br.net.buzu.pplimpl.api

import br.net.buzu.api.PplMapper
import br.net.buzu.lang.pplToString
import br.net.buzu.model.Dialect
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.codeMetadata
import br.net.buzu.pplimpl.metadata.loadMetadata
import br.net.buzu.pplimpl.metadata.parseMetadata

fun pplMapperOf(dialect:Dialect= Dialect.DEFAULT) : PplMapper{
    return BasicPplMapper(dialect)
}

class BasicPplMapper (val dialect: Dialect): PplMapper{

    override fun <T> fromPpl(text: String, type: Class<T>): T {
        val pplString = pplStringOf(text)
        val metaType = readMetaType(type)
        val metadata = parseMetadata(pplString) as StaticMetadata
        val mapper = positionalMapperOf(metadata, metaType)
        return mapper.parse(text) as T
    }

    override fun toPpl(source: Any): String {
        val metaType = readMetaType(source.javaClass)
        val metadata = loadMetadata(source, metaType) as StaticMetadata
        val mapper = positionalMapperOf(metadata, metaType)
        return pplToString(codeMetadata(metadata, dialect), mapper.serialize(source))
    }

}