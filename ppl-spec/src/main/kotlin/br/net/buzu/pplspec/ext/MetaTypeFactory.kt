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
package br.net.buzu.pplspec.ext

import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.MetaType
import br.net.buzu.pplspec.model.TypeAdapter
import br.net.buzu.pplspec.model.ValueMapper

/**
 * Specification of a MetaType Factory
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@FunctionalInterface
interface MetaTypeFactory {

    /**
     * Creates a instance of MetaType from given parameters.
     * @param fullName The complete field name including the parent field names like 'order.customer.addresses.street'
     * @param metaInfo The PPL basic information (name, type, size, occurs, default, domain etc)
     * @param children The MetaType children nodes
     * @param treeIndex The index of this element at the tree ( if is root = 0)
     * @param typeAdapter The TypeAdapter corresponding to the field type
     * @param valueMapper The ValueMapper corresponding to the field type and the Subtype at MetaInfo.
     * @param kit The Kit/Resolver used if the subtype of MetaType is not the same of Metadata.
     * This kit is used to create the correct ValueMapper to the Metadata subtype.
     *
     * @return The created instance of MetaType
     */
    fun create(fullName: String, metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int,
               typeAdapter: TypeAdapter, valueMapper: ValueMapper, kit: ValueMapperKit): MetaType
}
