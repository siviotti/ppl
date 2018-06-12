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
package br.net.buzu.parsing.simple.time;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import br.net.buzu.parsing.simple.AbstractSimpleParser;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * Abstract Parser to Timestamp
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class AbstractTimestampParser extends AbstractSimpleParser {

	private final DateTimeFormatter formatter;

	public AbstractTimestampParser() {
		formatter = getFormatter();
	}

	// ******************** PARSE ********************
	@Override
	protected LocalTime asSingleObject(StaticMetadata metadata, String text, Metaclass metaClass) {
		return LocalTime.parse(text, formatter);
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		return ((LocalDateTime) obj).format(formatter);
	}

	protected abstract DateTimeFormatter getFormatter();
}
