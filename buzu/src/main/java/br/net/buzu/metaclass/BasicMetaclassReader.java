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

import br.net.buzu.context.BasicContext;
import br.net.buzu.pplspec.annotation.PplMetadata;
import br.net.buzu.pplspec.annotation.PplParser;
import br.net.buzu.pplspec.api.MetaclassReader;
import br.net.buzu.pplspec.api.PayloadMapper;
import br.net.buzu.pplspec.api.SkipStrategy;
import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.exception.PplException;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Subtype;
import br.net.buzu.util.Reflect;
import br.net.buzu.util.StaticBehave;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static br.net.buzu.pplspec.model.KindKt.kindOf;

/**
 * Basic implementation domainOf <code>MetaclassReader</code>.
 *
 * @author Douglas Siviotti
 * @since 1.0
 * @see MetaclassReader
 *
 */
public class BasicMetaclassReader implements MetaclassReader {

	private final PplContext context;
	private final SkipStrategy skip;

	public BasicMetaclassReader() {
		this(new BasicContext());
	}

	public BasicMetaclassReader(PplContext context) {
		this(context, new BasicSkipStrategy());
	}

	public BasicMetaclassReader(PplContext context, SkipStrategy skip) {
		super();
		this.context = Objects.requireNonNull(context, "'context' cannot be null");
		this.skip = Objects.requireNonNull(skip, "'skip' cannot be null");
	}

	// **************************************************
	// API
	// **************************************************

	@Override
	public Metaclass read(Class<?> type) {
		return read(type, extractElementType(type));
	}

	@Override
	public Metaclass read(Class<?> type, Class<?> elementType) {
		PplMetadata pplMetadata = elementType.getAnnotation(PplMetadata.class);
		PplParser pplParser = elementType.getAnnotation(PplParser.class);
		Class<? extends PayloadMapper> parserType = pplParser != null ? pplParser.value() : null;
		return createMetaclass(Syntax.EMPTY, type, elementType, pplMetadata, null, parserType);
	}

	// **************************************************
	// Internal
	// **************************************************
	private Metaclass read(String parentId, Field field) {
		// Precedence 1: Field Annotation
		PplMetadata pplMetadata = field.getAnnotation(PplMetadata.class);
		PplParser pplParser = field.getAnnotation(PplParser.class);
		// Precedence 2: If null, use field Type Annotation
		Class<?> elementType = Reflect.getElementType(field);
		if (pplMetadata == null) {
			pplMetadata = elementType.getAnnotation(PplMetadata.class);
		}
		if (pplParser == null) {
			pplParser = elementType.getAnnotation(PplParser.class);
		}
		return createMetaclass(parentId, field.getType(), elementType, pplMetadata, field,
				pplParser != null ? pplParser.value() : null);
	}

	private Metaclass createMetaclass(String parentId, Class<?> fieldType, Class<?> elementType,
			PplMetadata pplMetadata, Field field, Class<? extends PayloadMapper> parserType) {
		boolean multiple = Reflect.isMultiple(fieldType);
		boolean complex = !context.subtypeManager().isSimple(elementType);
		MetaInfo metaInfo = createMetaInfo(parentId, pplMetadata, elementType, field != null ? field.getName() : "");
		List<Metaclass> children = complex ? createChildren(parentId, elementType) : null;

		if (complex) {
			return StaticBehave.isStaticChidren(children)
					? new ComplexStaticMetaclass(field, fieldType, elementType, kindOf(multiple, complex), metaInfo,
							parserType, children)

					: new ComplexMetaclass(field, fieldType, elementType, kindOf(multiple, complex), metaInfo,
							parserType, children);
		} else {
			return metaInfo.isStatic()
					? new SimpleStaticMetaclass(field, fieldType, elementType, kindOf(multiple, complex), metaInfo,
							parserType)

					: new SimpleMetaclass(field, fieldType, elementType, kindOf(multiple, complex), metaInfo,
							parserType);
		}
	}

	private MetaInfo createMetaInfo(String parentId, PplMetadata pplMetadata, Class<?> elementType, String fieldName) {
		Subtype subtype = context.subtypeManager().fromType(elementType);
		return pplMetadata != null ? new MetaInfo(parentId, pplMetadata, fieldName, subtype)
				: new MetaInfo(parentId, 0, fieldName, subtype, PplMetadata.EMPTY_INTEGER, PplMetadata.EMPTY_INTEGER,
						Syntax.DEFAULT_MIN_OCCURS, PplMetadata.EMPTY_INTEGER);
	}

	private List<Metaclass> createChildren(String parentId, Class<?> parentType) {
		if (context.subtypeManager().isSimple(parentType)) {
			return null;
		}
		List<Metaclass> list = new ArrayList<>();
		for (Field field : Reflect.getAllFields(parentType)) {
			if (skip.skip(field)) {
				continue;
			}
			list.add(read(parentId, field));
		}
		list.sort(Comparator.comparing(m -> m.info().getIndex()));
		return list;
	}

	private Class<?> extractElementType(Class<?> fieldType) {
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

	// ********** get/set **********

	public PplContext context() {
		return context;
	}

	public SkipStrategy skipStrategy() {
		return skip;
	}

}
