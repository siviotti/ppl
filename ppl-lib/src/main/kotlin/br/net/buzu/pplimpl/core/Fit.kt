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
package br.net.buzu.pplimpl.core

import br.net.buzu.pplspec.model.Align

fun fit(align: Align, original: String, finalSize: Int, c: Char): String {
    val len = original.length
    if (len == finalSize) {
        return original
    }
    return if (len < finalSize) fill(align, original, finalSize, c) else cut(align, original, finalSize)
}

fun rightFit(original: String, finalSize: Int, c: Char): String {
    if (original.length == finalSize) {
        return original
    }
    return if (original.length < finalSize) rightFill(original, finalSize, c) else rightCut(original, finalSize)
}

fun leftFit(original: String, finalSize: Int, c: Char): String {
    if (original.length == finalSize) {
        return original
    }
    return if (original.length < finalSize) leftFill(original, finalSize, c) else leftCut(original, finalSize)
}

fun cut(align: Align, original: String, finalSize: Int): String {
    return if (Align.RIGHT == align) leftCut(original, finalSize) else rightCut(original, finalSize)
}

fun leftCut(original: String, finalSize: Int): String {
    return original.substring(original.length - finalSize, original.length)
}

fun rightCut(original: String, finalSize: Int): String {
    return original.substring(0, finalSize)
}

fun fill(align: Align, original: String, finalSize: Int, c: Char): String {
    return if (Align.RIGHT == align) leftFill(original, finalSize, c) else rightFill(original, finalSize, c)
}

fun rightFill(original: String, finalSize: Int, c: Char): String {
    val sb = StringBuilder(original)
    for (i in original.length until finalSize) {
        sb.append(c)
    }
    return sb.toString()
}

fun leftFill(original: String, finalSize: Int, c: Char): String {
    val sb = StringBuilder()
    for (i in original.length until finalSize) {
        sb.append(c)
    }
    sb.append(original)
    return sb.toString()
}
