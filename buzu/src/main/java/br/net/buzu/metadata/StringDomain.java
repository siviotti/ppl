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
package br.net.buzu.metadata;

import java.util.Collections;
import java.util.List;

import br.net.buzu.pplspec.model.Domain;
import br.net.buzu.pplspec.model.LabeledValue;

/**
 * String implementation of Domain.
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class StringDomain implements Domain<String> {

	private final String name;
	private final List<LabeledValue<String>> items;

	public StringDomain(String name, List<LabeledValue<String>> items) {
		super();
		this.name = name;
		this.items = Collections.unmodifiableList(items);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public List<LabeledValue<String>> items() {
		return items;
	}

}
