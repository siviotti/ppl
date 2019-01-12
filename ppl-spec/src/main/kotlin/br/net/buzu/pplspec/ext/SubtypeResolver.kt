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
package br.net.buzu.pplspec.ext

import br.net.buzu.pplspec.model.Subtype

/**
 * Resolver to returns the default PPL Subtype corresponding to the platform type.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@FunctionalInterface
interface SubtypeResolver {

    /**
     * Resolves and returns a Subtype from a given type. This resolver is used to create MetaTypes and TypeAdapters
     * returning the default subtypes before create a Mapper that uses the Metadata information.
     * In this case is possible that the subtype be replaced.
     *
     * @param type The type that must be associated to a Subtype.
     *
     * @return The Subtype corresponding to the informed type.
     */
    fun resolve(type: Class<*>): Subtype
}