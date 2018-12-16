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
package br.net.buzu.parsing;

import static br.net.buzu.lib.TextKt.*;

import br.net.buzu.pplspec.api.PayloadMapper;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;
import br.net.buzu.util.Reflect;

import java.util.*;
import java.util.logging.Logger;

/**
 * [STATELESS] Generic parser based on Metadata.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class AbstractPositionalMapper implements PayloadMapper {

	// ******************** PARSE ********************

	@SuppressWarnings("unchecked")
	@Override
	public <T> T parse(StaticMetadata metadata, String text, Metaclass toClass) {
		return (T) doParse(metadata, text, toClass);
	}

	protected abstract Object doParse(StaticMetadata metadata, String text, Metaclass toClass);

	protected Object[] createAndFillArray(Metaclass toClass, int maxOccurs) {
		Object[] array = new Object[maxOccurs];
		if (!toClass.isPrimitive() && toClass.kind().isComplex()) {
			for (int i = 0; i < array.length; i++) {
				array[i] = Reflect.newInstance(toClass.elementType());
			}
		}
		return array;
	}

	protected void callSet(Object object, Metaclass childMetaclass, Object parsed) {
		if (parsed == null) {
			childMetaclass.set(object, parsed);
			// Reflect.set(object, childMetaclass.fieldName(), childMetaclass.fieldType(),
			// parsed);
		} else if (Collection.class.isAssignableFrom(childMetaclass.fieldType())
				&& !(parsed instanceof Collection<?>)) {
			List<Object> list = new ArrayList<>();
			list.add(parsed);
			// Reflect.set(object, childMetaclass.fieldName(), childMetaclass.fieldType(),
			// list);
			childMetaclass.set(object, list);
		} else if (childMetaclass.fieldType().isArray() && !parsed.getClass().isArray()) {
			// TODO
			Logger.getLogger(getClass().getCanonicalName()).severe("ARRAY ERROR");
		} else {
			// Reflect.set(object, childMetaclass.fieldName(), childMetaclass.fieldType(),
			// parsed);
			childMetaclass.set(object, parsed);
		}
	}

	// ******************** SERIALIZE ********************

	@Override
	public String serialize(StaticMetadata metadata, Object obj, Metaclass fromClass) {
		return obj != null ? serializeNotNull(metadata, obj, fromClass) : serializeNull(metadata);
	}

	protected String serializeNull(StaticMetadata metadata) {
		return fill(metadata.info().getAlign(), "", metadata.serialMaxSize(),
				metadata.info().getSubtype().getDataType().getNullChar());
	}

	protected abstract String serializeNotNull(StaticMetadata metadata, Object objNullSafe, Metaclass fromClass);

	protected Object[] toMaxArray(Object obj, int size) {
		Object[] array = new Object[size];
		if (size == 0) {
			return array;
		}
		for (int i = 0; i < array.length; i++) {
			array[i] = null;
		}
		if (obj instanceof Collection<?>) {
			int i = 0;
			for (Iterator<?> iterator = ((Collection<?>) obj).iterator(); iterator.hasNext();) {
				array[i] = iterator.next();
				i++;
			}
		} else if (obj != null && obj.getClass().isArray()) {
			for (int i = 0; i < ((Object[]) obj).length; i++) {
				array[i] = ((Object[]) obj)[i];
			}
		} else {
			array[0] = obj;
		}
		return array;
	}

	public Object fromArray(Object[] array, Metaclass toClass) {
		if (toClass.isArray()) {
			return array;
		}
		if (toClass.isCollection()) {
			Collection<Object> collection;
			if (Set.class.isAssignableFrom(toClass.fieldType())) {
				collection = new HashSet<>();
			} else {
				collection = new ArrayList<>();
			}
			for (int i = 0; i < array.length; i++) {
				collection.add(array[i]);
			}
			return collection;
		}
		return array[0];
	}

}
