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
package br.net.buzu.context;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.net.buzu.pplspec.context.SubtypeManager;
import br.net.buzu.pplspec.model.Subtype;

/**
 * Stateless SubType Manager.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicSubtypeManager implements SubtypeManager {

	public static final SubtypeManager INSTANCE = new BasicSubtypeManager();

	static final String SUBTYPE_NOT_FOUND = "Subtype not found:";
	private static final Map<Class<?>, Subtype> INTERNAL_MAP = new HashMap<>();
	static {
		// Simple Types
		INTERNAL_MAP.put(String.class, Subtype.STRING);
		INTERNAL_MAP.put(Integer.class, Subtype.INTEGER);
		INTERNAL_MAP.put(BigInteger.class, Subtype.INTEGER);
		INTERNAL_MAP.put(int.class, Subtype.INTEGER);
		INTERNAL_MAP.put(Boolean.class, Subtype.BOOLEAN);
		INTERNAL_MAP.put(boolean.class, Subtype.BOOLEAN);
		INTERNAL_MAP.put(Date.class, Subtype.TIMESTAMP);
		INTERNAL_MAP.put(LocalDateTime.class, Subtype.TIMESTAMP);
		INTERNAL_MAP.put(LocalDate.class, Subtype.DATE);
		INTERNAL_MAP.put(LocalTime.class, Subtype.TIME);
		INTERNAL_MAP.put(Period.class, Subtype.PERIOD);
		INTERNAL_MAP.put(BigDecimal.class, Subtype.NUMBER);
		INTERNAL_MAP.put(Double.class, Subtype.NUMBER);
		INTERNAL_MAP.put(double.class, Subtype.NUMBER);
		INTERNAL_MAP.put(Float.class, Subtype.NUMBER);
		INTERNAL_MAP.put(float.class, Subtype.NUMBER);
		INTERNAL_MAP.put(Long.class, Subtype.LONG);
		INTERNAL_MAP.put(BigInteger.class, Subtype.LONG);
		INTERNAL_MAP.put(long.class, Subtype.LONG);
		INTERNAL_MAP.put(Byte.class, Subtype.INTEGER);
		INTERNAL_MAP.put(byte.class, Subtype.INTEGER);
		INTERNAL_MAP.put(Short.class, Subtype.INTEGER);
		INTERNAL_MAP.put(short.class, Subtype.INTEGER);
		INTERNAL_MAP.put(Character.class, Subtype.CHAR);
		INTERNAL_MAP.put(char.class, Subtype.CHAR);
	}

	/**
	 * Creates a instance of <code>SubType</code> based on a Class.
	 * 
	 * @param typeClass
	 *            The class
	 * @return The correct instance relative to the class.
	 */
	public Subtype fromType(Class<?> typeClass) {
		if (typeClass.isEnum()) {
			return Subtype.STRING;
		}
		Subtype simpleType = INTERNAL_MAP.get(typeClass);
		return simpleType != null ? simpleType : Subtype.OBJ;
	}

	public boolean isSimple(Class<?> type) {
		return INTERNAL_MAP.containsKey(type) || type.isEnum();
	}

	/**
	 * Creates a instance of <code>SubType</code> based on a text
	 * representation.
	 * 
	 * @param text
	 *            The text representation
	 * @param indicates
	 *            if the Metadata is complex
	 * @return The correct instance relative to text or <code>null</code> if the
	 *         text is invalid.
	 */
	public Subtype fromText(String text, boolean complex) {
		if (text == null || text.isEmpty()) {
			return complex ? Subtype.DEFAULT_COMPLEX : Subtype.DEFAULT_SINGLE;
		}
		return Subtype.get(text);
	}
}
