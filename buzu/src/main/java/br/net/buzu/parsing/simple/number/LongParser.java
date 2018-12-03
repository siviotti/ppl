/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.parsing.simple.number;

import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;
import br.net.buzu.pplspec.model.Subtype;

import java.math.BigInteger;

/**
 * Parser to {@link Subtype#LONG}
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class LongParser extends AbstractNumericParser {

	public static final LongParser INSTANCE = new LongParser();

	static final String PARSING_ERROR = "Long parsing error. ";

	// ******************** PARSE ********************
	@Override
	protected Object asSingleObject(StaticMetadata metadata, String text, Metaclass toClass) {
		try {
			if (toClass.match(BigInteger.class)) {
				return new BigInteger(text);
			}
			return Long.parseLong(text);
		} catch (NumberFormatException nfe) {
			throw new PplParseException(PARSING_ERROR + " Text:\n" + text, nfe);
		}
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		return Long.toString((Long) obj);
	}
}
