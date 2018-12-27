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
import br.net.buzu.model.MetaInfo
import br.net.buzu.model.MetaType
import br.net.buzu.model.StaticMetadata
import br.net.buzu.model.ValueMapper
import java.lang.reflect.Field

/**
 * MetaType for simple structures (not complex) when the type is a Enum. This class handle "ATOMIC" and 'ARRAY" kinds.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class EnumSimpleJvmMetaType(fieldPath: String, fieldName: String, fieldType: Class<*>, elementType: Class<*>,
                            metaInfo: MetaInfo, children: List<MetaType>, treeIndex: Int, field: Field, valueMapper: ValueMapper)
    : SimpleJvmMetaType(fieldPath, fieldName, fieldType, elementType, metaInfo, children, treeIndex, field, valueMapper) {


    override fun parseAtomic(text: String, metadata: StaticMetadata): Any? {
        val fields = getAllFields(elementType)
        val constName = text.trim { it <= ' ' }
        for (field in fields) {
            if (field.isEnumConstant() && field.getName() == constName) {
                return field.get(null)
            }
        }
        throw PplParseException("The text '" + text + "' is missing at enum " + elementType)
    }

    override fun serializeAtomic(value: Any?, metadata: StaticMetadata): String {
        return if (value != null) (value as Enum<*>).name else serializeNull(metadata.info())
    }
}
