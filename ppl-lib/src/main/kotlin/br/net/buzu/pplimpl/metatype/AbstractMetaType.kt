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
package br.net.buzu.pplimpl.metatype

import br.net.buzu.exception.PplParseException
import br.net.buzu.exception.PplSerializeException
import br.net.buzu.model.*


abstract class AbstractMetaType(fullName: String, metaName: String, metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, typeAdapter: TypeAdapter,
                                valueMapper: ValueMapper) :
        MetaType(fullName, metaName, metaInfo, treeIndex, children, typeAdapter, valueMapper) {

    override val hasChildren: Boolean = children.isNotEmpty()
    private val childrenMap = children.map { it.metaName to it }.toMap()

    override fun getChildByMetaName(metaName: String): MetaType = childrenMap[metaName]
            ?: throw IllegalArgumentException("Child metaType '$metaName' not found at ${toString()}. Children:$children")


    override fun nodeCount(): Int {
        return if (children.isEmpty()) 1 else {
            var count = 1
            for (it in children) count += it.nodeCount()
            count
        }
    }

    override fun toString(): String = "[$treeIndex] $fullName: $typeAdapter ($metaName) $valueMapper $metaInfo"

    override fun valueMapperOf(metadataInfo: MetaInfo): ValueMapper {
        return if (metadataInfo.subtype == metaInfo.subtype) valueMapper
        else typeAdapter.getValueMapper(metadataInfo.subtype)
    }

}