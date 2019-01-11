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
package br.net.buzu.pplspec.model

/**
 * Abstraction over a field type. If this type is complex the MetaType has children (another MetaTypes).
 * So, the MetaType is a Node of a MetaTypes tree.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface MetaType {

    /** Complete field path (including the field name) like 'order.customer.addresses.street' */
    val fullName: String

    /** */
    val metaInfo: MetaInfo

    val treeIndex: Int

    val children: List<MetaType>

    val typeAdapter: TypeAdapter

    val valueMapper: ValueMapper

    val hasChildren: Boolean

    fun treeSize(): Int

    fun getChildByMetaName(metaName: String): MetaType

    fun valueMapperOf(metadataInfo: MetaInfo): ValueMapper

}
