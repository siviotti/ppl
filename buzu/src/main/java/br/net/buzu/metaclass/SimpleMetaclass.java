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

import java.util.List;

import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.model.Kind;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;

/**
 * Implementation of Metaclass without children.
 * 
 * @author Douglas Siviotti
 * @since 1.0
 */
public class SimpleMetaclass extends BasicMetaclass {
	

	private static final String SIMPLE_METACLASS_HAS_NO_CHILDREN = "SimpleMetaclass has no children";

	public SimpleMetaclass(String fieldName, Class<?> fieldType, Class<?> elementType, Kind kind, MetaInfo metaInfo,
			Class<? extends PayloadParser> parserType) {
		super(fieldName, fieldType, elementType, kind, metaInfo, parserType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Metaclass> children() {
		throw new UnsupportedOperationException(SIMPLE_METACLASS_HAS_NO_CHILDREN);
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Metaclass getChildByName(String childName) {
		throw new UnsupportedOperationException(SIMPLE_METACLASS_HAS_NO_CHILDREN);
	}


}
