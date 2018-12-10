/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.context;

import br.net.buzu.metadata.code.*;
import br.net.buzu.pplspec.api.MetadataCoder;
import br.net.buzu.pplspec.api.CoderManager;
import br.net.buzu.pplspec.model.Dialect;

/**
 * Simple CoderManager
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicCoderManager implements CoderManager {
	
	public static final CoderManager INSTANCE = new BasicCoderManager();

	static final MetadataCoder[] CODER_ARRAY = new MetadataCoder[Dialect.values().length];

	static {
		CODER_ARRAY[Dialect.VERBOSE.ordinal()]= VerboseMetadataCoder.INSTANCE;
		CODER_ARRAY[Dialect.NATURAL.ordinal()]= NaturalMetadataCoder.INSTANCE;
		CODER_ARRAY[Dialect.SHORT.ordinal()]= ShortMetadataCoder.INSTANCE;
		CODER_ARRAY[Dialect.COMPACT.ordinal()]= CompactMetadataCoder.INSTANCE;
		CODER_ARRAY[Dialect.STRUCTURAL.ordinal()]= StructuralMetadataCoder.INSTANCE;
	}

	public MetadataCoder getCoder(Dialect dialect) {
		return CODER_ARRAY[dialect.ordinal()];
	}

}
