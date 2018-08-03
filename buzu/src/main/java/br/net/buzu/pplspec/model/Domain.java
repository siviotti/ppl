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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Domain Value of a Metadata.
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class Domain implements Nameable {

	public static final Domain EMPTY = new Domain("", null);

	private final String name;

	private final List<DomainItem> items;

	protected Domain(String name, List<DomainItem> items) {
		super();
		this.name = name;
		this.items = items != null ? Collections.unmodifiableList(items)
				: Collections.unmodifiableList(new ArrayList<>());
	}
	
	public static Domain create(String name, List<DomainItem> items) {
		return new Domain(name, items);
	}
	
	@Override
	public String name() {
		return name;
	}


	/**
	 * Retuns the items of LabeledValue.
	 * 
	 * @return A instance of List<LabeledValue>.
	 */
	public List<DomainItem> items() {
		return items;
	}

}
