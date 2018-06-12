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
package br.net.buzu.pplspec.data;

/**
 * Plain Data Map
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public interface DataMap {

	/**
	 * Returns if a Data exists based on ID.
	 * 
	 * @param id
	 *            The id
	 * @return <code>true</code> if exists, <code>false</code> if not.
	 */
	boolean exists(final String id);

	/**
	 * Returns the Data relative to the ID.
	 * 
	 * @param id
	 *            the Data ID.
	 * @return The instance of <code>Data</code> corresponding to ID or an
	 *         IllegalArgumentException if not found.
	 * @throws IllegalArgumentException
	 */
	Data<?> get(final String id);

	/**
	 * Sets the value of an existing Data.
	 * 
	 * @param id
	 *            The ID
	 * @param value
	 *            The new value.
	 * @return The internal value after "set".
	 */
	<T> T set(final String id, final T value);

}
