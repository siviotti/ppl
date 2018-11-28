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

import br.net.buzu.pplspec.api.PayloadParser

/**
 * Imutable informations about the target class on a parsing operation.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface Metaclass : Metadata {

    val isArray: Boolean

    val isCollection: Boolean

    val isPrimitive: Boolean

    val isEnum: Boolean

    val isPplSerializable: Boolean

    val isOldDate: Boolean

    // **************************************************
    // API
    // **************************************************

    fun fieldName(): String

    fun fieldType(): Class<*>

    fun elementType(): Class<*>

    fun parserType(): Class<out PayloadParser>

    fun hasCustomParser(): Boolean

    fun match(elementClass: Class<*>): Boolean

    fun getChildByName(childName: String): Metaclass

    fun getValueSize(value: Any): Int

    operator fun get(`object`: Any): Any

    operator fun set(`object`: Any, param: Any)

}
