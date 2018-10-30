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
package br.net.buzu.pplspec.context;

import br.net.buzu.pplspec.model.Subtype;

/**
 * SubType Manager.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public interface SubtypeManager {

	/**
	 * Creates a instance of <code>SubType</code> based on a Class.
	 * 
	 * @param typeClass
	 *            The class
	 * @return The correct instance relative to the class.
	 */
	Subtype fromType(Class<?> typeClass);

	/**
	 * Creates a instance of <code>SubType</code> based on a text representation.
	 * 
	 * @param text
	 *            The text representation
	 * @param indicates
	 *            if the Metadata is complex
	 * @return The correct instance relative to text or <code>null</code> if the
	 *         text is invalid.
	 */
	Subtype fromText(String text, boolean complex);
	
	boolean isSimple(Class<?> type);
}
