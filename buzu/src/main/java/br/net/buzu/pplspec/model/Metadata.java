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

import java.util.Collection;
import java.util.List;

/**
 * Represents a element of a positional tree.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public interface Metadata extends Nameable {

	/**
	 * Retuns the Kind of the Element: ATOMIC, ARRAY, RECORD or TABLE.
	 * 
	 * @return The Kind instance of {@link Kind}
	 */
	Kind kind();

	/**
	 * Returns the informations about an element.
	 * 
	 * @return The instance of <code>MetaInfo</code> containing the basic element
	 *         informations like name, size, ocurrences, subtype etc.
	 * @see MetaInfo
	 */
	MetaInfo info();

	/**
	 * Indicates if the Structure (Metadata) is Static or Dynamic. <BR>
	 * Static - All 'sizes' and 'maxOccurs' has a value. <BR>
	 * Dynamic - Any 'size' has no value or any maxOccurs is Unbounded (no limit).
	 * 
	 * @return <code>true</code> if the Structure or Metadata is static or
	 *         <code>false</code> is not (Dynamic).
	 */
	default boolean isStatic() {
		return hasChildren() ? isStatic(children()) : info().isStatic();
	}

	/**
	 * @param children
	 * @return
	 */
	static boolean isStatic(Collection<? extends Metadata> children) {
		for (Metadata child : children) {
			if (!child.isStatic()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns tha list of subElements.
	 * 
	 * @return The children elements.
	 */
	<T extends Metadata> List<T> children();

	/**
	 * Indicates if the element has children.
	 * 
	 * @return <code>true</code> if has children or <code>false</code> if not.
	 */
	boolean hasChildren();

	/**
	 * [Debug method] Returns ths Metadata as a Tree.
	 * 
	 * @param level
	 * @return
	 */
	String toTree(int level);

	/**
	 * [Debug method] Returns ths Metadata as plain data
	 * 
	 * @param level
	 * @return
	 */
	String toPlain();

}
