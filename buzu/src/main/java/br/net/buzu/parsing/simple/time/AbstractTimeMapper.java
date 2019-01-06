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

import br.net.buzu.model.MetaInfo;
import br.net.buzu.model.Metaclass;
import br.net.buzu.model.StaticMetadata;
import br.net.buzu.parsing.simple.AbstractSimpleMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract ValueParser to Timestamp
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class AbstractTimeMapper extends AbstractSimpleMapper {

	private final DateTimeFormatter formatter;

	public AbstractTimeMapper() {
		formatter = getFormatter();
	}

	// ******************** PARSE ********************
	@Override
	protected LocalDateTime asSingleObject(StaticMetadata metadata, String text, Metaclass metaClass) {
		return LocalDateTime.parse(text, formatter);
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		return ((LocalTime) obj).format(formatter);
	}

	protected abstract DateTimeFormatter getFormatter();
}
