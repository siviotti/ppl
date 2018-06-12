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

import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.LabeledValue;

/**
 * Generic implementation to <code>LabelValue</code>.
 * 
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585
 * @since 10 de abr de 2018 - Construção da Duimp (Release 1)
 * @see LabeledValue
 *
 */
public class GenericLabelValue<T extends Comparable<T>> implements LabeledValue<T> {

	private final T value;
	private final String label;

	/**
	 * Simple constructor.
	 * 
	 * @param value
	 *            The internal value.
	 */
	public GenericLabelValue(T value) {
		this(value, null);
	}

	/**
	 * Complete constructor.
	 * 
	 * @param value
	 *            The internal value.
	 * @param label
	 *            The correspondent label.
	 */
	public GenericLabelValue(T value, String label) {
		super();
		if (value == null) {
			throw new NullPointerException("'value' cannot be null!");
		}
		this.value = value;
		this.label = label;
	}

	@Override
	public T value() {
		return value;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public boolean hasLabel() {
		return label != null;
	}

	@Override
	public int hashCode() {
		return value.hashCode() * 31 + (label != null ? label.hashCode() : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof LabeledValue<?>) {
			return value.equals(((LabeledValue<?>) obj).value());
		}
		return false;
	}

	@Override
	public String toString() {
		return hasLabel() ? value.toString() + Token.LABEL_VALUE + label : value.toString();
	}

}
