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
package br.net.buzu.metaclass;

import br.net.buzu.java.api.PayloadMapper;
import br.net.buzu.java.model.Kind;
import br.net.buzu.java.model.MetaInfo;
import br.net.buzu.java.model.StaticMetadata;

import java.lang.reflect.Field;

/**
 * Implementation domainOf StaticMetadata based on SimpleMetadata
 * 
 * @author Douglas Siviotti
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public class SimpleStaticMetaclass extends SimpleMetaclass implements StaticMetadata {

	private final int serialMaxSize;

	public SimpleStaticMetaclass(Field field, Class<?> fieldType, Class<?> elementType, Kind kind,
			MetaInfo metaInfo, Class<? extends PayloadMapper> parserType) {
		super(field, fieldType, elementType, kind, metaInfo, parserType);
		if (!metaInfo.isComplete()) {
			throw new IllegalArgumentException("Static Metaclass requires a complete metainfo (has size and maxOccurs):"+metaInfo);
		}
		if (metaInfo.isUnbounded()) {
			throw new IllegalArgumentException("Static Metaclass cannot be Unbounded:" + metaInfo);
		}
		serialMaxSize = metaInfo.getSize() * metaInfo.getMaxOccurs();
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
		if (obj instanceof SimpleStaticMetaclass) {
			SimpleStaticMetaclass other = (SimpleStaticMetaclass) obj;
			return super.equals(other) && serialMaxSize == other.serialMaxSize;
		}
		return false;
	}

}
