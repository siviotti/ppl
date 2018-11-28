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
package br.net.buzu.metadata;

import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.util.StaticBehave;

import java.util.List;

/**
 * No complex metadata.
 *
 * @author Douglas Siviotti
 * @since 1.0 (15/05/2017)
 */
public class SimpleMetadata extends BasicMetadata {

	private static final String SIMPLE_METADATA_HAS_NO_CHILDREN = "SimpleMetadata has no children!";

	public SimpleMetadata(MetaInfo metaInfo) {
		super(metaInfo);
		
	}

	@Override
	public List<Metadata> children() {
		throw new UnsupportedOperationException(SIMPLE_METADATA_HAS_NO_CHILDREN);
	}

	@Override
	public boolean hasChildren() {
		return false;
	}


	@Override
	public boolean isStatic() {
		return StaticBehave.isStatic(this);
	}
}
