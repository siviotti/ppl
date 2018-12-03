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
@file:JvmName("Token")

package br.net.buzu.pplspec.lang

/**
 * Language char tokens.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
// **************************************************
// Basic
// **************************************************
const val SUB_OPEN = '('
const val SUB_CLOSE = ')'
const val PLIC = '\''
const val QUOTE = '"'
const val BRACE_OPEN = '{'
const val BRACE_CLOSE = '}'
const val ARRAY_OPEN = '['
const val ARRAY_CLOSE = ']'
const val VAR = '$'

// **************************************************
// DataTypes
// **************************************************

const val TYPE_CHAR = 'C'
const val TYPE_STRING = 'S'
const val TYPE_NUMBER = 'N'
const val TYPE_INTEGER = 'I'
const val TYPE_LONG = 'L'
const val TYPE_BOOLEAN = 'B'
const val TYPE_DATE = 'D'
const val TYPE_TIME = 't'
const val TYPE_TIMESTAMP = 'T'
const val TYPE_PERIOD = 'P'
const val TYPE_HEXA = 'H'

const val TYPE_FLAT = 'F'
const val TYPE_MAP = 'M'
const val TYPE_OBJECT = 'O'

// *********************************************************************
// Metadata
// [NAME]:[TYPE][SIZE.SCALE]#[OCCURS_MIN-MAX][DOMAIN][DEFAULT][TAGS]
// *********************************************************************

/** Determines the end domainOf 'name' part  */
const val NAME_END = ':'

/** Decimal Separator  */
const val DECIMAL_SEP = ','

/** Occurs tag  */
const val OCCURS_BEGIN = '#'

/** Minimum occurs / maximun occurs separator  */
const val OCCURS_RANGE = '-'

/** Metadata delimiter  */
const val METADATA_END = ';'

// **************************************************
// Extensios
// [DOMAIN] ='defaultValue' TAGS @'alias' ?'key=value'
// **************************************************
const val DOMAIN_BEGIN = '['

const val DOMAIN_SEPARATOR = ','

const val DOMAIN_END = ']'

const val DEFAULT_VALUE = '='

const val LABEL_VALUE = '='

const val FILL_CHAR = '!'

const val PROPERTY = '?'

const val ALIAS = '@'

// **************************************************
// Extension Tags
// **************************************************

const val RIGHT = 'R'

const val LEFT = 'L'

const val INDEXED = 'I'

const val KEY = 'K'

const val VALUE_BEGIN = '"'

const val VALUE_END = '"'


// **************************************************
// Etc
// **************************************************

/** Path separator  */
const val PATH_SEP = '.'

