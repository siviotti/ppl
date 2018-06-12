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

import java.time.format.DateTimeFormatter;

import br.net.buzu.pplspec.model.Subtype;

/**
 * Parser to {@link Subtype#TIME}
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class TimeParser extends AbstractTimeParser {

	public static final String FORMAT = "HHmmss";

	public static final TimeParser INSTANCE = new TimeParser();

	@Override
	protected DateTimeFormatter getFormatter() {
		return DateTimeFormatter.ofPattern(FORMAT);
	}

	
 }
