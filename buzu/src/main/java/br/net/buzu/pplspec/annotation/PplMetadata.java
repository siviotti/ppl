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
package br.net.buzu.pplspec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.model.Subtype;

/**
 * Metadata information about some annoted field. This information is mandatoty
 * over reflection information.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PplMetadata {

	public static final String EMPTY_NAME = "EMPTY NAME";
	public static final String EMPTY_STRING = Syntax.EMPTY;
	public static final int EMPTY_INTEGER = -1;
	public static final char EMPTY_CHAR = ' ';
	
	// Basic

	int index() default EMPTY_INTEGER;

	String name() default EMPTY_NAME;
	
	@SuppressWarnings("deprecation")
	Subtype subtype() default Subtype.EMPTY_SUBTYPE;

	int size() default EMPTY_INTEGER;
	
	int scale() default EMPTY_INTEGER;

	int minOccurs() default EMPTY_INTEGER;

	int maxOccurs() default EMPTY_INTEGER;
	
	// Extension
	
	String defaultValue() default EMPTY_STRING;
	
	String [] domain() default {};

	String tags() default EMPTY_STRING;
	
	boolean key() default false;
	
	boolean indexed() default false;
	
	char align() default EMPTY_CHAR;
	char fillChar() default EMPTY_CHAR;
	char nullChar() default EMPTY_CHAR;
	
}
