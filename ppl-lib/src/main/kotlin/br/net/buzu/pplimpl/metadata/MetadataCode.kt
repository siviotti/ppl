/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.metadata

import br.net.buzu.ext.MetadataCoder
import br.net.buzu.ext.MetadataCoderResolver
import br.net.buzu.lang.*
import br.net.buzu.model.Dialect
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Metadata
import br.net.buzu.model.Subtype

internal const val SPACE = "" + br.net.buzu.lang.SPACE

fun codeMetadata(metadata: Metadata, dialect: Dialect = Dialect.DEFAULT): String = Coders.from(dialect).code(metadata)


fun metadataAsVerbose(metadata: Metadata): String {
    return serialize(metadata, 0, " ", "\n", "\n", " ", " ",
            serializeVerboseSimpleType, " ", serializeVerboseComplexSize, serializeVerboseOccurs)
}

fun metadataAsNatural(metadata: Metadata): String {
    return serialize(metadata, 0, " ", "\n", "\n", " ", " ",
            serializeVerboseSimpleType, " ", serializeNaturalComplexSize, serializeNaturalOccurs)
}

fun metadataAsShort(metadata: Metadata): String {
    return serialize(metadata, 0, "", "", "", "", "",
            serializeVerboseSimpleType, "", serializeNaturalComplexSize, serializeNaturalOccurs)
}

fun metadataAsCompact(metadata: Metadata): String {
    return serialize(metadata, 0, "", "", "", "", "",
            serializeCompactSimpleType, "", serializeNaturalComplexSize, serializeNaturalOccurs)
}

internal fun serialize(metadata: Metadata, level: Int,
        // name
                       afterName: String,
        // elementType
                       afterSubOpen: String, afterMetaEnd: String, indentation: String, afterType: String, serializeSimpleType: (MetaInfo) -> String,
        // size
                       afterSize: String, serializeComplexSize: (MetaInfo, String) -> String,
        // occurs
                       serializeOccurs: (MetaInfo) -> String

): String {
    val metaInfo = metadata.info()
    val complex = metadata.kind().isComplex
    val sb = StringBuilder()

    // Name
    sb.append(serializeName(metaInfo.name, afterName))
    // Subtype
    if (complex) {
        sb.append(SUB_OPEN).append(afterSubOpen)
        for (child in metadata.children<Metadata>()) {
            for (i in 0 until level) {
                sb.append(indentation)
            }
            sb.append(serialize(child, level + 1, afterName, afterSubOpen, afterMetaEnd, indentation, afterType, serializeSimpleType,
                    afterSize, serializeComplexSize, serializeOccurs))
            sb.append(METADATA_END).append(afterMetaEnd)
        }
        sb.deleteCharAt(sb.length - 1) // remove last
        for (i in 0 until level) {
            sb.append(indentation)
        }
        sb.append(SUB_CLOSE)
        sb.append(afterType)
        // Size
        sb.append(serializeComplexSize(metaInfo, afterSize))
    } else {
        sb.append(serializeSimpleType(metaInfo))
        sb.append(afterType)
        // Size
        sb.append(serializeSimpleSize(metaInfo, afterSize))
    }
    // Occurs
    sb.append(serializeOccurs(metaInfo))
    // Extension
    sb.append(serializeExtension(metaInfo))
    return sb.toString()
}

fun fastSerialize(metadata: Metadata, level: Int): String {
    val metaInfo = metadata.info()
    val complex = metadata.kind().isComplex
    val sb = StringBuilder()

    // Name
    sb.append(metaInfo.name)
    // Subtype
    if (complex) {
        sb.append(SUB_OPEN)
        for (child in metadata.children<Metadata>()) {
            sb.append(fastSerialize(child, level + 1))
            sb.append(METADATA_END)
        }
        sb.deleteCharAt(sb.length - 1) // remove last
        sb.append(SUB_CLOSE)
        // Size
        sb.append(serializeVerboseComplexSize(metaInfo, ""))
    } else {
        sb.append(metaInfo.subtype.id)
        // Size
        sb.append(serializeVerboseComplexSize(metaInfo, ""))
    }
    // Occurs
    sb.append(serializeVerboseOccurs(metaInfo))
    // Extension
    sb.append(serializeExtension(metaInfo))
    return sb.toString()
}

// name

internal fun serializeName(name: String, afterName: String): String {
    return if (EMPTY != name && !name.startsWith(NO_NAME_START)) name + NAME_END + afterName else EMPTY
}

// elementType

internal val serializeVerboseSimpleType: (metaInfo: MetaInfo) -> String = { metaInfo ->
    metaInfo.subtype.id
}

internal val serializeCompactSimpleType: (metaInfo: MetaInfo) -> String = { metaInfo ->
    if (Subtype.DEFAULT_SINGLE == metaInfo.subtype) {
        EMPTY
    } else serializeVerboseSimpleType(metaInfo)
}

// size

internal fun serializeSimpleSize(metaInfo: MetaInfo, afterSize: String): String {
    return when {
        metaInfo.subtype.isFixedSizeType -> EMPTY
        metaInfo.hasScale() -> "" + metaInfo.size + DECIMAL_SEP + metaInfo.scale + afterSize
        else -> "" + metaInfo.size + afterSize
    }
}

internal val serializeVerboseComplexSize: (metaInfo: MetaInfo, afterSize: String) -> String = { metaInfo, afterSize ->
    "" + metaInfo.size + afterSize
}

internal val serializeNaturalComplexSize: (metaInfo: MetaInfo, afterSize: String) -> String = { metaInfo, afterSize -> EMPTY }

// occurs

internal val serializeVerboseOccurs: (metaInfo: MetaInfo) -> String = { metaInfo ->
    "" + OCCURS_BEGIN + metaInfo.minOccurs + OCCURS_RANGE + metaInfo.maxOccurs
}

internal val serializeNaturalOccurs: (metaInfo: MetaInfo) -> String = { metaInfo ->
    if (metaInfo.minOccurs == DEFAULT_MIN_OCCURS && metaInfo.maxOccurs == DEFAULT_MAX_OCCURS) {
        EMPTY
    } else {
        if (metaInfo.minOccurs == 1 && metaInfo.maxOccurs == 1) {
            "" + OCCURS_BEGIN
        } else {
            serializeVerboseOccurs(metaInfo)
        }
    }

}

// extension

internal fun serializeExtension(info: MetaInfo): String {
    if (!info.isExtended) {
        return EMPTY
    }
    val sb = StringBuilder()
    if (info.hasDomain()) {
        var valueBegin = ""
        var valueEnd = ""
        if (info.subtype.dataType.group.isDelimited) {
            valueBegin = "" + VALUE_BEGIN
            valueEnd = "" + VALUE_END
        }
        sb.append(DOMAIN_BEGIN)
        for (item in info.domain.items()) {
            sb.append(valueBegin).append(item.asSerial()).append(valueEnd).append(DOMAIN_SEPARATOR)
        }
        sb.deleteCharAt(sb.length - 1)
        sb.append(DOMAIN_END)
    }
    if (info.hasDefaultValue()) {
        sb.append(SPACE + DEFAULT_VALUE + VALUE_BEGIN + info.defaultValue + VALUE_END)
    }
    if (info.hasTags()) {
        sb.append(info.tags)
    }
    return sb.toString()
}

class VerboseCoder : MetadataCoder {
    override fun code(metadata: Metadata): String {
        return metadataAsVerbose(metadata = metadata)
    }
}

class NaturalCoder : MetadataCoder {
    override fun code(metadata: Metadata): String {
        return metadataAsNatural(metadata = metadata)
    }
}

class ShortCoder : MetadataCoder {
    override fun code(metadata: Metadata): String {
        return metadataAsShort(metadata = metadata)
    }
}

class CompactCoder : MetadataCoder {
    override fun code(metadata: Metadata): String {
        return metadataAsCompact(metadata = metadata)
    }
}

class StructuralCoder : MetadataCoder {
    override fun code(metadata: Metadata): String {
        return metadataAsCompact(metadata = metadata)
    }
}

open class Coders : MetadataCoderResolver {
    override fun resolve(dialect: Dialect): MetadataCoder = from(dialect)

    companion object {
        private val CODER_ARRAY = arrayOfNulls<MetadataCoder>(Dialect.values().size)

        init {
            CODER_ARRAY[Dialect.VERBOSE.ordinal] = VerboseCoder()
            CODER_ARRAY[Dialect.NATURAL.ordinal] = NaturalCoder()
            CODER_ARRAY[Dialect.SHORT.ordinal] = ShortCoder()
            CODER_ARRAY[Dialect.COMPACT.ordinal] = CompactCoder()
            CODER_ARRAY[Dialect.STRUCTURAL.ordinal] = StructuralCoder()
        }

        fun from(dialect: Dialect): MetadataCoder {
            return CODER_ARRAY[dialect.ordinal]
                    ?: throw IllegalStateException("Invalid array 'CODER_ARRAY'. Missing $dialect")
        }

    }
}

object GenericMetadataCoderResolver: Coders()