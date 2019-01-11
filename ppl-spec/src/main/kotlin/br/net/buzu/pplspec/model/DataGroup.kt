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
package br.net.buzu.pplspec.model

/**
 * List of DataType group.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class DataGroup(val sizeType: SizeType, val align: Align, val fillChar: Char, val nullChar: Char, val isDelimited: Boolean) {

    /**  */
    TEXT(SizeType.CUSTOM, Align.LEFT, ' ', ' ', true),
    /**  */
    NUMERIC(SizeType.CUSTOM, Align.RIGHT, '0', ' ', true),
    /**  */
    BOOLEAN(SizeType.FIXED, Align.LEFT, ' ', ' ', true),
    /**  */
    TIME(SizeType.FIXED, Align.LEFT, ' ', ' ', true),
    /**  */
    COMPLEX(SizeType.SUM, Align.LEFT, ' ', ' ', true);
}
