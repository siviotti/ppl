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

import br.net.buzu.parsing.simple.AbstractSimpleMapper;
import br.net.buzu.java.model.MetaInfo;
import br.net.buzu.java.model.Metaclass;
import br.net.buzu.java.model.StaticMetadata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Parser to Date
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class AbstractDateMapper extends AbstractSimpleMapper {

	private final DateTimeFormatter formatter;

	public AbstractDateMapper() {
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
