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
 * Generic implementation to <code>LabelValue</code>.
 * 
 * @author Douglas Siviotti (073.116.317-69) #21 3509-7585
 * @since 10 de abr de 2018 - Construção da Duimp (Release 1)
 * @see LabeledValue
 *
 */
public class DomainItem implements LabeledValue, Comparable<DomainItem> {

	private final String value;
	private final String label;

	/**
	 * Simple constructor.
	 * 
	 * @param value
	 *            The internal value.
	 */
	public DomainItem(String value) {
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
	public DomainItem(String value, String label) {
		super();
		if (value == null) {
			throw new NullPointerException("'value' cannot be null!");
		}
		this.value = value;
		this.label = label;
	}
	
	public static DomainItem parse(String text) {
		if (text == null) {
			return null;
		}
		int pos = text.indexOf(Token.LABEL_VALUE);
		if (pos < 0) {
			return new DomainItem(text);
		}
		String value = text.substring(0, pos);
		String label = text.substring(pos+1, text.length());
		return new DomainItem(value, label);
	}


	@Override
	public String value() {
		return value;
	}
	
	public int intValue() {
		return Integer.parseInt(value);
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
	
	public String asSerial() {
		return hasLabel() ? value + Token.LABEL_VALUE + label : value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof DomainItem) {
			return asSerial().equals(((DomainItem)obj).asSerial());
		}
		return false;
	}

	@Override
	public String toString() {
		return asSerial();
	}

	@Override
	public int compareTo(DomainItem o) {
		return value.compareTo(o.value);
	}

}
