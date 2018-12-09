/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.metadata

import br.net.buzu.lib.checkStaticChild
import br.net.buzu.lib.checkStaticInfo
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.StaticMetadata
import br.net.buzu.pplspec.model.StaticStructure

/**
 * @author Douglas Siviotti
 * @since 1.0
 */
class ComplexStaticMetadada(metaInfo: MetaInfo, children: List<Metadata>) : ComplexMetadata(metaInfo, children), StaticMetadata {

    private val serialSize: Int

    init {
        checkStaticInfo(info())
        var tmp = 0
        for (child in children) {
            checkStaticChild(child)
            tmp += (child as StaticStructure).serialMaxSize()
        }
        this.serialSize = tmp * info().maxOccurs
    }

    override fun isStatic(): Boolean {
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = super.hashCode()
        result = prime * result + serialSize
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is ComplexStaticMetadada) {
            val other = obj as ComplexStaticMetadada?
            return super.equals(other) && serialSize == other.serialSize
        }
        return false
    }

    override fun serialMaxSize(): Int {
        return serialSize
    }

}
