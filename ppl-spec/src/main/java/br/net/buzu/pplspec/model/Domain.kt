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

import java.util.ArrayList
import java.util.Collections

import br.net.buzu.pplspec.lang.Syntax
import br.net.buzu.pplspec.lang.Token

/**
 * Represents the Domain Value of a Metadata.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class Domain protected constructor(private val name: String, items: List<DomainItem>?) : Nameable {

    private val items: List<DomainItem>

    val isEmpty: Boolean
        get() = items.isEmpty()

    init {
        this.items = if (items != null)
            Collections.unmodifiableList(items)
        else
            Collections.unmodifiableList(ArrayList())
    }

    override fun name(): String {
        return name
    }

    fun containsValue(value: String): Boolean {
        for (item in items) {
            if (item.value() == value) {
                return true
            }
        }
        return false
    }

    fun items(): List<DomainItem> {
        return items
    }

    fun asSerial(): String {
        if (items.isEmpty()) {
            return Syntax.EMPTY
        }
        val sb = StringBuilder()
        sb.append(Token.DOMAIN_BEGIN)
        for (item in items) {
            sb.append(Token.VALUE_BEGIN).append(item.asSerial()).append(Token.VALUE_END).append(Token.DOMAIN_SEPARATOR)
        }
        sb.deleteCharAt(sb.length - 1) // remove last
        sb.append(Token.DOMAIN_END)
        return sb.toString()
    }

    override fun hashCode(): Int {
        return name.hashCode() * 31 + items.hashCode()
    }

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

    fun equalsItems(other: Domain): Boolean {
        return items == other.items
    }

    override fun toString(): String {
        return StringBuilder().append(name).append(asSerial()).toString()
    }

    companion object {

        val EMPTY = Domain(Syntax.EMPTY, null)

        fun list(vararg array: String): List<DomainItem> {
            val list = ArrayList<DomainItem>()
            for (i in array.indices) {
                list.add(DomainItem.parse(array[i]))
            }
            return list
        }

        fun createItems(stringList: List<String>): List<DomainItem> {
            val list = ArrayList<DomainItem>()
            for (item in stringList) {
                list.add(DomainItem.parse(item))
            }
            return list
        }

        fun create(name: String, items: List<DomainItem>): Domain {
            return Domain(name, items)
        }

        fun of(vararg array: String): Domain {
            return create(Syntax.EMPTY, list(*array))
        }
    }

}
