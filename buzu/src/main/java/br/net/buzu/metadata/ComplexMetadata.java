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

import java.util.Collections;
import java.util.List;

import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;

/**
 * Metadata parent of others Metadatas.
 *
 * @author Douglas Siviotti
 * @since 1.0 (15/05/2017)
 */
public class ComplexMetadata extends BasicMetadata {

	private final List<Metadata> children;

	public ComplexMetadata(MetaInfo metaInfo, List<Metadata> children) {
		super(metaInfo.update(complexSize(children), metaInfo.maxOccurs()) );
		if (children == null) {
			throw new IllegalArgumentException("children cannot be null!");
		}
		this.children = Collections.unmodifiableList(children);
	}

	private static int complexSize(List<? extends Metadata> children) {
		int complexSize = 0;
		for (Metadata child : children) {
			complexSize += child.info().size();
		}
		return complexSize;
	}

	@Override
	public List<Metadata> children() {
		return children;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + children.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComplexMetadata) {
			ComplexMetadata other = (ComplexMetadata) obj;
			return super.equals(other) && children.equals(other.children);
		}
		return false;
	}

	@Override
	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}

	@Override
	public boolean isStatic() {
		for (Metadata child: children) {
			if (!child.isStatic()) {
				return false;
			}
		}
		return true;
	}

	

}
