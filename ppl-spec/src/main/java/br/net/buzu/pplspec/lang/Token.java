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
package br.net.buzu.pplspec.lang;

/**
 * Language char tokens.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public final class Token {
	
	/** Private Constructor */
	private Token() {}

	// **************************************************
	// Basic
	// **************************************************

	public static final char SUB_OPEN = 	'(';
	public static final char SUB_CLOSE = 	')';
	public static final char PLIC = 		'\'';
	public static final char QUOTE = 		'"';
	public static final char BRACE_OPEN = 	'{';
	public static final char BRACE_CLOSE = 	'}';
	public static final char ARRAY_OPEN = 	'[';
	public static final char ARRAY_CLOSE = 	']';
	public static final char VAR = 			'$';

	// **************************************************
	// DataTypes
	// **************************************************

	public static final char TYPE_CHAR = 		'C';
	public static final char TYPE_STRING = 		'S';
	public static final char TYPE_NUMBER = 		'N';
	public static final char TYPE_INTEGER = 	'I';
	public static final char TYPE_LONG = 		'L';
	public static final char TYPE_BOOLEAN = 	'B';
	public static final char TYPE_DATE = 		'D';
	public static final char TYPE_TIME = 		't';
	public static final char TYPE_TIMESTAMP = 	'T';
	public static final char TYPE_PERIOD = 		'P';
	public static final char TYPE_HEXA = 		'H';

	public static final char TYPE_FLAT = 		'F';
	public static final char TYPE_MAP = 		'M';
	public static final char TYPE_OBJECT = 		'O';

	// *********************************************************************
	// Metadata
	// [NAME]:[TYPE][SIZE.SCALE]#[OCCURS_MIN-MAX][DOMAIN][DEFAULT][TAGS]
	// *********************************************************************

	/** Determines the end of 'name' part */
	public static final char NAME_END = ':';

	/** Decimal Separator */
	public static final char DECIMAL_SEP = ',';

	/** Occurs tag */
	public static final char OCCURS_BEGIN = '#';

	/** Minimum occurs / maximun occurs separator */
	public static final char OCCURS_RANGE = '-';

	/** Metadata delimiter */
	public static final char METADATA_END = ';';

	// **************************************************
	// Extensios
	// [DOMAIN] ='defaultValue' TAGS @'alias' ?'key=value'
	// **************************************************
	public static final char DOMAIN_BEGIN = '[';

	public static final char DOMAIN_SEPARATOR = ',';

	public static final char DOMAIN_END = ']';

	public static final char DEFAULT_VALUE = '=';
	
	public static final char LABEL_VALUE = '=';

	public static final char FILL_CHAR = '!';

	public static final char PROPERTY = '?';

	public static final char ALIAS = '@';

	// **************************************************
	// Extension Tags
	// **************************************************

	public static final char RIGHT = 'R';

	public static final char LEFT = 'L';

	public static final char INDEXED = 'I';

	public static final char KEY = 'K';

	public static final char VALUE_BEGIN = '"';

	public static final char VALUE_END = '"';


	// **************************************************
	// Etc
	// **************************************************

	/** Path separator */
	public static final char PATH_SEP = '.';


}
