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

import br.net.buzu.pplspec.lang.Token

/**
 * Fundamental data types of PPL. A SuperType can define many SubTypes.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class DataType private constructor(private val id: Char, private val group: DataGroup, val isComplex: Boolean = DataGroup.COMPLEX == group, private val sizeType: SizeType = group.sizeType(), private val align: Align = group.align(), private val fillChar: Char = group.fillChar(),
                                        private val nullChar: Char = group.nullChar()) {

    // SINGLE (Fields)

    // Text

    /** Static Characteres  */
    CHAR(Token.TYPE_CHAR, DataGroup.TEXT, false, SizeType.CUSTOM, Align.LEFT, ' ', '|'),
    /** Dynamic Characteres  */
    STRING(Token.TYPE_STRING, DataGroup.TEXT),

    // Numeric

    /** Number  */
    NUMBER(Token.TYPE_NUMBER, DataGroup.NUMERIC),
    /** Integer (32bits)  */
    INTEGER(Token.TYPE_INTEGER, DataGroup.NUMERIC),
    /** Long Integer (64bits)  */
    LONG(Token.TYPE_LONG, DataGroup.NUMERIC),
    /** Hexadecima  */
    HEXA(Token.TYPE_HEXA, DataGroup.NUMERIC),

    // Boolean

    /** Boolean  */
    BOOLEAN(Token.TYPE_BOOLEAN, DataGroup.BOOLEAN),

    // Time

    /** Date  */
    DATE(Token.TYPE_DATE, DataGroup.TIME),
    /** Time  */
    TIME(Token.TYPE_TIME, DataGroup.TIME),
    /** Timestamp / DateTime  */
    TIMESTAMP(Token.TYPE_TIMESTAMP, DataGroup.TIME),
    /** Period / Duration  */
    PERIOD(Token.TYPE_PERIOD, DataGroup.TIME),

    // COMPLEX (Class)

    /** Plain Complex Object  */
    FLAT(Token.TYPE_FLAT, DataGroup.COMPLEX),
    /** Key/Value Object  */
    MAP(Token.TYPE_MAP, DataGroup.COMPLEX),
    /** Hierarquical Complex Object  */
    OBJECT(Token.TYPE_OBJECT, DataGroup.COMPLEX);

    fun id(): Char {
        return id
    }

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

    fun group(): DataGroup {
        return group
    }

    companion object {

        /**
         * Returns the instance of Type
         *
         * @param charId
         * The Charactere ID
         * @return A instance of `Type` or `null` .
         */
        operator fun get(charId: Char): DataType? {
            for (type in values()) {
                if (type.id == charId) {
                    return type
                }
            }
            return null
        }
    }

}
