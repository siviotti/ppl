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
package br.net.buzu.pplspec.data

import br.net.buzu.pplspec.model.Metadata

/**
 * Abstraction over an identifyable data element.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface Data<T> {

    /**
     * The data value.
     *
     * @return The value.
     */
    val value: T

    /**
     * The ID.
     *
     * @return The String ID.
     */
    fun id(): String

    fun setValue(value: Any)


    fun metadata(): Metadata
}
