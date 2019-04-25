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

import br.net.buzu.pplspec.ext.MetadataFactory
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.StaticStructure

/**
 * Generic implementation of  MetadataFactory
 *
 * @author Douglas Siviotti
 * @since 1.0
 * @see MetadataFactory
 */
object GenericMetadataFactory : MetadataFactory {
    override fun create(metaInfo: MetaInfo, children: List<Metadata>): Metadata {
        return if (children.isEmpty())
            if (metaInfo.isStatic)
                SimpleStaticMetadata(metaInfo)
            else
                SimpleMetadata(metaInfo)
        else {
            if (isStaticChildren(children))
                ComplexStaticMetadata(metaInfo, children)
            else
                ComplexMetadata(metaInfo, children)
        }
    }
    private fun isStaticChildren(children: List<Metadata>): Boolean {
        for (child in children) {
            if (child !is StaticStructure) {
                return false
            }
        }
        return true
    }
}

