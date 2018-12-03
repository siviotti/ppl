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
package br.net.buzu.metadata.build.load;

import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.SizeType;
import br.net.buzu.pplspec.model.Subtype;

import java.util.Collection;

/**
 * Abstraction over Field/Root values
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class LoadNode {

	private final Object[] value;
	private final Metaclass metaclass;
	private final Subtype subtype;
	private final String fieldPath;

	/**
	 * Multiple Node Constructor.
	 * 
	 * @param value
	 * @param fieldType
	 * @param subtype
	 * @param elementType
	 */
	public LoadNode(Object value, Metaclass metaclass, String fieldPath) {
		this.subtype = metaclass.info().getSubtype();
		this.metaclass = metaclass;
		this.fieldPath = fieldPath;
		if (value == null) {
			this.value = new Object[1];
		} else if (metaclass.isCollection()) {
			if (((Collection<?>) value).isEmpty()) {
				this.value = new Object[1];
			} else {
				this.value = ((Collection<?>) value).toArray();
			}
		} else if (metaclass.isArray()) {
			if (((Object[]) value).length == 0) {
				this.value = new Object[1];
			} else {
				this.value = (Object[]) value;
			}
		} else {
			this.value = new Object[1];
			this.value[0] = value;
		}
	}

	public int calcMaxSize() {
		if (subtype.dataType().sizeType().equals(SizeType.CUSTOM)) {
			int max = 0;
			int tmp = 0;
			for (Object obj : value) {
				tmp = metaclass.getValueSize(obj);
				if (tmp > max) {
					max = tmp;
				}
			}
			return max;
		}
		return subtype.fixedSize();
	}

	boolean isNull() {
		return value[0] == null && value.length == 1;
	}

	public boolean isComplex() {
		return subtype.dataType().isComplex();
	}

	public boolean isEnum() {
		return metaclass.isEnum();
	}

	public Object[] getValue() {
		return value;
	}

	public int getOccurs() {
		return value.length;
	}

	public Subtype getSubtype() {
		return subtype;
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public Metaclass getMetaclass() {
		return metaclass;
	}

}
