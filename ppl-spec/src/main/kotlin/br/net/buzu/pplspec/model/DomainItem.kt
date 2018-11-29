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

import br.net.buzu.pplspec.lang.LABEL_VALUE

/**
 * Generic implementation to `LabelValue`.
 *
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585
 * @since 10 de abr de 2018 - Construção da Duimp (Release 1)
 * @see LabeledValue
 */
class DomainItem
/**
 * Complete constructor.
 *
 * @param value
 * The internal value.
 * @param label
 * The correspondent label.
 */
@JvmOverloads constructor(private val value: String, private val label: String? = null) : LabeledValue, Comparable<DomainItem> {

    init {
        if (value == null) {
            throw NullPointerException("'value' cannot be null!")
        }
    }


    override fun value(): String {
        return value
    }

    fun intValue(): Int {
        return Integer.parseInt(value)
    }

    override fun label(): String? {
        return label
    }

    override fun hasLabel(): Boolean {
        return label != null
    }

    override fun hashCode(): Int {
        return value.hashCode() * 31 + (label?.hashCode() ?: 0)
    }

    fun asSerial(): String {
        return if (hasLabel()) value + LABEL_VALUE + label else value
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        return if (obj is DomainItem) {
            asSerial() == obj.asSerial()
        } else false
    }

    override fun toString(): String {
        return asSerial()
    }

    override fun compareTo(o: DomainItem): Int {
        return value.compareTo(o.value)
    }

    companion object {

        fun parse(text: String): DomainItem {
            val pos = text.indexOf(LABEL_VALUE)
            if (pos < 0) {
                return DomainItem(text)
            }
            val value = text.substring(0, pos)
            val label = text.substring(pos + 1, text.length)
            return DomainItem(value, label)
        }
    }

}
