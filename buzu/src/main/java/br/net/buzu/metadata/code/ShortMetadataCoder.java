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
package br.net.buzu.metadata.code;

import br.net.buzu.ext.MetadataCoder;
import br.net.buzu.model.Dialect;

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class ShortMetadataCoder extends NaturalMetadataCoder {
	
	public static final MetadataCoder INSTANCE = new ShortMetadataCoder();
	
	@Override
	public Dialect dialect() {
		return Dialect.SHORT;
	}

	@Override
	// Hide space after name
	protected String afterName() {
		return EMPTY;
	}

	@Override
	// Hide enter after (
	protected String afterSubOpen() {
		return EMPTY;
	}

	@Override
	// Hide "tab" after subMeta
	protected String indentationElement() {
		return EMPTY;
	}

	@Override
	// Hide space after elementType
	protected String afterType() {
		return EMPTY;
	}

	@Override
	// Hide space after format
	protected String afterSize() {
		return EMPTY;
	}

	@Override
	// Hide enter after name
	protected String afterMetadataEnd() {
		return EMPTY;
	}
	
	


}
