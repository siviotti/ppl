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
 * List of Metadata Kind: ATOMIC, ARRAY, RECORD and TABLE
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public enum Kind {

	/** One single value. String, Integer, Boolean etc. */
	ATOMIC(false, false),
	/** Many single values. List, Set, Arrays etc. */
	ARRAY(true, false),
	/** One complex value. Customer, Person etc. */
	RECORD(false, true),
	/** Many complex values. List[Customer], Set[Person] etc. */
	TABLE(true, true),;

	private final boolean multiple;
	private final boolean complex;

	private Kind(boolean multiple, boolean complex) {
		this.multiple = multiple;
		this.complex = complex;
	}

	public static Kind get(boolean multiple, boolean complex) {
		if (multiple) {
			return complex? TABLE: ARRAY;
		} else {
			return complex? RECORD: ATOMIC;
		}
	}

	/**
	 * Indicates if a metadata allows multiple values.
	 * 
	 * @return <code>true</code> if the metadata supports many values or
	 *         <code>false</code> if supports only one.
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * Indicates if a metadata is composed for other metadatas.
	 * 
	 * @return <code>true</code> if a metadata is composed by others or
	 *         <code>false</code> if its value is a simple value.
	 */
	public boolean isComplex() {
		return complex;
	}

}
