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

import br.net.buzu.model.StaticMetaclass;
import br.net.buzu.api.PayloadMapper;
import br.net.buzu.model.*;
import br.net.buzu.util.StaticBehave;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
@SuppressWarnings("unchecked")
public class ComplexStaticMetaclass extends ComplexMetaclass implements StaticMetaclass {

	private final int serialSize;

	public ComplexStaticMetaclass(Field field, Class<?> fieldType, Class<?> elementType, Kind kind,
                                  MetaInfo metaInfo, Class<? extends PayloadMapper> parserType, List<Metaclass> children) {
		super(field, fieldType, elementType, kind, metaInfo, parserType, children);
		int tmp = 0;
		for (Metaclass child : children) {
			StaticBehave.checkStaticChild(child);
			tmp += ((StaticStructure) child).serialMaxSize();
		}
		this.serialSize = tmp * metaInfo.getMaxOccurs();
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public int serialMaxSize() {
		return serialSize;
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
		if (obj instanceof ComplexStaticMetaclass) {
			ComplexStaticMetaclass other = (ComplexStaticMetaclass) obj;
			return super.equals(other) && serialSize == other.serialSize;
		}
		return false;
	}


}
