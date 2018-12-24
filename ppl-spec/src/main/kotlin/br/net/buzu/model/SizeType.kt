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

/**
 * Lista com as estratégias de definição de tamanho dos campos.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class SizeType(val isFixed: Boolean) {

    /** Fixed size. Cannot be defined */
    FIXED(true),
    /** Custom size. Must be defined, but has a default size.  */
    CUSTOM(false),
    /** Size derived from a sum of others sizes. Used on complex types.*/
    SUM(true)

}
