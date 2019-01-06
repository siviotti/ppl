package br.net.buzu.pplimpl.api

import br.net.buzu.api.PplMapper
import br.net.buzu.api.PplSimpleMapper
import br.net.buzu.ext.*
import br.net.buzu.lang.pplToString
import br.net.buzu.model.Dialect
import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.pplStringOf
import br.net.buzu.pplimpl.jvm.JvmSubtypeResolver
import br.net.buzu.pplimpl.jvm.JvmValueMapperKit
import br.net.buzu.pplimpl.jvm.genericSkip
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.*
import br.net.buzu.pplimpl.metatype.GenericMetaTypeFactory
import java.lang.reflect.Field

fun pplSimpleMapper(): PplSimpleMapper = GenericPplSimpleMapper()

fun pplMapper(dialect: Dialect = Dialect.DEFAULT,
              metaTypeFactory: MetaTypeFactory = GenericMetaTypeFactory, subtypeResolver: SubtypeResolver = JvmSubtypeResolver,
              valueMapperKit: ValueMapperKit = JvmValueMapperKit, skip: (Field) -> Boolean = genericSkip,
              metadataFactory: MetadataFactory = GenericMetadataFactory,
              metadataCoderResolver: MetadataCoderResolver = GenericMetadataCoderResolver,
              positionalMapperFactory: PositionalMapperFactory = GenericPositionalMapperFactpry)
        : PplMapper = GenericPplMapper(dialect, metaTypeFactory, subtypeResolver, valueMapperKit, skip,
        metadataFactory, metadataCoderResolver, positionalMapperFactory)


internal class GenericPplSimpleMapper : PplSimpleMapper {

    override fun <T> fromPpl(text: String, type: Class<T>): T {
        val pplString = pplStringOf(text)
        val metadata = parseMetadata(pplString) as StaticMetadata
        val metaType = readMetaType(type)
        val mapper = positionalMapperOf(metadata, metaType)
        return mapper.parse(pplString.payload) as T
    }

    override fun toPpl(source: Any): String {
        val metaType = readMetaType(source.javaClass)
        val metadata = loadMetadata(source, metaType) as StaticMetadata
        val mapper = positionalMapperOf(metadata, metaType)
        return pplToString(codeMetadata(metadata), mapper.serialize(source))
    }
}

internal class GenericPplMapper(val dialect: Dialect,
        // MetaType extension points
                                val metaTypeFactory: MetaTypeFactory, val subtypeResolver: SubtypeResolver,
                                val valueMapperKit: ValueMapperKit, val skip: (Field) -> Boolean,
        // Metadata exrension points
                                val metadataFactory: MetadataFactory, val metadataCoderResolver: MetadataCoderResolver,
        // Mapper extension points
                                val positionalMapperFactory: PositionalMapperFactory)
    : PplMapper {

    override fun <T> fromPpl(text: String, type: Class<T>): T {
        return fromPpl(text, readMetaType(type, metaTypeFactory, subtypeResolver, valueMapperKit, skip))
    }

    override fun <T> fromPpl(text: String, metaType: MetaType): T {
        return fromPpl(text, metaType, parseMetadata(pplStringOf(text), metadataFactory) as StaticMetadata)
    }

    override fun <T> fromPpl(text: String, metaType: MetaType, metadata: StaticMetadata): T {
        return positionalMapperFactory.create(metadata, metaType).parse(pplStringOf(text).payload) as T
    }

    override fun toPpl(source: Any): String {
        val metaType = readMetaType(source.javaClass, metaTypeFactory, subtypeResolver, valueMapperKit, skip)
        return toPpl(source, metaType, loadMetadata(source, metaType, metadataFactory) as StaticMetadata)
    }

    override fun toPpl(source: Any, metaType: MetaType): String {
        return toPpl(source, metaType, loadMetadata(source, metaType, metadataFactory) as StaticMetadata)
    }

    override fun toPpl(source: Any, metaType: MetaType, metadata: StaticMetadata): String {
        return pplToString(metadataCoderResolver.resolve(dialect).code(metadata),
                positionalMapperFactory.create(metadata, metaType).serialize(source))
    }
}