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
package br.net.buzu.java.api

import java.lang.reflect.Field

/**
 * Strategy to decide if a field must be ignored or used.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@FunctionalInterface
interface SkipStrategy {

    /**
     * Returns if a field must be skipped or readed.
     *
     * @param field
     * The field to check.
     * @return`true` if the field must be ignored or
     * `false` if the field must be used..
     */
    fun skip(field: Field): Boolean
}
