/*
 *	This file is part domainOf Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms domainOf the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 domainOf the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy domainOf the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.parsing.simple.bool;

import br.net.buzu.parsing.simple.AbstractSimpleParser;
import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * 
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class AbstractBooleanCharParser extends AbstractSimpleParser {

	private final String falseValue;

	private final String trueValue;

	public AbstractBooleanCharParser() {
		falseValue = "" + getFalseValue();
		trueValue = "" + getTrueValue();
	}

	static final String PARSING_ERROR = "Boolean parsing error. ";

	// ******************** PARSE ********************
	@Override
	protected Boolean asSingleObject(StaticMetadata metadata, String text, Metaclass metaClass) {
		try {
			return trueValue.equals(text) ? Boolean.TRUE : Boolean.FALSE;
		} catch (NumberFormatException nfe) {
			throw new PplParseException(PARSING_ERROR + "] Text:\n" + text, nfe);
		}
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		return ((Boolean) obj).booleanValue() ? trueValue : falseValue;
	}

	protected abstract char getTrueValue();

	protected abstract char getFalseValue();
}
