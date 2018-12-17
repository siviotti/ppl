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

import br.net.buzu.java.annotation.PplMetadata;
import br.net.buzu.java.exception.PplReflectionException;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

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

	static final Map<Class<?>, Object> INITIAL_VALUES = new HashMap<>();

	static {
		INITIAL_VALUES.put(boolean.class, false);
		INITIAL_VALUES.put(int.class, 0);
		INITIAL_VALUES.put(long.class, 0L);
		INITIAL_VALUES.put(short.class, Short.valueOf("0"));
		INITIAL_VALUES.put(byte.class, Byte.valueOf("0"));
		INITIAL_VALUES.put(float.class, Float.valueOf("0.0"));
		INITIAL_VALUES.put(double.class, Double.valueOf("0.0"));
	}

	/** Private Constructor */
	private Reflect() {
	}

	// **************************************************
	// Multiple
	// **************************************************

	public static boolean isMultiple(Class<?> type) {
		return type.isArray() || (Collection.class.isAssignableFrom(type));
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

	// **************************************************
	// Fields
	// **************************************************

	/**
	 * Returns the internal type domainOf a Collection or Array if is multiple. If not
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

	/**
	 * Returna all fields presents in a class including the superclasses fields.
	 * 
	 * @param type
	 *            The class
	 * @return The field list.
	 */
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

	// **************************************************
	// New Instance
	// **************************************************

	/**
	 * Creates a new instance from a type.
	 * <P>
	 * Constructor Strategy Precedence:<BR>
	 * 1 - Try the default constructor <BR>
	 * 2 - If the type is Serializable try unserialization<BR>
	 * 3 - Use the constructor with less parameters
	 * 
	 * @param type
	 *            The type
	 * @return The new instance
	 */
	public static Object newInstance(Class<?> type) {
		try {
			if (type.isInterface()) {
				return newInstanceFromInterface(type);
			}
			Constructor<?> c = getMostSimpleConstructor(type);
			if (!Modifier.isPublic(c.getModifiers())) {
				c.setAccessible(true);
			}
			if (c.getParameterCount() == 0) {
				return c.newInstance();
			}
			if (Serializable.class.isAssignableFrom(type)) {
				return instantiateUsingSerialization(type);
			} else {
				return c.newInstance(getInitialParams(c));
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new PplReflectionException(e.getMessage(), e);
		}
	}

	private static Object newInstanceFromInterface(Class<?> type) {
		if (List.class.equals(type)) {
			return new ArrayList<Object>();
		} else if (Set.class.equals(type) || Collection.class.equals(type)) {
			return new HashSet<Object>();
		} else if (Map.class.equals(type)) {
			return new HashMap<String, Object>();
		}
		throw new PplReflectionException("Is not possible create a new instance from the interface " + type);
	}

	public static Object[] getInitialParams(Constructor<?> c) {
		Object[] params = new Object[c.getParameterCount()];
		for (int i = 0; i < params.length; i++) {
			if (c.getParameterTypes()[i].isPrimitive()) {
				params[i] = INITIAL_VALUES.get(c.getParameterTypes()[i]);
			} else {
				params[i] = null;
			}
		}
		return params;
	}

	// From XStream
	public static Object instantiateUsingSerialization(Class<?> type) {
		try {
			byte[] data;
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream stream = new DataOutputStream(bytes);
			stream.writeShort(ObjectStreamConstants.STREAM_MAGIC);
			stream.writeShort(ObjectStreamConstants.STREAM_VERSION);
			stream.writeByte(ObjectStreamConstants.TC_OBJECT);
			stream.writeByte(ObjectStreamConstants.TC_CLASSDESC);
			stream.writeUTF(type.getName());
			stream.writeLong(ObjectStreamClass.lookup(type).getSerialVersionUID());
			stream.writeByte(2); // classDescFlags (2 = Serializable)
			stream.writeShort(0); // field size
			stream.writeByte(ObjectStreamConstants.TC_ENDBLOCKDATA);
			stream.writeByte(ObjectStreamConstants.TC_NULL);
			data = bytes.toByteArray();

			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
			return in.readObject();
		} catch (IOException e) {
			throw new PplReflectionException("Cannot create " + type.getName() + " by JDK serialization", e);
		} catch (ClassNotFoundException e) {
			throw new PplReflectionException("Cannot find class " + e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public static Constructor getMostSimpleConstructor(Class<?> toClass) {
		try {
			Constructor<?> c = toClass.getDeclaredConstructors()[0];
			for (Constructor tmp : toClass.getDeclaredConstructors()) {
				if (tmp.getParameterCount() == 0) {
					return tmp;
				}
				if (tmp.getParameterCount() < c.getParameterCount()) {
					c = tmp;
				}
			}
			return c;
		} catch (SecurityException e) {
			throw new PplReflectionException(e.getMessage(), e);
		}
	}

}
