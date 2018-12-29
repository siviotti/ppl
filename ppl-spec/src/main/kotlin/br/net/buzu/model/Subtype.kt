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

import br.net.buzu.lang.DEFAULT_SIZE
import br.net.buzu.lang.DEFAULT_TYPE
import br.net.buzu.lang.EMPTY
import java.util.*

/**
 * Subtype List.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class Subtype(val dataType: DataType, val id: String = "" + dataType.id, val fixedSize: Int = DEFAULT_SIZE, val isFullLength: Boolean = false) {

    // DataType CHAR

    /** Fixed-size Text  */
    CHAR(DataType.CHAR),

    // DataType STRING

    /** Text  */
    STRING(DataType.STRING),

    // DataType NUMBER

    /** Number  */
    NUMBER(DataType.NUMBER),

    // DataType INTEGER

    /** Integer 32 Bits  */
    INTEGER(DataType.INTEGER),

    // DataType LONG

    /** Integer 64 Bits  */
    LONG(DataType.LONG),

    // DataType BOOLEAN

    /** Boolean based on 'true' or 'false'  */
    BOOLEAN(DataType.BOOLEAN, "" + DataType.BOOLEAN.id, 5, false),
    /** Boolean based on 1 or 0  */
    BOZ(DataType.BOOLEAN, "BOZ", 1, true),
    /** Boolean based on 'T' or 'F'  */
    BTF(DataType.BOOLEAN, "BTF", 1, true),
    /** Boolean based on 'Y' or 'N'  */
    BYN(DataType.BOOLEAN, "BYN", 1, true),
    /** Boolean based on 'S' or 'N'  */
    BSN(DataType.BOOLEAN, "BSN", 1, true),

    // DataType DATE

    /** Date yyyymmdd  */
    DATE(DataType.DATE, "" + DataType.DATE.id, 8, true),
    /** Iso Date 8601: yyyy-mm-dd  */
    ISO_DATE(DataType.DATE, "" + DataType.DATE.id + "I", 10, true),
    /** ISO Offset Date yyyy-mm-dd+hh:mm  - UTC (Coordinated Universal Time)*/
    UTC_DATE(DataType.DATE, "" + DataType.DATE.id + "U", 16, true),

    // DataType TIME

    /** Default Time hhmmss  */
    TIME(DataType.TIME, "" + DataType.TIME.id, 6, true),
    /** Time hhmmssMMM  */
    TIME_AND_MILLIS(DataType.TIME, "" + DataType.TIME.id + "m", 13, true),
    /** ISO Time hh:mm:ss  */
    ISO_TIME(DataType.TIME, "" + DataType.TIME.id + "I", 8, true),
    /** ISO Offset Time hh:mm:ss+hh:mm - - UTC (Coordinated Universal Time)*/
    UTC_TIME(DataType.TIME, "" + DataType.TIME.id + "U", 14, true),

    // DataType TIMESTAMP

    /** Default Timestamp yyyymmddhhmmss  */
    TIMESTAMP(DataType.TIMESTAMP, "" + DataType.TIMESTAMP.id, 14, true),
    /** Default Timestamp yyyymmddhhmmssMMM  */
    TIMESTAMP_AND_MILLIS(DataType.TIMESTAMP, "" + DataType.TIMESTAMP.id + "m", 17, true),
    /** ISO Timestamp yyyy-mm-ddThh:mm:ss  */
    ISO_TIMESTAMP(DataType.TIMESTAMP, "" + DataType.TIMESTAMP.id + "I", 19, true),
    /** ISO Offset Timestamp yyyy-mm-ddThh:mm:ss+hh:mm  - UTC (Coordinated Universal Time)*/
    UTC_TIMESTAMP(DataType.TIMESTAMP, "" + DataType.TIMESTAMP.id + "U", 25, true),


    /** Default Period  */
    PERIOD(DataType.PERIOD),

    // DataType COMPLEX

    /** Tipo complexo porém plano, ou seja, sem ocorrências indefinidas  */
    MAP(DataType.MAP),

    /** Tipo complexo porém plano, ou seja, sem ocorrências indefinidas  */
    OBJ(DataType.OBJECT),

    @Deprecated("this instance represents a null Subtype")
    EMPTY_SUBTYPE(DataType.OBJECT, "_EMPTY_SUBTYPE", -1, false);

    val isFixedSizeType: Boolean
        get() = SizeType.FIXED == dataType.sizeType

    fun fixedSize(): Int {
        return fixedSize
    }

    companion object {

        val DEFAULT_SINGLE: Subtype
        val DEFAULT_COMPLEX = OBJ

        private val INTERNAL_MAP = TreeMap<String, Subtype>()

        init {
            for (subtype in values()) {
                INTERNAL_MAP[subtype.id] = subtype
            }

            DEFAULT_SINGLE = of("" + DEFAULT_TYPE)
            INTERNAL_MAP[EMPTY] = DEFAULT_SINGLE
        }

        fun of(id: String): Subtype {
            val tmp: Subtype? = INTERNAL_MAP[id]
            if (tmp != null) {
                return tmp
            }
            throw IllegalArgumentException("The id $id correspond to none Subtype")
        }
    }

}
