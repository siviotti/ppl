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

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Metadata

/**
 * No complex metadata.
 *
 * @author Douglas Siviotti
 * @since 1.0 (15/05/2017)
 */
open class SimpleMetadata(metaInfo: MetaInfo) : BasicMetadata(metaInfo) {

    override fun <T: Metadata> children(): List<T> {
        throw UnsupportedOperationException(SIMPLE_METADATA_HAS_NO_CHILDREN)
    }

    override fun hasChildren(): Boolean {
        return false
    }


    override fun isStatic(): Boolean {
        return false
    }

    companion object {
        private const val SIMPLE_METADATA_HAS_NO_CHILDREN = "SimpleMetadata has no children!"
    }
}
