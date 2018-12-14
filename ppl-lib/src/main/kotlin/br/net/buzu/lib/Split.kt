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
package br.net.buzu.lib

import br.net.buzu.pplspec.exception.PplParseException
import br.net.buzu.pplspec.lang.EMPTY
import br.net.buzu.pplspec.lang.VAR
import br.net.buzu.pplspec.lang.*
import br.net.buzu.pplspec.model.PplNode

internal const val UNTERMINATED_STRING_LITERAL = "Unterminated String Literal using "
internal const val WRONG_USE_OF_BLOCK = "Wrong use domainOf block "

fun splitNodes(text: String): List<PplNode> {
    val list = mutableListOf<PplNode>()
    var c = ' '
    var sub = 0
    var brace = 0
    var array = 0
    var beginMeta = 0
    var i = 0
    while (i < text.length) {
        c = text[i]
        sub = countBlock(sub, c, SUB_OPEN, SUB_CLOSE)
        brace = countBlock(brace, c, BRACE_OPEN, BRACE_CLOSE)
        array = countBlock(array, c, ARRAY_OPEN, ARRAY_CLOSE)
        if (pplIsStringDelimiter(c)) {
            i = pplNextStringDelimiter(text, i, c) + 1
            continue
        }
        if (c == METADATA_END && sub + brace + array == 0) {
            list.add(splitNode(text.substring(beginMeta, i)))
            beginMeta = i + 1
        }
        i++
    }
    checkBlock("( )", sub, text)
    checkBlock("{ }", brace, text)
    checkBlock("[ ]", array, text)
    if (c != METADATA_END) {
        list.add(splitNode(text.substring(beginMeta, i)))
    }
    return list
}

internal fun splitNode(text: String): PplNode {

    val lastCharIndex = text.length - 1

    val sName = splitName(text, 0)

    val sType = splitType(text, sName.index)

    if (sType.index > lastCharIndex) {
        return PplNode(name = sName.part, type = sType.part, children = sType.children)
    }
    val sSize = splitSize(text, sType.index)

    if (sSize.index > lastCharIndex) {
        return PplNode(name = sName.part, type = sType.part, size = sSize.part, children = sType.children)
    }
    val sOccurs = splitOccurs(text, sSize.index)

    if (sOccurs.index > lastCharIndex) {
        return PplNode(name = sName.part, type = sType.part, size = sSize.part, occurs = sOccurs.part, children = sType.children)
    }
    val sDomain = splitDomain(text, sOccurs.index)

    if (sDomain.index > lastCharIndex) {
        return PplNode(name = sName.part, type = sType.part, size = sSize.part, occurs = sOccurs.part, domain = sDomain.part, children = sType.children)
    }
    val sDefaultValue = splitDefaultValue(text, sDomain.index)

    if (sDefaultValue.index > lastCharIndex) {
        return PplNode(name = sName.part, type = sType.part, size = sSize.part, occurs = sOccurs.part, defaultValue = sDefaultValue.part, children = sType.children)
    }
    val sTags = splitTags(text, sDefaultValue.index)

    return PplNode(name = sName.part, type = sType.part, size = sSize.part,
            occurs = sOccurs.part, domain = sDomain.part, defaultValue = sDefaultValue.part,
            tags = sTags, children = sType.children)
}

inline private fun countBlock(value: Int, c: Char, open: Char, close: Char): Int {
    if (c == open) {
        return value + 1
    }
    return if (c == close) value - 1 else value
}

inline private fun checkBlock(element: String, count: Int, text: String) {
    if (count != 0) {
        throw PplParseException(WRONG_USE_OF_BLOCK + element + ". Too many "
                + (if (count > 0) "'Open'" else "'Close'") + " Text:\n" + text)
    }
}

inline private fun checkStr(c: Char, noOpened: Boolean, text: String) {
    if (!noOpened) {
        throw PplParseException(UNTERMINATED_STRING_LITERAL + c + " Text:\n" + text)
    }
}

internal fun splitName(text: String, beginIndex: Int): Split {
    if (text.isEmpty()) {
        return Split(beginIndex)
    }
    val firstChar = pplFirstChar(text, 0)
    var c = text[firstChar]
    var count = 0
    if (pplIsValidFirstCharMetaName(c) || c == VAR) {
        count++
    } else {
        return if (c == NAME_END) Split(firstChar + 1) else Split(firstChar)
    }
    for (i in firstChar + 1 until text.length) {
        c = text[i]
        if (pplIsValidCharMetaName(c)) {
            count++
        } else if (c == NAME_END) {
            return Split(i + 1, text.substring(firstChar, firstChar + count))
        } else {
            break
        }
    }
    return Split(firstChar)
}

internal fun splitType(text: String, beginIndex: Int): TypeSplit {
    val firstChar = pplFirstChar(text, beginIndex)
    var endIndex = firstChar
    var offset = 0
    var endBlock = 0
    var c: Char
    var children = PplNode.EMPTY_CHILDREN
    for (i in firstChar until text.length) {
        c = text[i]
        if (Character.isLetter(c)) { // A-Za-z c >= 'A' && c <= 'Z'
            endIndex++
        } else if (c == SUB_OPEN) {
            endBlock = pplBlockEndIndex(text, i, SUB_OPEN, SUB_CLOSE)
            children = splitNodes(text.substring(i + 1, endBlock))
            offset = endBlock + 1// primeiro após )
            break
        } else {
            break
        }
    }
    val part = text.substring(firstChar, endIndex)

    if (children.size == 0) {
        return TypeSplit(endIndex, part)
    }
    return TypeSplit(offset, part, children)
}

internal fun splitSize(text: String, beginIndex: Int): Split {
    val firstChar = pplFirstChar(text, beginIndex)
    val endIndex = pplLastNumberIndex(text, firstChar, DECIMAL_SEP)
    if (endIndex > beginIndex) {
        return Split(endIndex, text.substring(firstChar, endIndex))
    }
    return Split(endIndex)
}

internal fun splitOccurs(text: String, beginIndex: Int): Split {
    var firstChar = pplFirstChar(text, beginIndex)
    if (text[firstChar] != OCCURS_BEGIN) {
        return Split(firstChar)
    }
    firstChar++ // skip #
    if (firstChar == text.length) {
        return Split(firstChar) // returns the # treeIndex
    }
    val endIndex = pplLastNumberIndex(text, firstChar, OCCURS_RANGE)
    if (endIndex > beginIndex) {
        return Split(endIndex, text.substring(firstChar, endIndex))
    }
    return Split(firstChar)
}

internal fun splitDomain(text: String, beginIndex: Int): Split {
    val firstChar = pplFirstChar(text, beginIndex)
    if (text[firstChar] != DOMAIN_BEGIN) {
        return Split(firstChar)
    }
    val endIndex = pplBlockEndIndex(text, firstChar, DOMAIN_BEGIN, DOMAIN_END)
    return Split(endIndex + 1, text.substring(firstChar, endIndex + 1))
}

internal fun splitDefaultValue(text: String, beginIndex: Int): Split {
    val firstChar = pplFirstChar(text, beginIndex)
    var c = text[firstChar]
    if (c != DEFAULT_VALUE) {
        return Split(firstChar)
    }
    val index = firstChar + 1
    if (index > text.length - 1) {
        return Split(firstChar)
    }
    c = text[index]
    val endIndex: Int
    if (pplIsStringDelimiter(c)) {
        endIndex = pplNextStringDelimiter(text, index, c) + 1
        return Split(endIndex + 1, text.substring(firstChar + 2, endIndex - 1))
    } else {
        endIndex = pplNextCharOrLast(text, index, SPACE)
        return Split(endIndex + 1, text.substring(firstChar + 1, endIndex))
    }

}

internal fun splitTags(text: String, beginIndex: Int): String {
    val firstChar = pplFirstChar(text, beginIndex)
    var endIndex = firstChar
    for (i in firstChar until text.length) {
        if (!pplIsIgnored(text[i])) {
            endIndex = i
        }
    }
    return text.substring(firstChar, endIndex + 1)
}

internal data class Split(val index: Int, val part: String = EMPTY)

internal data class TypeSplit(val index: Int, val part: String = EMPTY, val children: List<PplNode> = PplNode.EMPTY_CHILDREN)

