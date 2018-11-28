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
package br.net.buzu.parsing.simple.time;

import br.net.buzu.parsing.simple.AbstractSimpleParser;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Parser to Date
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class AbstractDateParser extends AbstractSimpleParser {

	private final DateTimeFormatter formatter;

	public AbstractDateParser() {
		formatter = getFormatter();
	}

	// ******************** PARSE ********************
	@Override
	protected LocalDate asSingleObject(StaticMetadata metadata, String text, Metaclass metaClass) {
		return LocalDate.parse(text, formatter);
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		return ((LocalDate) obj).format(formatter);
	}

	protected abstract DateTimeFormatter getFormatter();
}
