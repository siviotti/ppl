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
package br.net.buzu.parsing.simple.number;

import java.math.BigDecimal;

import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.exception.PplTypeMismatchException;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * Parser to Type.NUMBER
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class NumberParser extends AbstractNumericParser {

	public static final NumberParser INSTANCE = new NumberParser();
	
	private final int round;
	
	private NumberParser() {
		this(BigDecimal.ROUND_DOWN);
	}

	public NumberParser(int round) {
		super();
		this.round = round;
	}

	// ******************** PARSE ********************
	@Override
	protected Object asSingleObject(StaticMetadata metadata, String text, Metaclass toClass) {
		try {
			if (toClass.match(BigDecimal.class)) {
				if (metadata.info().hasScale()) {
					return new BigDecimal(text).setScale(metadata.info().scale(), round);
				}
				return new BigDecimal(text);
			} else if (toClass.match(Double.class) || toClass.match(double.class)) {
				return Double.parseDouble(text);
			} else if (toClass.match(Float.class) || toClass.match(float.class)) {
				return Float.parseFloat(text);
			}
			throw new PplTypeMismatchException(toClass.elementType(), text, BigDecimal.class, Double.class,
					double.class, Float.class, float.class);
		} catch (NumberFormatException nfe) {
			throw new PplParseException(getClass().getSimpleName(), text, toClass.elementType(), nfe);
		}
	}

	// ******************** SERIALIZE ********************

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		if (obj instanceof BigDecimal) {
			if (meta.hasScale()) {
				return ((BigDecimal) obj).setScale(meta.scale(), round).toPlainString();
			}
			return ((BigDecimal) obj).toPlainString();
		}
		if (obj instanceof Number) {
			return ((Number) obj).toString();
		}
		throw new PplTypeMismatchException(
				"The value '" + obj + "' is not a valid number! The class '" + obj.getClass().getCanonicalName()
						+ "' is not a Number class: BigDecimal or Number (Double, double, Float, float etc)");
	}
}
