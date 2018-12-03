/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplspec.model

/**
 * List domainOf DataType group.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class DataGroup(private val sizeType: SizeType, private val align: Align, private val fillChar: Char, private val nullChar: Char, val isDelimited: Boolean) {

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

    fun sizeType(): SizeType {
        return sizeType
    }

    fun align(): Align {
        return align
    }

    fun fillChar(): Char {
        return fillChar
    }

    fun nullChar(): Char {
        return nullChar
    }

}
