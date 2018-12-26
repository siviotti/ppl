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
package br.net.buzu.model

/**
 * Basic tree node for type parsing/serialization
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
abstract class MetaType(val fieldFullName: String, val metaName: String, val metaInfo: MetaInfo,
                        val treeIndex: Int, val valueMapper: ValueMapper, val children: List<MetaType>) : TypeAdapter{

    abstract val hasChildren: Boolean

    // Parse and Serialization API

    abstract fun parse(text: String, metadata: StaticMetadata): Any?

    abstract fun serialize(value: Any?, metadata:StaticMetadata): String

    // Metadata Load API

    abstract fun nodeCount(): Int

    abstract fun getValueSize(value: Any?): Int

    abstract fun valueToArray(value: Any?): Array<Any?>

}
