/*
 *	This file is part of DefaultPplMapper.
 *
 *   DefaultPplMapper is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DefaultPplMapper is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with DefaultPplMapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.StaticMetadata
import br.net.buzu.pplspec.model.StaticStructure

/**
 * @author Douglas Siviotti
 * @since 1.0
 */
class ComplexStaticMetadata(metaInfo: MetaInfo, children: List<Metadata>) : ComplexMetadata(metaInfo, children), StaticMetadata {

    private val serialSize: Int

    init {
        checkStaticInfo(info())
        var tmp = 0
        for (child in children) {
            if (child !is StaticStructure) {
                throw IllegalArgumentException(
                        INVALID_STATIC_CHILD + child.info() + " is not a " + StaticStructure::class.java.simpleName)
            }
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
        if (obj is ComplexStaticMetadata) {
            val other = obj as ComplexStaticMetadata?
            return super.equals(other) && serialSize == other.serialSize
        }
        return false
    }

    override fun serialMaxSize(): Int {
        return serialSize
    }

    companion object {
        internal const val INVALID_STATIC_CHILD = "Invalid Static child:"
    }
}
