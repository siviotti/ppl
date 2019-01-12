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

import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.ValueMapper

/**
 * Resolver (internal kit) to return a ValueMapper from a given subtype and a given type.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@FunctionalInterface
interface ValueMapperKit {

    /**
     * Returns the ValueMapper corresponding to the informed params.
     *
     * @param metaInfo The meta information used to create the mapper. This is the Metadata basic information.
     * @param type The type witch will be mapped.
     *
     * @return The ValueMapper instance corresponding to the given parameters.
     *
     */
    fun getMapper(metaInfo: MetaInfo, type: Class<*>): ValueMapper
}