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

/**
 * List of DataType group.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public enum DataGroup {

	/** */
	TEXT(SizeType.CUSTOM, Align.LEFT, ' ', ' ', true),
	/** */
	NUMERIC(SizeType.CUSTOM, Align.RIGHT, '0', ' ', true),
	/** */
	BOOLEAN(SizeType.FIXED, Align.LEFT, ' ', ' ', true),
	/** */
	TIME(SizeType.FIXED, Align.LEFT, ' ', ' ', true),
	/** */
	COMPLEX(SizeType.SUM, Align.LEFT, ' ', ' ', true);

	private final SizeType sizeType;
	private final Align align;
	private final char fillChar;
	private final char nullChar;
	private final boolean delimited;

	public boolean isDelimited() {
		return delimited;
	}

	private DataGroup(SizeType sizeType, Align align, char fillChar, char nullChar, boolean delimited) {
		this.sizeType = sizeType;
		this.align = align;
		this.fillChar = fillChar;
		this.nullChar = nullChar;
		this.delimited = delimited;
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

}
