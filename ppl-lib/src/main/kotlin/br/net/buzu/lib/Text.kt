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

import br.net.buzu.model.Align

/**
 * Completa ou corta uma String de forma que ela fique com um tamanho fixo
 * definido como parâmetro.
 *
 * @param align
 * O tipo de alinhamento (direita ou esquerda).
 *
 * @param original
 * A Srtring original.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @param c
 * O caractere que preenche possíveis buracos.
 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
 * tamanho.
 */
fun fit(align: Align, original: String, finalSize: Int, c: Char): String {
    val l = original.length
    if (l == finalSize) {
        return original
    }
    return if (l < finalSize) fill(align, original, finalSize, c) else cut(align, original, finalSize)
}

/**
 * Completa ou corta uma String de forma que ela fique com um tamanho fixo
 * definido como parâmetro com alinhamento á DIREITA.
 *
 *
 * @param original
 * A Srtring original.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @param c
 * O caractere que preenche possíveis buracos.
 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
 * tamanho.
 */
fun rightFit(original: String, finalSize: Int, c: Char): String {
    if (original.length == finalSize) {
        return original
    }
    return if (original.length < finalSize) rightFill(original, finalSize, c) else rightCut(original, finalSize)
}

/**
 * Completa ou corta uma String de forma que ela fique com um tamanho fixo
 * definido como parâmetro com alinhamento á ESQUERDA.
 *
 *
 * @param original
 * A Srtring original.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @param c
 * O caractere que preenche possíveis buracos.
 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
 * tamanho.
 */
fun leftFit(original: String, finalSize: Int, c: Char): String {
    if (original.length == finalSize) {
        return original
    }
    return if (original.length < finalSize) leftFill(original, finalSize, c) else leftCut(original, finalSize)
}

/**
 * Corta uma String de forma que ela fique com um tamanho fixo definido como
 * parâmetro.
 *
 * @param align
 * O tipo de alinhamento (direita ou esquerda).
 *
 * @param original
 * A Srtring original.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @return A String no tamanho certo, preenchida ou cortada de acordo com o
 * tamanho.
 */
fun cut(align: Align, original: String, finalSize: Int): String {
    return if (Align.RIGHT == align) leftCut(original, finalSize) else rightCut(original, finalSize)
}

/**
 * Corta uma String à direita até que ela fique no tamanho final definido
 * como parâmetro.
 *
 * @param original
 * A String original que será cortada.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @return A String cortada e com o tamanho correto.
 */
fun leftCut(original: String, finalSize: Int): String {
    return original.substring(original.length - finalSize, original.length)
}

/**
 * Corta uma String à direita até que ela fique no tamanho final definido
 * como parâmetro.
 *
 * @param original
 * A String original que será cortada.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @return A String cortada e com o tamanho correto.
 */
fun rightCut(original: String, finalSize: Int): String {
    return original.substring(0, finalSize)
}

/**
 * Completa uma String de forma que ela fique com um tamanho fixo definido
 * como parâmetro.
 *
 * @param align
 * O tipo de alinhamento (direita ou esquerda).
 *
 * @param original
 * A Srtring original.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @param c
 * O caractere que preenche possíveis buracos.
 * @return A String no tamanho certo, preenchida de acordo com o tamanho.
 */
fun fill(align: Align, original: String, finalSize: Int, c: Char): String {
    return if (Align.RIGHT == align) leftFill(original, finalSize, c) else rightFill(original, finalSize, c)
}

/**
 * Completa uma String á direita de forma que ela fique com um tamanho fixo
 * definido como parâmetro.
 *
 * @param original
 * A Srtring original.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @param c
 * O caractere que preenche possíveis buracos.
 * @return A String no tamanho certo, preenchida de acordo com o tamanho.
 */
fun rightFill(original: String, finalSize: Int, c: Char): String {
    val sb = StringBuilder(original)
    for (i in original.length until finalSize) {
        sb.append(c)
    }
    return sb.toString()
}

/**
 * Completa uma String à esquerda de forma que ela fique com um tamanho fixo
 * definido como parâmetro.
 *
 * @param original
 * A Srtring original.
 * @param finalSize
 * O tamanho final que a String deve ter.
 * @param c
 * O caractere que preenche possíveis buracos.
 * @return A String no tamanho certo, preenchida de acordo com o tamanho.
 */
fun leftFill(original: String, finalSize: Int, c: Char): String {
    val sb = StringBuilder()
    for (i in original.length until finalSize) {
        sb.append(c)
    }
    sb.append(original)
    return sb.toString()
}
