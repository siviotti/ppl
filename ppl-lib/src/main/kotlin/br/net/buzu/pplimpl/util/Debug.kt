package br.net.buzu.pplimpl.util

import br.net.buzu.pplspec.lang.PATH_SEP

fun <T> asTree(node: T, getChildren: (T) -> List<T>, nodeToString: (T) -> String= {it.toString()}, level: Int = 0): String {
    val sb = StringBuilder()
    for (i in 0 until level) sb.append(PATH_SEP)
    sb.append(nodeToString(node)).append("\n")
    getChildren(node).forEach { child -> sb.append(asTree(child, getChildren, nodeToString, level + 1)) }
    return sb.toString()
}

