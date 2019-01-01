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

import br.net.buzu.model.*
import java.lang.reflect.Field

/**
 * MetaType specific for "ATOMIC" kind.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class AtomicJvmMetaType(fieldPath: String, fieldName: String, metaInfo: MetaInfo, children: List<MetaType>,
                        treeIndex: Int, typeAdapter: TypeAdapter, valueMapper: ValueMapper)
    : JvmMetaType(fieldPath, fieldName, metaInfo, children, treeIndex, typeAdapter, valueMapper) {

    override fun doParse(text: String, metadata: StaticMetadata): Any? = parseAtomic(text, metadata)

    override fun doSerialize(value: Any?, metadata: StaticMetadata): String = serializeAtomic(value, metadata)
}
