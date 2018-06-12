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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import br.net.buzu.pplspec.annotation.PplIgnore;
import br.net.buzu.pplspec.annotation.PplUse;
import br.net.buzu.pplspec.api.SkipStrategy;
import br.net.buzu.pplspec.exception.PplReflectionException;
import br.net.buzu.util.Reflect;

/**
 * Indicates if a field must be skipped
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicSkipStrategy implements SkipStrategy {

	private final SkipStrategy next;
	private final Class<? extends Annotation> ignoreAnnotation;
	private final Class<? extends Annotation> useAnnotation;

	public BasicSkipStrategy(Class<? extends Annotation> ignoreAnnotation, Class<? extends Annotation> useAnnotation,
			SkipStrategy next) {
		super();
		this.ignoreAnnotation = ignoreAnnotation;
		this.useAnnotation = useAnnotation;
		this.next = next;
	}

	public BasicSkipStrategy(SkipStrategy next) {
		this(PplIgnore.class, PplUse.class, next);
	}
	
	public BasicSkipStrategy() {
		this(null);
	}


	/**
	 * Indicates if the field must be skipped
	 * 
	 * @param field
	 * @return
	 */
	@Override
	public boolean skip(Field field) {
		// Precedence 1: Field explict ignore
		if (field.isAnnotationPresent(ignoreAnnotation)) {
			return true;
		}

		// Precedence 2: Field explicit use
		if (field.isAnnotationPresent(useAnnotation)) {
			return false;
		}

		// Precedence 3: Static, Transient or Ignore
		if (Modifier.isStatic(field.getModifiers())) {
			return true;
		}
		if (Modifier.isTransient(field.getModifiers())) {
			return true;
		}

		// Precedence 4: Field Class
		Class<?> fieldClass;
		try {
			fieldClass = Reflect.getElementType(field);
		} catch (PplReflectionException pre) {
			fieldClass = field.getClass();
		}
		if (fieldClass.isAnnotationPresent(ignoreAnnotation)) {
			return true;
		}

		return next != null ? next.skip(field) : false;
	}

}
