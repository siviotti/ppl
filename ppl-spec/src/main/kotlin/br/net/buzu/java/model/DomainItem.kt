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

import br.net.buzu.java.lang.VALUE_LABEL_SEPARATOR

/**
 * Generic implementation to `LabelValue`.
 *
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585
 * @since 10 de abr de 2018 - Construção da Duimp (Release 1)
 * @see LabeledValue
 */
data class DomainItem @JvmOverloads
constructor(private val value: String, private val label: String?=null) : LabeledValue, Comparable<DomainItem> {


    override fun value(): String = value
    override fun label(): String? = label

    fun intValue(): Int = Integer.parseInt(value)

    override fun hasLabel(): Boolean =label != null

    override fun hashCode(): Int {
        return value.hashCode() * 31 + (label?.hashCode() ?: 0)
    }

    fun asSerial(): String =if (hasLabel()) value + VALUE_LABEL_SEPARATOR + label else value

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


}
