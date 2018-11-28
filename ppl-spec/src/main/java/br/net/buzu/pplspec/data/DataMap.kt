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
package br.net.buzu.pplspec.data

/**
 * Plain Data Map
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface DataMap {

    /**
     * Returns if a Data exists based on ID.
     *
     * @param id
     * The id
     * @return `true` if exists, `false` if not.
     */
    fun exists(id: String): Boolean

    /**
     * Returns the Data relative to the ID.
     *
     * @param id
     * the Data ID.
     * @return The instance domainOf `Data` corresponding to ID or an
     * IllegalArgumentException if not found.
     * @throws IllegalArgumentException
     */
    operator fun <T> get(id: String): Data<T>

    /**
     * Sets the value domainOf an existing Data.
     *
     * @param id
     * The ID
     * @param value
     * The new value.
     * @return The internal value after "set".
     */
    operator fun <T> set(id: String, value: T): T

}
