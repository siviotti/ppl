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
import br.net.buzu.pplspec.model.StaticMetadata

/**
 * Implementation of StaticMetadata based on SimpleMetadata
 *
 * @author Douglas Siviotti
 */
class SimpleStaticMetadata(metaInfo: MetaInfo) : SimpleMetadata(metaInfo), StaticMetadata {

    private val serialMaxSize: Int

    init {
        checkStaticInfo(metaInfo)
        serialMaxSize = metaInfo.size * metaInfo.maxOccurs
    }

    override fun isStatic(): Boolean =true

    override fun serialMaxSize(): Int =serialMaxSize

    override fun hashCode(): Int {
        val prime = 31
        var result = super.hashCode()
        result = prime * result + serialMaxSize
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is SimpleStaticMetadata) {
            val other = obj as SimpleStaticMetadata?
            return super.equals(other) && serialMaxSize == other.serialMaxSize
        }
        return false
    }

}
