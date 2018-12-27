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

import br.net.buzu.lang.*

/**
 * Fundamental data types of PPL. A DataType can define many SubTypes.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class DataType(val id: Char, val group: DataGroup, val isComplex: Boolean = DataGroup.COMPLEX == group,
                    val sizeType: SizeType = group.sizeType, val align: Align = group.align,
                    val fillChar: Char = group.fillChar, val nullChar: Char = group.nullChar) {

    // SINGLE (Fields)

    // Text

    /** Static Characteres  */
    CHAR(TYPE_CHAR, DataGroup.TEXT, false, SizeType.CUSTOM, Align.LEFT, ' ', '|'),
    /** Dynamic Characteres  */
    STRING(TYPE_STRING, DataGroup.TEXT),

    // Numeric

    /** Number  */
    NUMBER(TYPE_NUMBER, DataGroup.NUMERIC),
    /** Integer (32bits)  */
    INTEGER(TYPE_INTEGER, DataGroup.NUMERIC),
    /** Long Integer (64bits)  */
    LONG(TYPE_LONG, DataGroup.NUMERIC),
    /** Hexadecima  */
    HEXA(TYPE_HEXA, DataGroup.NUMERIC),

    // Boolean

    /** Boolean  */
    BOOLEAN(TYPE_BOOLEAN, DataGroup.BOOLEAN),

    // Time

    /** Date  */
    DATE(TYPE_DATE, DataGroup.TIME),
    /** Time  */
    TIME(TYPE_TIME, DataGroup.TIME),
    /** Timestamp / DateTime  */
    TIMESTAMP(TYPE_TIMESTAMP, DataGroup.TIME),
    /** Period / Duration  */
    PERIOD(TYPE_PERIOD, DataGroup.TIME),

    // COMPLEX (Class)

    /** Key/Value Object  */
    MAP(TYPE_MAP, DataGroup.COMPLEX),
    /** Hierarquical Complex Object  */
    OBJECT(TYPE_OBJECT, DataGroup.COMPLEX);

    companion object {

        /**
         * Returns the instance domainOf Type
         *
         * @param charId
         * The Charactere ID
         * @return A instance domainOf `Type` or `null` .
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
