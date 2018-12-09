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
package br.net.buzu.metaclass;


import br.net.buzu.pplspec.api.PayloadMapper;
import br.net.buzu.pplspec.model.Kind;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.util.StaticBehave;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Implementation domainOf Metaclass without children.
 * 
 * @author Douglas Siviotti
 *
 */
public class ComplexMetaclass extends BasicMetaclass {

	
	private final List<Metaclass> children;
	private final Map<String, Metaclass> internalMap = new HashMap<>();

	public ComplexMetaclass(Field field, Class<?> fieldType, Class<?> elementType, Kind kind, MetaInfo metaInfo,
                            Class<? extends PayloadMapper> parserType, List<? extends Metaclass> children) {
		super(field, fieldType, elementType, kind, metaInfo, parserType);
		this.children = children != null ? Collections.unmodifiableList(children)
				: Collections.unmodifiableList(new ArrayList<>());
		this.children.forEach(c -> internalMap.put(c.info().getName(), c));
	}

	@Override
	public List<Metaclass> children() {
		return children;
	}

	@Override
	public boolean hasChildren() {
		return !children.isEmpty();
	}

	@Override
	public Metaclass getChildByName(String childName) {
		return internalMap.get(childName);
	}


@Override
	public boolean isStatic() {
		return StaticBehave.isStaticChidren(children);
	}

}
