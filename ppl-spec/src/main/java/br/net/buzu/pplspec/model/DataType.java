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
package br.net.buzu.pplspec.model;

import br.net.buzu.pplspec.lang.Token;

/**
 * Fundamental data types of PPL. A SuperType can define many SubTypes.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public enum DataType {

	// SINGLE (Fields)

	// Text

	/** Static Characteres */
	CHAR(Token.TYPE_CHAR, DataGroup.TEXT, false, SizeType.CUSTOM, Align.LEFT, ' ', '|'),
	/** Dynamic Characteres */
	STRING(Token.TYPE_STRING, DataGroup.TEXT),

	// Numeric

	/** Number */
	NUMBER(Token.TYPE_NUMBER, DataGroup.NUMERIC),
	/** Integer (32bits) */
	INTEGER(Token.TYPE_INTEGER, DataGroup.NUMERIC),
	/** Long Integer (64bits) */
	LONG(Token.TYPE_LONG, DataGroup.NUMERIC),
	/** Hexadecima */
	HEXA(Token.TYPE_HEXA, DataGroup.NUMERIC),

	// Boolean

	/** Boolean */
	BOOLEAN(Token.TYPE_BOOLEAN, DataGroup.BOOLEAN),

	// Time

	/** Date */
	DATE(Token.TYPE_DATE, DataGroup.TIME),
	/** Time */
	TIME(Token.TYPE_TIME, DataGroup.TIME),
	/** Timestamp / DateTime */
	TIMESTAMP(Token.TYPE_TIMESTAMP, DataGroup.TIME),
	/** Period / Duration */
	PERIOD(Token.TYPE_PERIOD, DataGroup.TIME),

	// COMPLEX (Class)

	/** Plain Complex Object */
	FLAT(Token.TYPE_FLAT, DataGroup.COMPLEX),
	/** Key/Value Object */
	MAP(Token.TYPE_MAP, DataGroup.COMPLEX),
	/** Hierarquical Complex Object */
	OBJECT(Token.TYPE_OBJECT, DataGroup.COMPLEX),;

	private final char id;
	private final DataGroup group;
	private final boolean complex;
	private final SizeType sizeType;
	private final Align align;
	private final char fillChar;
	private final char nullChar;

	private DataType(char id, DataGroup group) {
		this(id, group, DataGroup.COMPLEX.equals(group), group.sizeType(), group.align(), group.fillChar(),
				group.nullChar());
	}

	private DataType(char id, DataGroup group, boolean complex, SizeType size, Align align, char fillChar,
			char nullChar) {
		this.id = id;
		this.group = group;
		this.complex = complex;
		this.sizeType = size;
		this.align = align;
		this.fillChar = fillChar;
		this.nullChar = nullChar;
	}

	/**
	 * Returns the instance of Type
	 * 
	 * @param charId
	 *            The Charactere ID
	 * @return A instance of <code>Type</code> or <code>null</code> .
	 */
	public static DataType get(char charId) {
		for (DataType type : values()) {
			if (type.id == charId) {
				return type;
			}
		}
		return null;
	}

	public char id() {
		return id;
	}

	public boolean isComplex() {
		return complex;
	}

	public SizeType sizeType() {
		return sizeType;
	}

	public Align align() {
		return align;
	}

	public char fillChar() {
		return fillChar;
	}

	public char nullChar() {
		return nullChar;
	}

	public DataGroup group() {
		return group;
	}

}
