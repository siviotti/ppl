package br.net.buzu.lib

import br.net.buzu.pplspec.lang.EMPTY;
import br.net.buzu.pplspec.lang.pplFirstChar
import br.net.buzu.pplspec.lang.pplIsIgnored

fun splitNode(text : String): PplNode{

    val sName = splitName(text, 0)

    val sType = splitType(text, sName.index)

    val sSize = splitSize(text, sType.index)

    val sOccurs = splitOccurs(text, sSize.index)

    val sDomain = splitDomain(text, sOccurs.index)

    val sDefaultValue = splitDefaultValue(text, sDomain.index)

    val sTags = splitTag(text, sDefaultValue.index)

    return PplNode(name=sName.part, type=sType.part, size = sSize.part,
            occurs = sOccurs.part, domain = sDomain.part, defaultValue = sDefaultValue.part,
            tags = sTags.part, children = sType.children)
}

fun splitNodes(text: String): Array<PplNode>{
    val list= ArrayList<PplNode>()

    return list.toArray() as Array<PplNode>
}

fun splitName(text: String, beginIndex: Int): Split{

    return Split(23)
}

fun splitType(text: String, beginIndex: Int): TypeSplit{
    val children: Array<PplNode> = arrayOf()
    return TypeSplit(23, children=children)
}

fun splitSize(text: String, beginIndex: Int): Split{

    return Split(23)
}

fun splitOccurs(text: String, beginIndex: Int): Split{

    return Split(23)
}

fun splitDomain(text: String, beginIndex: Int): Split{

    return Split(23)
}

fun splitDefaultValue(text: String, beginIndex: Int): Split{
    val firstChar = pplFirstChar(text, beginIndex)
    var endIndex = firstChar
    for (i in firstChar until text.length) {
        if (!pplIsIgnored(text[i])) {
            endIndex = i
        }
    }
    return Split(endIndex, text.substring(firstChar, endIndex + 1))
}

fun splitTag(text: String, beginIndex: Int): Split{

    return Split(beginIndex)
}

data class Split (val index: Int, val part: String= EMPTY)

data class TypeSplit (val index: Int, val part: String= EMPTY, val children: Array<PplNode>)

