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

import br.net.buzu.lang.*


/**
 * Represents the Domain Value of a Metadata.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class Domain private constructor(private val name: String, val items: List<DomainItem> = listOf()) : Nameable {


    val isEmpty: Boolean
        get() = items.isEmpty()

    override fun name(): String = name

    fun containsValue(value: String): Boolean = items.indexOfFirst { it.value() == value } > -1

    fun items(): List<DomainItem> = items.toList()

    fun asSerial(): String {
        if (items.isEmpty()) {
            return br.net.buzu.lang.EMPTY
        }
        val sb = StringBuilder()
        sb.append(DOMAIN_BEGIN)
        for (item in items) {
            sb.append(VALUE_BEGIN).append(item.asSerial()).append(VALUE_END).append(DOMAIN_SEPARATOR)
        }
        sb.deleteCharAt(sb.length - 1) // remove last
        sb.append(DOMAIN_END)
        return sb.toString()
    }

    override fun hashCode(): Int =name.hashCode() * 31 + items.hashCode()

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (obj === this) {
            return true
        }
        if (obj is Domain) {
            val o = obj as Domain?
            return name == o!!.name && equalsItems(o)
        }
        return false
    }

    fun equalsItems(other: Domain): Boolean = items == other.items

    override fun toString(): String = "$name${asSerial()}"

    companion object {

        val EMPTY = Domain(br.net.buzu.lang.EMPTY, listOf())

        internal fun create(name: String, items: List<DomainItem>): Domain {
            return Domain(name, items)

        }

    }

}
