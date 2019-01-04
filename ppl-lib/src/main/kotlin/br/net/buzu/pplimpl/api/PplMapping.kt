package br.net.buzu.pplimpl.api

import br.net.buzu.api.ParamPplMapper
import br.net.buzu.api.PplMapper
import br.net.buzu.lang.pplToString
import br.net.buzu.model.Dialect
import br.net.buzu.model.MetaType
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
        return mapper.parse(pplString.payload) as T
    }

    override fun toPpl(source: Any): String {
        val metaType = readMetaType(source.javaClass)
        val metadata = loadMetadata(source, metaType) as StaticMetadata
        val mapper = positionalMapperOf(metadata, metaType)
        return pplToString(codeMetadata(metadata, dialect), mapper.serialize(source))
    }
}

class BasicParamPplMapper(val dialect: Dialect): ParamPplMapper{

    override fun <T> fromPpl(text: String, type: Class<T>): T {
        return fromPpl(text, readMetaType(type))
    }

    override fun <T> fromPpl(text: String, metaType: MetaType): T {
        return fromPpl(text, metaType, parseMetadata(pplStringOf(text)) as StaticMetadata)
    }

    override fun <T> fromPpl(text: String, metaType: MetaType, metadata: StaticMetadata): T {
        val pplString = pplStringOf(text)
        val mapper = positionalMapperOf(metadata, metaType)
        return mapper.parse(pplString.payload) as T

    }

    override fun toPpl(source: Any): String {
        val metaType = readMetaType(source.javaClass)
        return toPpl(source, metaType, loadMetadata(source, metaType) as StaticMetadata)
    }

    override fun toPpl(source: Any, metaType: MetaType): String {
        return toPpl(source, metaType, loadMetadata(source, metaType) as StaticMetadata)
    }

    override fun toPpl(source: Any, metaType: MetaType, metadata: StaticMetadata): String {
        val mapper = positionalMapperOf(metadata, metaType)
        return pplToString(codeMetadata(metadata, dialect), mapper.serialize(source))
    }


}