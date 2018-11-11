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
package br.net.buzu.pplspec.lang

/**
 * Language char tokens.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
object Token {

    // **************************************************
    // Basic
    // **************************************************

    val SUB_OPEN = '('
    val SUB_CLOSE = ')'
    val PLIC = '\''
    val QUOTE = '"'
    val BRACE_OPEN = '{'
    val BRACE_CLOSE = '}'
    val ARRAY_OPEN = '['
    val ARRAY_CLOSE = ']'
    val VAR = '$'

    // **************************************************
    // DataTypes
    // **************************************************

    val TYPE_CHAR = 'C'
    val TYPE_STRING = 'S'
    val TYPE_NUMBER = 'N'
    val TYPE_INTEGER = 'I'
    val TYPE_LONG = 'L'
    val TYPE_BOOLEAN = 'B'
    val TYPE_DATE = 'D'
    val TYPE_TIME = 't'
    val TYPE_TIMESTAMP = 'T'
    val TYPE_PERIOD = 'P'
    val TYPE_HEXA = 'H'

    val TYPE_FLAT = 'F'
    val TYPE_MAP = 'M'
    val TYPE_OBJECT = 'O'

    // *********************************************************************
    // Metadata
    // [NAME]:[TYPE][SIZE.SCALE]#[OCCURS_MIN-MAX][DOMAIN][DEFAULT][TAGS]
    // *********************************************************************

    /** Determines the end of 'name' part  */
    val NAME_END = ':'

    /** Decimal Separator  */
    val DECIMAL_SEP = ','

    /** Occurs tag  */
    val OCCURS_BEGIN = '#'

    /** Minimum occurs / maximun occurs separator  */
    val OCCURS_RANGE = '-'

    /** Metadata delimiter  */
    val METADATA_END = ';'

    // **************************************************
    // Extensios
    // [DOMAIN] ='defaultValue' TAGS @'alias' ?'key=value'
    // **************************************************
    val DOMAIN_BEGIN = '['

    val DOMAIN_SEPARATOR = ','

    val DOMAIN_END = ']'

    val DEFAULT_VALUE = '='

    val LABEL_VALUE = '='

    val FILL_CHAR = '!'

    val PROPERTY = '?'

    val ALIAS = '@'

    // **************************************************
    // Extension Tags
    // **************************************************

    val RIGHT = 'R'

    val LEFT = 'L'

    val INDEXED = 'I'

    val KEY = 'K'

    val VALUE_BEGIN = '"'

    val VALUE_END = '"'


    // **************************************************
    // Etc
    // **************************************************

    /** Path separator  */
    val PATH_SEP = '.'


}
/** Private Constructor  */
