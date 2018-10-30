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

import java.io.Serializable;
import java.text.ParseException;

import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.lang.Token;

/**
 * <b>Immutable</b> representation of a PPL String.
 * 
 * <pre>
 * (METADATA) PAYLOAD
 * </pre>
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public final class PplString implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final PplString EMPTY = new PplString("" + Token.SUB_OPEN + Token.SUB_CLOSE);

	private final String metadata;
	private final String payload;
	private final Syntax syntax;

	/**
	 * Simple constructor.
	 * 
	 * @param text
	 *            The PPL String as text.
	 */
	public PplString(String text) {
		this(text, new Syntax());
	}

	/**
	 * Context constructor.
	 * 
	 * @param text
	 *            The PPL String as text.
	 * @param syntax
	 *            The alternative Syntax.
	 */
	public PplString(String text, Syntax syntax) {
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("text cannot be 'null' or Empty!");
		}
		if (syntax == null) {
			throw new IllegalArgumentException("syntax cannot be null!");
		}
		this.syntax = syntax;
		try {
			metadata = syntax.extractMetadata(text);
			payload = text.substring(metadata.length());
		} catch (ParseException pe) {
			throw new PplParseException("PPL Parse error. Text:\n" + text, pe);
		}
	}
	// **************************************************
	// factory methods
	// **************************************************

	public static PplString of(String text) {
		return new PplString(text);
	}
	
	// **************************************************
	// hashcode, equals, toString
	// **************************************************

	@Override
	public int hashCode() {
		return metadata.hashCode() * 31 + payload.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof PplString) {
			PplString other = (PplString) obj;
			return metadata.equals(other.metadata) && payload.equals(other.payload);
		}
		return false;
	}

	@Override
	public String toString() {
		return syntax.asPplString(metadata, payload);
	}

	// **************************************************
	// get / set
	// **************************************************

	public String getMetadata() {
		return metadata;
	}

	public String getPplMetadata() {
		return metadata.substring(1, metadata.length() - 1);
	}

	public String getPayload() {
		return payload;
	}

	public Syntax getSyntax() {
		return syntax;
	}

}
