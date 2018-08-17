package br.net.buzu.pplspec.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import br.net.buzu.pplspec.exception.PplException;
import br.net.buzu.pplspec.exception.PplReflectionException;
import br.net.buzu.util.Reflect;

/**
 * 
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class TypeAdapter {

	private final Class<?> elementType;


	public TypeAdapter(Class<?> elementType) {
		super();
		this.elementType = elementType;
	}
	
	public static TypeAdapter create(Field field) {
		return new FieldTypeAdapter(Reflect.getElementType(field), field);
	}

	public static TypeAdapter create(Class<?> type) {
		return new RootTypeAdapter(extractElementType(type), type);
	}

	public static TypeAdapter create(Class<?> type, Class<?> elementType) {
		return new RootTypeAdapter(elementType, type);
	}

	private static Class<?> extractElementType(Class<?> fieldType) {
		if (Collection.class.isAssignableFrom(fieldType)) {
			if (!(fieldType.getGenericSuperclass() instanceof ParameterizedType)) {
				return Object.class;
			}
			ParameterizedType parType = (ParameterizedType) fieldType.getGenericSuperclass();
			if (parType.getActualTypeArguments().length < 1) {
				return Object.class;
			}
			Type itemType = parType.getActualTypeArguments()[0];
			try {
				return Class.forName(itemType.getTypeName());
			} catch (ClassNotFoundException e) {
				throw new PplException(e);
			}
		} else if (fieldType.isArray()) {
			return fieldType.getComponentType();
		} else {
			return fieldType;
		}
	}

	// ********** API **********
	
	public abstract String fieldName();

	public abstract Class<?> fieldType();
	
	public abstract Field field();
	
	public Class<?> elementType() {
		return elementType;
	}
	
	public void set(Object object, Object value) {
		try {
			field().set(object, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new PplReflectionException(e);
		}
	}

	public Object get(Object object) {
		try {
			return field().get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new PplReflectionException(e);
		}
	}
}
