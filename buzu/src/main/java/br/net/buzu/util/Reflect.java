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
package br.net.buzu.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.net.buzu.pplspec.annotation.PplMetadata;
import br.net.buzu.pplspec.exception.PplReflectionException;

/**
 * Reflection Utils
 * 
 * @author Douglas Siviotti
 * @since 1.0
 * 
 */
public class Reflect {

	static final String MISSING_NULLARY_CONSTRUCTOR = "Missing nullary constructor: Class '";
	static final String UNSAFE_COLLECTION = "Unsafe collection '";

	/** Private Constructor */
	private Reflect() {
	}

	// **************************************************
	// Multiple
	// **************************************************

	public static boolean isMultiple(Class<?> type) {
		return type.isArray() || (Collection.class.isAssignableFrom(type));
	}

	public static boolean isCollection(Class<?> type) {
		return (Collection.class.isAssignableFrom(type));
	}

	// **************************************************
	// Method Invoke
	// **************************************************

	public static Method findGet(final String attributeName, final Object obj) {
		String methodGet = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
		try {
			return findMethod(methodGet, obj);
		} catch (NoSuchMethodException e) {
			String methodIs = "is" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
			try {
				return findMethod(methodIs, obj);
			} catch (NoSuchMethodException e1) {
				throw new PplReflectionException(methodGet + "()/" + methodIs + "()", obj.getClass(), e);
			}
		}
	}

	public static Method findSet(final String attributeName, final Object obj, Class<?> paramType) {
		String methodName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
		try {
			return findMethod(methodName, obj, paramType);
		} catch (NoSuchMethodException e) {
			throw new PplReflectionException(methodName + "(" + paramType.getSimpleName() + ")", obj.getClass(), e);
		}
	}

	static Method findMethod(final String methodName, final Object obj, Class<?>... params)
			throws NoSuchMethodException {
		try {
			return obj.getClass().getMethod(methodName, params);
		} catch (SecurityException e) {
			throw new PplReflectionException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Object obj, final String attributeName) {
		try {
			return (T) findGet(attributeName, obj).invoke(obj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new PplReflectionException(e);
		}
	}

	public static void set(Object obj, String fieldName, Class<?> fieldType, Object param) {
		try {
			findSet(fieldName, obj, fieldType).invoke(obj, param);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new PplReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new PplReflectionException(
					"Bad param: class:" + obj.getClass().getSimpleName() + " field:" + fieldName + "("
							+ fieldType.getSimpleName() + ") param(" + param.getClass().getSimpleName() + "):" + param,
					e);
		}
	}

	public static void setField(Object obj, Field field, Object param) {
		
	}


	// **************************************************
	// Fields
	// **************************************************

	/**
	 * Returns the internal type of a Collection or Array if is multiple. If not
	 * returns the field type.
	 * 
	 * @param field
	 *            The field
	 * @return The single type relative to the field.
	 */
	public static Class<?> getElementType(Field field) {
		Class<?> fieldType = field.getType();
		if (Collection.class.isAssignableFrom(fieldType)) {
			if (field.getGenericType() instanceof ParameterizedType) {
				ParameterizedType parType = (ParameterizedType) field.getGenericType();
				if (parType.getActualTypeArguments().length > 0) {
					Type itemType = parType.getActualTypeArguments()[0];
					return (Class<?>) itemType;
				}
			}
			throw new PplReflectionException(UNSAFE_COLLECTION + field.getName() + "'. Use a generic type.");
		} else if (fieldType.isArray()) {
			return fieldType.getComponentType();
		}
		return fieldType;

	}

	public static Object newInstance(Class<?> toClass) {
		try {
			if (List.class.equals(toClass)) {
				return new ArrayList<Object>();
			} else if (Set.class.equals(toClass) || Collection.class.equals(toClass)) {
				return new HashSet<Object>();
			} else if (Map.class.equals(toClass)) {
				return new HashMap<String, Object>();
			}
			return getNullaryConstructor(toClass).newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new PplReflectionException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("rawtypes")
	static Constructor getNullaryConstructor(Class<?> toClass) {
		try {
			if (toClass.getDeclaredConstructors().length == 1) {
				return toClass.getDeclaredConstructors()[0];
			}
			return toClass.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new PplReflectionException(MISSING_NULLARY_CONSTRUCTOR + toClass.getCanonicalName() + "'", e);
		} catch (SecurityException e) {
			throw new PplReflectionException(e.getMessage(), e);
		}
	}

	public static List<Field> getAllFields(Class<?> type) {
		if (Object.class.equals(type.getSuperclass()) || type.getSuperclass() == null) {
			return Arrays.asList(type.getDeclaredFields());
		}
		List<Field> fields = new ArrayList<>();
		fields.addAll(getAllFields(type.getSuperclass()));
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		return fields;
	}

	public static PplMetadata getPplMetadata(Field field) {
		if (field.isAnnotationPresent(PplMetadata.class)) {
			return field.getAnnotation(PplMetadata.class);
		}
		Class<?> elementType = getElementType(field);
		if (elementType.isAnnotationPresent(PplMetadata.class)) {
			return elementType.getAnnotation(PplMetadata.class);
		}
		return null;
	}

}
