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
package br.net.buzu.parsing.simple.oldtime;

import br.net.buzu.parsing.simple.AbstractSimpleParser;
import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract Parser to Old Date
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class AbstractOldDatetimeParser extends AbstractSimpleParser {

	static final String PARSING_ERROR = "Old Date parsing error. ";

	private final SimpleDateFormat sdf;

	public AbstractOldDatetimeParser() {
		sdf = new SimpleDateFormat(getFormat());
	}

	// ******************** PARSE ********************
	@Override
	protected Date asSingleObject(StaticMetadata metadata, String text, Metaclass metaClass) {
		try {
			return sdf.parse(text);
		} catch (ParseException e) {
			throw new PplParseException(PARSING_ERROR + " Text:\n" + text, e);
		}
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		return sdf.format((Date) obj);
	}

	protected abstract String getFormat();
}
