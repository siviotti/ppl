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
package br.net.buzu.parsing.simple.oldtime;

import br.net.buzu.pplspec.model.Subtype;

/**
 * Parser to {@link Subtype#TIMESTAMP_AND_MILLIS}
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class OldTimestampAndMillisMapper extends AbstractOldDatetimeMapper {

	public static final String FORMAT = "yyyyMMddHHmmssSSS";
	
	public static final OldTimestampAndMillisMapper INSTANCE = new OldTimestampAndMillisMapper();

	@Override
	protected String getFormat() {
		return FORMAT;
	}

}
