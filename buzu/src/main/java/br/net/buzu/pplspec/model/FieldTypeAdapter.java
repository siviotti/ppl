package br.net.buzu.pplspec.model;

import java.lang.reflect.Field;

/**
 * @author Douglas Siviotti
 * @since 1.0
 */
public class FieldTypeAdapter extends TypeAdapter{

	private final Field field;
	
	public FieldTypeAdapter(Class<?> elementType, Field field) {
		super(elementType);
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public String fieldName() {
		return field.getName();
	}

	@Override
	public Class<?> fieldType() {
		return field.getType();
	}

	@Override
	public Field field() {
		return field;
	}

}
