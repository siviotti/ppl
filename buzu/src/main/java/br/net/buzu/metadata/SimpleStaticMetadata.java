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
package br.net.buzu.metadata;

import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.StaticMetadata;
import br.net.buzu.util.StaticBehave;

/**
 * Implementation of StaticMetadata based on SimpleMetadata
 * 
 * @author Douglas Siviotti
 *
 */
public class SimpleStaticMetadata extends SimpleMetadata implements StaticMetadata {

	private final int serialMaxSize;

	public SimpleStaticMetadata(MetaInfo metaInfo) {
		super(metaInfo);
		StaticBehave.checkStaticInfo(metaInfo);
		serialMaxSize = metaInfo.size() * metaInfo.maxOccurs();
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public int serialMaxSize() {
		return serialMaxSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + serialMaxSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof SimpleStaticMetadata) {
			SimpleStaticMetadata other = (SimpleStaticMetadata) obj;
			return super.equals(other) && serialMaxSize == other.serialMaxSize;
		}
		return true;
	}

}
