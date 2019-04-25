/*
 *	This file is part of DefaultPplMapper.
 *
 *   DefaultPplMapper is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DefaultPplMapper is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with DefaultPplMapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.api

import br.net.buzu.pplimpl.jvm.JvmSubtypeResolver
import br.net.buzu.pplimpl.jvm.JvmValueMapperKit
import br.net.buzu.pplimpl.jvm.genericSkip
import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplimpl.metadata.*
import br.net.buzu.pplimpl.metatype.GenericMetaTypeFactory
import br.net.buzu.pplspec.api.PplMapper
import br.net.buzu.pplspec.api.PplSimpleMapper
import br.net.buzu.pplspec.ext.*
import br.net.buzu.pplspec.lang.pplToString
import br.net.buzu.pplspec.model.*
import java.lang.reflect.Field

fun pplSimpleMapper(): PplSimpleMapper = GenericPplSimpleMapper

fun pplMapper(): PplMapper = DefaultPplMapper

@JvmOverloads
fun pplMapperOf(dialect: Dialect = Dialect.DEFAULT,
                metaTypeFactory: MetaTypeFactory = GenericMetaTypeFactory, subtypeResolver: SubtypeResolver = JvmSubtypeResolver,
                valueMapperKit: ValueMapperKit = JvmValueMapperKit, skip: (Field) -> Boolean = genericSkip,
                metadataFactory: MetadataFactory = GenericMetadataFactory,
                metadataCoderResolver: MetadataCoderResolver = GenericMetadataCoderResolver,
                positionalMapperFactory: PositionalMapperFactory = GenericPositionalMapperFactpry)
        : PplMapper = GenericPplMapper(dialect, metaTypeFactory, subtypeResolver, valueMapperKit, skip,
        metadataFactory, metadataCoderResolver, positionalMapperFactory)


internal object GenericPplSimpleMapper : PplSimpleMapper {

    override fun fromPpl(text: String, elementType: Class<*>): Any? {
        val pplString = pplStringOf(text)
        val metadata = parseMetadata(pplString) as StaticMetadata
        val fieldType = if (metadata.kind().isMultiple) List::class.java else elementType
        val metaType = readMetaType(fieldType, elementType)
        val mapper = positionalMapperOf(metadata, metaType)
        return mapper.parse(pplString.payload)
    }

    override fun toPpl(source: Any): String {
        if (source is Collection<*> && source.isEmpty()) return PplString.EMPTY.metadata
        val elementType = getElementType(source)
        val metaType = readMetaType(source.javaClass, elementType)
        val metadata = loadMetadata(source, metaType) as StaticMetadata
        val mapper = positionalMapperOf(metadata, metaType)
        return pplToString(codeMetadata(metadata), mapper.serialize(source))
    }

}

open class GenericPplMapper(val dialect: Dialect,
        // MetaType extension points
                            val metaTypeFactory: MetaTypeFactory, val subtypeResolver: SubtypeResolver,
                            val valueMapperKit: ValueMapperKit, val skip: (Field) -> Boolean,
        // Metadata exrension points
                            val metadataFactory: MetadataFactory, val metadataCoderResolver: MetadataCoderResolver,
        // Mapper extension points
                            val positionalMapperFactory: PositionalMapperFactory)
    : PplMapper {

    override fun fromPpl(text: String, elementType: Class<*>): Any {
        val metadata = parseMetadata(pplStringOf(text), metadataFactory) as StaticMetadata
        val fieldType = if (metadata.kind().isMultiple) List::class.java else elementType
        return fromPpl(text, readMetaType(fieldType, elementType, metaTypeFactory, subtypeResolver, valueMapperKit, skip))
    }

    override fun <T> fromPpl(text: String, metaType: MetaType): T {
        return fromPpl(text, metaType, parseMetadata(pplStringOf(text), metadataFactory) as StaticMetadata)
    }

    override fun <T> fromPpl(text: String, metaType: MetaType, metadata: StaticMetadata): T {
        return positionalMapperFactory.create(metadata, metaType).parse(pplStringOf(text).payload) as T
    }

    override fun toPpl(source: Any): String {
        val elementType = getElementType(source)
        val metaType = readMetaType(source.javaClass, elementType, metaTypeFactory, subtypeResolver,
                valueMapperKit, skip)
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

internal object DefaultPplMapper : GenericPplMapper(Dialect.DEFAULT,
        GenericMetaTypeFactory, JvmSubtypeResolver,
        JvmValueMapperKit, genericSkip,
        GenericMetadataFactory,
        GenericMetadataCoderResolver,
        GenericPositionalMapperFactpry)

private fun getElementType(source: Any): Class<Any> {
    return if (source is Collection<*>) {
        if (source.isEmpty()) Any::class.java else source.iterator().next()!!.javaClass
    } else source.javaClass
}
