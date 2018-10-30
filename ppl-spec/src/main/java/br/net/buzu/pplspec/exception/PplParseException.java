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

/**
 * Exception rised durng the Parsing process. From Text to Object.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class PplParseException extends PplException {

	private static final long serialVersionUID = 1L;
	
	public PplParseException(String parser, String fromText, Class<?> toClass, Throwable cause) {
		super(parseMessage(parser, fromText, toClass), cause);
	}

	public PplParseException(String metadata, String text, Class<?> toClass) {
		this(metadata, text, toClass, null);
	}

	public PplParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PplParseException(String message) {
		super(message);
	}

	public PplParseException(Throwable cause) {
		super(cause);
	}
	
	private static String parseMessage(String parserName, String fromText, Class<?> toClass) {
		return "[" + parserName+"] Parsing error:\nfrom text:" + fromText + "\nto class :" + toClass;
	}


}
