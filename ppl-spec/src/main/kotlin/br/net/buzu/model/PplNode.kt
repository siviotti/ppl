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
package br.net.buzu.model

import br.net.buzu.lang.EMPTY
import br.net.buzu.lang.VAR

/**
 * Basic tree node for PPL parsing
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
data class PplNode @JvmOverloads constructor(val name: String = EMPTY, val type: String = EMPTY, val size: String = EMPTY, val occurs: String = EMPTY,
                                             val domain: String = EMPTY, val defaultValue: String = EMPTY, val tags: String = EMPTY,
                                             val children: List<PplNode> = EMPTY_CHILDREN) {

    fun isComplex() = children.isNotEmpty()

    fun isVar(): Boolean = name.isNotEmpty() && name[0] == VAR

    fun hasName(): Boolean = name.isNotEmpty()

    fun hasType(): Boolean = type.isNotEmpty()

    fun hasSize(): Boolean = size.isNotEmpty()

    fun hasOccurs(): Boolean = occurs.isNotEmpty()

    fun toTree(level: Int): String {
        val sb = StringBuilder()
        sb.append("\n")
        for (i in 0 until level) {
            sb.append(".")
        }
        sb.append("name:").append(name).append(" type:").append(type).append(" size:").append(size).append(" occurs:")
                .append(occurs).append(" domain:").append(domain).append(" defaultValue=").append(defaultValue)
                .append(" tags=").append(tags)
        for (node in children) {
            sb.append(node.toTree(level + 1))
        }
        return sb.toString()
    }

    companion object {
        val EMPTY_CHILDREN: List<PplNode> = listOf()
    }
}




