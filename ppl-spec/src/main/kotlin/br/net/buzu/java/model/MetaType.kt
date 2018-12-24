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

import java.lang.IllegalArgumentException

/**
 * Basic tree node for type parsing.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
abstract class MetaType(val fieldFullName: String, val metaName: String, val metaInfo: MetaInfo,
                        val treeIndex: Int, val adapter: TypeAdapter<*>, val children: List<MetaType>) {

    val hasChildren: Boolean = children.isNotEmpty()
    private val childrenMap = children.map { it.metaName to it }.toMap()


    fun nodeCount(): Int {
        return if (children.isEmpty()) 1 else {
            var count = 1
            for (it in children) count += it.nodeCount()
            count
        }
    }

    open fun getChildByMetaName(name: String): MetaType = childrenMap[name]
            ?: throw IllegalArgumentException("Child fieldAdapter '$name' not found at ${toString()}. Children:$children")



    abstract fun getFieldValue(parentObject: Any): Any?

    abstract fun setFieldValue(parentObject: Any, paramValue: Any?)

    abstract fun getValueSize(value: Any?): Int

    abstract fun asSingleObject(positionalText: String): Any?

    abstract fun asStringFromNotNull(value: Any): String

    abstract fun maxArrayToValue(array: Array<Any?>): Any

    abstract fun createAndFillArray(size: Int): Array<Any?>

    abstract fun valueToMaxArray(value: Any?, size: Int): Array<Any?>

    abstract fun valueToArray(value: Any?): Array<Any?>

}
