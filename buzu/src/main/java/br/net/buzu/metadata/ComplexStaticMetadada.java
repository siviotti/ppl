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

import br.net.buzu.model.MetaInfo;
import br.net.buzu.model.Metadata;
import br.net.buzu.model.StaticMetadata;
import br.net.buzu.model.StaticStructure;
import br.net.buzu.util.StaticBehave;

import java.util.List;

/**
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class ComplexStaticMetadada extends ComplexMetadata implements StaticMetadata {

	private final int serialSize;

	public ComplexStaticMetadada(MetaInfo metaInfo, List<Metadata> children) {
		super(metaInfo, children);
		StaticBehave.checkStaticInfo(info());
		int tmp = 0;
		for (Metadata child : children) {
			StaticBehave.checkStaticChild(child);
			tmp += ((StaticStructure) child).serialMaxSize();
		}
		this.serialSize = tmp * info().getMaxOccurs();
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + serialSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComplexStaticMetadada) {
			ComplexStaticMetadada other = (ComplexStaticMetadada) obj;
			return super.equals(other) && serialSize == other.serialSize;
		}
		return false;
	}

	@Override
	public int serialMaxSize() {
		return serialSize;
	}

}
