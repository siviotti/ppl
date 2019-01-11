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

import br.net.buzu.pplspec.ext.MetaTypeFactory
import br.net.buzu.pplspec.ext.ValueMapperKit
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.MetaType
import br.net.buzu.pplspec.model.TypeAdapter
import br.net.buzu.pplspec.model.ValueMapper

/**
 * Generic implementation of MetaType independent of language/platform.
 *
 * @author Douglas Siviotti
 * @since 1.0
 * @see MetaType
 */
class GenericMetaType(override val fullName: String, override val metaInfo: MetaInfo,
                      override val children: List<MetaType>, override val treeIndex: Int,
                      override val typeAdapter: TypeAdapter, override val valueMapper: ValueMapper, val kit: ValueMapperKit)
    : MetaType {

    override val hasChildren = children.isNotEmpty()
    private val childrenMap = children.map { it.metaInfo.name to it }.toMap()

    override fun getChildByMetaName(metaName: String): MetaType = childrenMap[metaName]
            ?: throw IllegalArgumentException("Child metaType '$metaName' not found at ${toString()}. Children:$children")

    override fun treeSize(): Int {
        return if (children.isEmpty()) 1 else {
            var count = 1
            for (it in children) count += it.treeSize()
            count
        }
    }

    override fun toString(): String = "[$treeIndex] $fullName: $typeAdapter (${metaInfo.name}) $valueMapper $metaInfo"

    override fun valueMapperOf(metadataInfo: MetaInfo): ValueMapper {
        return if (metadataInfo.subtype == metaInfo.subtype) valueMapper
        else typeAdapter.getValueMapper(metadataInfo, kit)
    }
}

object GenericMetaTypeFactory: MetaTypeFactory {
    override fun create(fullName: String, metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int,
                        typeAdapter: TypeAdapter, valueMapper: ValueMapper, kit: ValueMapperKit): MetaType {
        return GenericMetaType(fullName, metaInfo, children, treeIndex, typeAdapter, valueMapper, kit)
    }


}