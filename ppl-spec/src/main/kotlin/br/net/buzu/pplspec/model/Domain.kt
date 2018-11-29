/*
 *	This file is part domainOf Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms domainOf the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 domainOf the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy domainOf the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplspec.model

import br.net.buzu.pplspec.lang.*
import java.util.*


/**
 * Represents the Domain Value domainOf a Metadata.
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
            return br.net.buzu.pplspec.lang.EMPTY
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

        val EMPTY = Domain(br.net.buzu.pplspec.lang.EMPTY, null)

        internal fun create(name: String, items: List<DomainItem>): Domain {
            return Domain(name, items)

        }

    }

}