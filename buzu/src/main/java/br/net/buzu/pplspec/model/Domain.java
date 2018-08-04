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

import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.lang.Token;

/**
 * Represents the Domain Value of a Metadata.
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class Domain implements Nameable {

	public static final Domain EMPTY = new Domain(Syntax.EMPTY, null);

	private final String name;

	private final List<DomainItem> items;

	protected Domain(String name, List<DomainItem> items) {
		super();
		this.name = name;
		this.items = items != null ? Collections.unmodifiableList(items)
				: Collections.unmodifiableList(new ArrayList<>());
	}

	public static List<DomainItem> list(String... array) {
		List<DomainItem> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(DomainItem.parse(array[i]));
		}
		return list;
	}

	public static List<DomainItem> createItems(List<String> stringList) {
		List<DomainItem> list = new ArrayList<>();
		for (String item : stringList) {
			list.add(DomainItem.parse(item));
		}
		return list;
	}

	public static Domain create(String name, List<DomainItem> items) {
		return new Domain(name, items);
	}

	public static Domain of(String... array) {
		return create(Syntax.EMPTY, list(array));
	}

	@Override
	public String name() {
		return name;
	}
	
	public boolean containsValue(String value) {
		for (DomainItem item: items) {
			if (item.value().equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retuns the items of LabeledValue.
	 * 
	 * @return A instance of List<LabeledValue>.
	 */
	public List<DomainItem> items() {
		return items;
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	public String asSerial() {
		if (items.isEmpty()) {
			return Syntax.EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(Token.DOMAIN_BEGIN);
		for (DomainItem item : items) {
			sb.append(Token.VALUE_BEGIN).append(item.asSerial()).append(Token.VALUE_END).append(Token.DOMAIN_SEPARATOR);
		}
		sb.deleteCharAt(sb.length() - 1); // remove last
		sb.append(Token.DOMAIN_END);
		return sb.toString();
	}


	@Override
	public int hashCode() {
		return name.hashCode() * 31 + items.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Domain) {
			Domain o = (Domain) obj;
			return name.equals(o.name) && items.equals(o.items);
		}
		return false;
	}

	@Override
	public String toString() {
		return asSerial();
	}

}
