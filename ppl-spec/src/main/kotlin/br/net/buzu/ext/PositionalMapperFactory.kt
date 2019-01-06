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
package br.net.buzu.ext

import br.net.buzu.api.PositionalMapper
import br.net.buzu.model.*

/**
 * Specification of a PositionalMapper Factory
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@FunctionalInterface
interface PositionalMapperFactory {

    fun create(metadata: StaticMetadata, metaType: MetaType): PositionalMapper
}
