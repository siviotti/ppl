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

import br.net.buzu.lib.complexSize
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata

import java.util.Collections

/**
 * Metadata parent domainOf others Metadatas.
 *
 * @author Douglas Siviotti
 * @since 1.0 (15/05/2017)
 */
open class ComplexMetadata(metaInfo: MetaInfo, children: List<Metadata>) : BasicMetadata(metaInfo.update(complexSize(children), metaInfo.maxOccurs)) {

    private val children: List<Metadata>

    init {
        this.children = children.toList()
    }

    private fun complexSize(children: List<Metadata>): Int {
        var complexSize = 0
        for (child in children) {
            complexSize += child.info().size
        }
        return complexSize
    }

    override fun <T: Metadata> children(): List<T> {
        val list = mutableListOf<T>()
        for (child in children) {
            list.add(child as T)
        }
        return list
    }


    override fun hashCode(): Int {
        val prime = 31
        var result = super.hashCode()
        result = prime * result + children!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is ComplexMetadata) {
            val other = obj as ComplexMetadata?
            return super.equals(other) && children == other.children
        }
        return false
    }

    override fun hasChildren(): Boolean {
        return children != null && !children.isEmpty()
    }

    override fun isStatic(): Boolean {
        for (child in children!!) {
            if (!child.isStatic()) {
                return false
            }
        }
        return true
    }


}