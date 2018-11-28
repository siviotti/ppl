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
package br.net.buzu.metadata.code;

import br.net.buzu.pplspec.api.MetadataCoder;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.Dialect;
import br.net.buzu.pplspec.model.MetaInfo;

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class NaturalMetadataCoder extends VerboseMetadataCoder {
	
	public static final MetadataCoder INSTANCE = new NaturalMetadataCoder();
	
	@Override
	public Dialect dialect() {
		return Dialect.NATURAL;
	}

	@Override
	// Hide default occurs #0-1
	protected String serializeOccurs(MetaInfo meta) {
		if (meta.getMinOccurs() == Syntax.DEFAULT_MIN_OCCURS
				&& meta.getMaxOccurs() == Syntax.DEFAULT_MAX_OCCURS) {
			return EMPTY;
		}
		if (meta.getMinOccurs() == 1 && meta.getMaxOccurs() == 1) {
			return "" + Token.OCCURS_BEGIN;
		}
		return super.serializeOccurs(meta);
	}

	@Override
	// Hide Complex format (size)
	protected String serializeComplexSize(MetaInfo metadata) {
		return EMPTY;
	}

}
