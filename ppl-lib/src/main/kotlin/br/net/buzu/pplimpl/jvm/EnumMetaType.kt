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
package br.net.buzu.pplimpl.jvm

import br.net.buzu.exception.PplParseException
import br.net.buzu.model.*
import java.lang.reflect.Field

/**
 * MetaType for simple structures (not complex) when the type is a Enum. This class handle "ATOMIC" and 'ARRAY" kinds.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class EnumSimpleJvmMetaType(fieldPath: String, fieldName: String, metaInfo: MetaInfo, children: List<MetaType>,
                            treeIndex: Int, typeAdapter: TypeAdapter, valueMapper: ValueMapper)
    : SimpleJvmMetaType(fieldPath, fieldName, metaInfo, children, treeIndex, typeAdapter, valueMapper) {


    override fun parseAtomic(text: String, metadata: StaticMetadata): Any?= typeAdapter.enumConstantToValue(text)

    override fun serializeAtomic(value: Any?, metadata: StaticMetadata): String {
        return if (value != null) (value as Enum<*>).name else serializeNull(metadata.info())
    }
}
