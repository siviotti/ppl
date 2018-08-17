package br.net.buzu.pplspec.model;

import java.lang.reflect.Field;

/**
 * 
 * @author Douglas Siviotti
 * @since 1.0
 */
public class RootTypeAdapter extends TypeAdapter {

	private final Class<?> rootType;

	public RootTypeAdapter(Class<?> elementType, Class<?> rootType) {
		super(elementType);
		this.rootType = rootType;
	}

	@Override
	public String fieldName() {
		return "";
	}

	@Override
	public Class<?> fieldType() {
		return rootType;
	}

	@Override
	public Field field() {
		throw new UnsupportedOperationException("RoorTypeAdapter has no Field");
	}

}
