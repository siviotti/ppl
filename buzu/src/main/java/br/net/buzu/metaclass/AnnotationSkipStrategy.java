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

import br.net.buzu.java.api.SkipStrategy;
import br.net.buzu.java.exception.PplReflectionException;
import br.net.buzu.util.Reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Indicates if a field must be skipped
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class AnnotationSkipStrategy extends BasicSkipStrategy {

	private final Set<Class<? extends Annotation>> ignoreAnnotations = new HashSet<>();
	private final Set<Class<? extends Annotation>> useAnnotations = new HashSet<>();

	public AnnotationSkipStrategy(@SuppressWarnings("unchecked") Class<? extends Annotation>... ignoreAnnotations) {
		this(createSet(ignoreAnnotations), null, null);
	}

	public AnnotationSkipStrategy(Set<Class<? extends Annotation>> ignoreAnnotations,
			Set<Class<? extends Annotation>> useAnnotations) {
		this(ignoreAnnotations, useAnnotations, new BasicSkipStrategy());
	}

	public AnnotationSkipStrategy(Set<Class<? extends Annotation>> ignoreAnnotations,
			Set<Class<? extends Annotation>> useAnnotations, SkipStrategy next) {
		super(next);
		if (ignoreAnnotations != null) {
			this.ignoreAnnotations.addAll(ignoreAnnotations);
		}
		if (useAnnotations != null) {
			this.useAnnotations.addAll(useAnnotations);
		}
	}

	private static Set<Class<? extends Annotation>> createSet(Class<? extends Annotation>[] annotationClasses) {
		Set<Class<? extends Annotation>> set = new HashSet<>();
		if (annotationClasses != null) {
			for (Class<? extends Annotation> clazz : annotationClasses) {
				set.add(clazz);
			}
		}
		return set;
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
		for (Class<? extends Annotation> annotation : ignoreAnnotations) {
			if (field.isAnnotationPresent(annotation)) {
				return true;
			}
		}

		// Precedence 2: Field explicit use
		for (Class<? extends Annotation> annotation : useAnnotations) {
			if (field.isAnnotationPresent(annotation)) {
				return false;
			}
		}

		Class<?> fieldClass;
		try {
			fieldClass = Reflect.getElementType(field);
		} catch (PplReflectionException pre) {
			fieldClass = field.getClass();
		}
		
		// Precedence 3: Field class explict ignore
		for (Class<? extends Annotation> annotation : ignoreAnnotations) {
			if (fieldClass.isAnnotationPresent(annotation)) {
				return true;
			}
		}

		// Precedence 4: Field class explict use
		for (Class<? extends Annotation> annotation : useAnnotations) {
			if (fieldClass.isAnnotationPresent(annotation)) {
				return false;
			}
		}

		return super.skip(field);
	}

}
