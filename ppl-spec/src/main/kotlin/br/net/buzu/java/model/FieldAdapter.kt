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
package br.net.buzu.java.model

/**
 * Basic tree node for type parsing.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
abstract class FieldAdapter(val fieldPath: String, val fieldName: String, val fieldType: Class<*>,
                            val elementType: Class<*>, val metaInfo: MetaInfo,
                            val children: List<FieldAdapter>, val treeIndex: Int) {

    val hasChildren: Boolean = children.isNotEmpty()
    val isArray: Boolean = fieldType.isArray
    val isCollection: Boolean = Collection::class.java.isAssignableFrom(fieldType)
    val multiple: Boolean

    init {
        multiple = isArray || isCollection
    }

    fun nodeCount(): Int {
        return if (children.isEmpty()) 1 else {
            var count = 1
            for (it in children) count += it.nodeCount()
            count
        }
    }

    abstract fun getFieldValue(parentObject: Any): Any?

    abstract fun setFieldValue(parentObject: Any, paramValue: Any?)

    abstract fun getValueSize(value: Any?): Int

    override fun toString(): String =  "[$treeIndex] $fieldPath: ${fieldType.simpleName}<${elementType.simpleName}> $metaInfo"
}
