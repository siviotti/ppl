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
package br.net.buzu.pplspec.exception;

import java.util.Arrays;

/**
 * Thrown when there is a type mismatch between a value and an metadata type.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class PplTypeMismatchException extends PplException{

	private static final long serialVersionUID = 1L;

	public PplTypeMismatchException(Class<?> toClass, String text, Class<?> ... expected) {
		super("Type mismatch:" + text + "\nSupported list : " + Arrays.asList(expected) + " \nfound (toClass): " + toClass.getCanonicalName());
	}

	
	public PplTypeMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public PplTypeMismatchException(String message) {
		super(message);

	}

	public PplTypeMismatchException(Throwable cause) {
		super(cause);

	}

}
