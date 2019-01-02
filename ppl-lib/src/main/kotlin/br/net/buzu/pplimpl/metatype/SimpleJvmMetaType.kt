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

import br.net.buzu.model.*
import br.net.buzu.pplimpl.core.arrayParse
import br.net.buzu.pplimpl.core.arraySerialize

/**
 * MetaType for simple structures (not complex). This class handle "ATOMIC" and 'ARRAY" kinds.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
open class SimpleJvmMetaType(fieldPath: String, fieldName: String, metaInfo: MetaInfo, children: List<MetaType>,
                             treeIndex: Int, typeAdapter: TypeAdapter, valueMapper: ValueMapper)
    : AbstractMetaType(fieldPath, fieldName, metaInfo, children, treeIndex, typeAdapter, valueMapper) {

    override fun doParse(text: String, metadata: StaticMetadata): Any? = arrayParse(text, metadata, typeAdapter, valueMapper)

    override fun doSerialize(value: Any?, metadata: StaticMetadata): String = arraySerialize(value, metadata, typeAdapter, valueMapper)
}