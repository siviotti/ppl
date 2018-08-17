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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.Kind;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.PplSerializable;
import br.net.buzu.util.Reflect;

/**
 * Imutable informations about the target class on a parsing operation.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class BasicMetaclass implements Metaclass {

	private final Kind kind;
	private final Field field;
	private final String fieldName;
	private final Class<?> fieldType;
	private final Class<?> elementType;
	private final Class<? extends PayloadParser> parserType;
	private final MetaInfo metaInfo;

	public BasicMetaclass(Field field, Class<?> fieldType, Class<?> elementType, Kind kind, MetaInfo metaInfo,
			Class<? extends PayloadParser> parserType) {
		super();
		this.field = field;
		this.fieldName = field != null ? field.getName() : "";
		this.fieldType = fieldType;
		this.elementType = elementType;
		this.parserType = parserType;
		this.kind = kind;
		this.metaInfo = metaInfo;
	}

	// **************************************************
	// API
	// **************************************************

	@Override
	public String name() {
		return fieldName;
	}

	public String fieldName() {
		return fieldName;
	}

	@Override
	public Class<?> fieldType() {
		return fieldType;
	}

	@Override
	public Class<?> elementType() {
		return elementType;
	}

	@Override
	public Class<? extends PayloadParser> parserType() {
		return parserType;
	}

	@Override
	public boolean isArray() {
		return fieldType.isArray();
	}

	@Override
	public boolean isCollection() {
		return Collection.class.isAssignableFrom(fieldType);
	}

	@Override
	public boolean isPrimitive() {
		return elementType.isPrimitive();
	}

	@Override
	public boolean isEnum() {
		return elementType.isEnum();
	}

	@Override
	public boolean isPplSerializable() {
		return PplSerializable.class.isAssignableFrom(elementType);
	}

	@Override
	public boolean isOldDate() {
		return Date.class.isAssignableFrom(elementType);
	}

	@Override
	public boolean hasCustomParser() {
		return parserType != null;
	}

	@Override
	public boolean match(Class<?> elementClass) {
		return this.elementType.equals(elementClass);
	}

	@Override
	public MetaInfo info() {
		return metaInfo;
	}

	@Override
	public Kind kind() {
		return kind;
	}

	@Override
	public String toTree(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append(Token.PATH_SEP);
		}
		sb.append(toString()).append("\n");
		if (hasChildren()) {
			children().forEach(c -> sb.append(c.toTree(level + 1)));
		}
		return sb.toString();
	}

	@Override
	public String toPlain() {
		StringBuilder sb = new StringBuilder();
		sb.append(toString()).append("\n");
		if (hasChildren()) {
			children().forEach(c -> sb.append(c.toPlain()));
		}
		return sb.toString();
	}

	@Override
	public int getValueSize(Object value) {
		if (value == null) {
			return 0;
		}
		String str;
		if (isPplSerializable()) {
			str = ((PplSerializable) value).asPplSerial();
		} else {
			str = value.toString();
		}
		return str != null ? str.length() : 0;
	}

	@Override
	public Object get(Object object) {
		try {
			field.setAccessible(true);
			return field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			return Reflect.get(object, fieldName);
		}
	}

	@Override
	public void set(Object object, Object param) {
		try {
			field.setAccessible(true);
			field.set(object, param);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Reflect.set(object, fieldName, fieldType, param);
		}
	}

	// **************************************************
	// hashcode, equals, toString
	// **************************************************

	@Override
	public int hashCode() {
		return kind.hashCode() * fieldType.hashCode() * elementType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof Metaclass) {
			Metaclass other = (Metaclass) obj;
			return kind.equals(other.kind()) && fieldType.equals(other.fieldType())
					&& elementType.equals(other.elementType());
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append(metaInfo.name()).append("(").append(fieldName()).append("):");
		if (!kind.isMultiple()) {
			sb.append(fieldType.getSimpleName());
		} else if (isArray()) {
			sb.append(elementType.getSimpleName()).append("[]");
		} else {
			sb.append(fieldType.getSimpleName()).append("<").append(elementType.getSimpleName()).append(">");
		}
		return sb.toString();
	}

}
