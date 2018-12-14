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

import br.net.buzu.lib.isStaticChidren
import br.net.buzu.lib.splitNodes
import br.net.buzu.pplspec.api.MetadataParser
import br.net.buzu.pplspec.exception.MetadataParseException
import br.net.buzu.pplspec.exception.PplParseException
import br.net.buzu.pplspec.lang.*
import br.net.buzu.pplspec.model.*
import java.text.ParseException
import java.util.*

internal const val SUBTYPE_NOT_FOUND = "Subtype not found:"
internal const val INVALID_DOMAIN = "Invalid domain:"

private val EMPTY_DOMAIN = Domain.EMPTY
private val EMPTY_CHILDREN: List<Metadata> = listOf()

typealias CreateMetadata = (MetaInfo, List<Metadata>) -> Metadata

val genericCreateMetadata: CreateMetadata = { metaInfo, children ->
    if (children.isEmpty())
        if (metaInfo.isStatic)
            SimpleStaticMetadata(metaInfo)
        else
            SimpleMetadata(metaInfo)
    else {
        if (isStaticChidren(children))
            ComplexStaticMetadada(metaInfo, children)
        else
            ComplexMetadata(metaInfo, children)
    }
}

fun parseMetadata(pplString: PplString): Metadata {
    return parseMetadata(pplString, createMetadata = genericCreateMetadata)
}

fun parseMetadata(pplString: PplString, createMetadata: CreateMetadata, seq: IndexSequence = IndexSequence()): Metadata {
    try {
        val nodes = splitNodes(pplString.pplMetadata)
        if (nodes.size > 1) {
            return parseMetadata(EMPTY, createRoot(nodes), createMetadata, seq)
        } else {
            return parseMetadata(EMPTY, nodes[0], createMetadata, seq)
        }
    } catch (e: ParseException) {
        throw PplParseException("Parsing error on text:\n$pplString", e)
    }

}

internal fun createRoot(nodes: List<PplNode>): PplNode {
    val map = HashMap<String, String>()
    val children = mutableListOf<PplNode>()
    for (child in nodes) {
        if (child.isVar()) {
            map[child.name.substring(1)] = child.defaultValue
        } else {
            children.add(child)
        }
    }
    val rootName: String = if (map.containsKey(VAR_ROOT)) map[VAR_ROOT].toString() else "root"
    return PplNode(name = rootName, children = children)
}

internal fun parseMetadata(parentId: String, node: PplNode, createMetadata: CreateMetadata, seq: IndexSequence): Metadata {
    val index = seq.next()
    val name = parseName(node, index)
    val subtype = parseSubtype(node)
    val size = parseSize(node, subtype)
    val scale = parseScale(node, subtype)
    val minOccurs = parseMinOccurs(node)
    val maxOccurs = parseMaxOccurs(node)
    val domain = parseDomain(node)
    val metaInfo = MetaInfo(parentId, index, name, subtype, size, scale, minOccurs, maxOccurs, domain,
            node.defaultValue, node.tags)
    return createMetadata(metaInfo, parseChildren(metaInfo.id, node, createMetadata, seq))
}

internal fun parseChildren(parentId: String, node: PplNode, createMetadata: CreateMetadata, seq: IndexSequence): List<Metadata> {
    if (!node.isComplex()) {
        return EMPTY_CHILDREN
    }
    val metas = mutableListOf<Metadata>()
    for (i in 0 until node.children.size) {
        metas.add(parseMetadata(parentId, node.children.get(i), createMetadata, seq))
    }
    return metas
}

internal fun parseName(node: PplNode, index: Int): String {
    val name = if (node.hasName()) node.name else NO_NAME_START + index
    if (!pplIsValidMetaName(name)) {
        throw MetadataParseException("Invalid Metadata name:$name", node)
    }
    return name
}

internal fun parseSubtype(node: PplNode): Subtype {
    if (node.hasType()) {
        val subtype = Subtype.of(node.type)
                ?: throw MetadataParseException(SUBTYPE_NOT_FOUND + node.type, node)
        return subtype
    }
    return if (node.isComplex()) Subtype.DEFAULT_COMPLEX else Subtype.DEFAULT_SINGLE
}

internal fun parseSize(node: PplNode, subtype: Subtype): Int {
    if (node.hasSize()) {
        if (subtype.dataType().sizeType() == SizeType.FIXED) {
            throw MetadataParseException(subtype.toString() + " do not support custom size." + subtype, node)
        }
        return extractSize(node)
    }
    return subtype.fixedSize()
}

internal fun parseScale(node: PplNode, subtype: Subtype): Int {
    if (node.hasSize()) {
        if (subtype.dataType().sizeType() == SizeType.FIXED) {
            throw MetadataParseException(subtype.toString() + " do not support custom scale." + subtype, node)
        }
        return extractScale(node)
    }
    return 0
}

internal fun extractSize(node: PplNode): Int {
    var size = node.size
    val index = size.indexOf(DECIMAL_SEP)
    if (index > 0) {
        size = size.substring(0, index)
    }
    return Integer.parseInt(size)
}

internal fun extractScale(node: PplNode): Int {
    var scale = node.size
    val index = scale.indexOf(DECIMAL_SEP)
    if (index > 0) {
        scale = scale.substring(index + 1)
        return if (scale.length > 0) Integer.parseInt(scale) else 0
    }
    return 0
}

internal fun parseMinOccurs(node: PplNode): Int {
    return if (node.hasOccurs()) extractMinOccurs(node.occurs) else 0
}

internal fun extractMinOccurs(occurs: String): Int {
    val index = occurs.indexOf(OCCURS_RANGE)
    return if (index < 0) DEFAULT_MIN_OCCURS else Integer.parseInt(occurs.substring(0, index))
}

internal fun parseMaxOccurs(node: PplNode): Int {
    return if (node.hasOccurs()) extractMaxOccurs(node.occurs) else 1
}

internal fun extractMaxOccurs(occurs: String): Int {
    val index = occurs.indexOf(OCCURS_RANGE)
    return if (index < -1) Integer.parseInt(occurs) else Integer.parseInt(occurs.substring(index + 1))
}

internal fun parseDomain(node: PplNode): Domain {
    var domainStr = node.domain
    if (domainStr == null || domainStr!!.length < 3) {
        return EMPTY_DOMAIN
    }
    if (domainStr!!.get(0) != DOMAIN_BEGIN || domainStr!!.get(domainStr!!.length - 1) != DOMAIN_END) {
        throw MetadataParseException(INVALID_DOMAIN + domainStr!!, node)
    }
    domainStr = domainStr!!.substring(1, domainStr!!.length - 1)
    if (domainStr!!.trim({ it <= ' ' }).isEmpty()) {
        return EMPTY_DOMAIN
    }
    val list = ArrayList<String>()
    var c: Char
    var beginIndex = 0
    var endIndex = domainStr!!.length
    var i = 0
    while (i < domainStr!!.length) {
        c = domainStr!!.get(i)
        if (c == PLIC || c == QUOTE) {
            try {
                i = pplNextStringDelimiter(domainStr!!, i, c)
            } catch (e: ParseException) {
                throw MetadataParseException(INVALID_DOMAIN + domainStr!!, node, e)
            }

            i++
            continue
        }
        if (c == DOMAIN_SEPARATOR) {
            endIndex = i
            list.add(extractItem(domainStr!!, beginIndex, endIndex))
            beginIndex = endIndex + 1
        }
        i++

    }
    list.add(extractItem(domainStr!!, beginIndex, domainStr!!.length))

    return createDomain(node.name + PATH_SEP + "domain", toDomainItemList(list))
}

private fun extractItem(domain: String, beginIndex: Int, endIndex: Int): String {
    val s = domain.substring(beginIndex, endIndex)
    val firstChar = s[0]
    if (firstChar == QUOTE || firstChar == PLIC) {
        val lastChar = s[s.length - 1]
        if (firstChar == lastChar) {
            return s.substring(1, s.length - 1)
        }
    }
    return s
}

class GenericMetadataParser : MetadataParser {
    override fun parse(pplString: PplString): Metadata {
        return br.net.buzu.pplimpl.metadata.parseMetadata(pplString)
    }
}

class IndexSequence() {  private var internalValue = 0;    fun next() = internalValue++ }

private fun isStaticChildren(children: List<Metadata>): Boolean {
    for (child in children) {
        if (child !is StaticStructure) {
            return false
        }
    }
    return true
}
