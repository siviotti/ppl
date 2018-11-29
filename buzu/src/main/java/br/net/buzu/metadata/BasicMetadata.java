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

import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.Kind;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;

import static br.net.buzu.pplspec.model.KindKt.getKind;

/**
 * Most basic abstract implementation domainOf <code>Metadata</code>.
 *
 * @author Douglas Siviotti
 * @since 1.0 (15/05/2017)
 */
public abstract class BasicMetadata implements Metadata {

	private final Kind kind;
	private final MetaInfo metaInfo;

	public BasicMetadata(MetaInfo metaInfo) {
		super();
		if (metaInfo == null) {
			throw new NullPointerException("metainfo cannot be null!");
		}
		this.metaInfo = metaInfo;
		this.kind = getKind(this.metaInfo.isMultiple(), this.metaInfo.getSubtype().dataType().isComplex());
	}

	// **************************************************
	// API
	// **************************************************

	@Override
	public String name() {
		return metaInfo.getName();
	}

	@Override
	public Kind kind() {
		return kind;
	}
	


	@Override
	public MetaInfo info() {
		return metaInfo;
	}

	@Override
	public String toTree(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(Token.PATH_SEP);
		}
		sb.append(toString()).append("\n");
		if (hasChildren()) {
			for (Metadata child : children()) {
				sb.append(child.toTree(level + 1));
			}
		}
		return sb.toString();
	}

	@Override
	public String toPlain() {
		StringBuilder sb = new StringBuilder();
		sb.append(info().getId()).append("\n");
		if (hasChildren()) {
			children().forEach(c -> sb.append(c.toPlain()));
		}
		return sb.toString();
	}

	// **************************************************
	// hashcode, equals, toString
	// **************************************************

	@Override
	public int hashCode() {
		return kind.ordinal() * 31 + metaInfo.hashCode() * 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof Metadata) {
			Metadata other = (Metadata) obj;
			return kind.equals(other.kind()) && metaInfo.equals(other.info());
		}
		return false;
	}

	@Override
	public String toString() {
		return kind + " " + metaInfo.ppl();
	}

}
