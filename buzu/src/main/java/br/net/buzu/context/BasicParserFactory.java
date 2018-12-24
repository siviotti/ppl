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

import br.net.buzu.parsing.complex.ComplexMapper;
import br.net.buzu.parsing.simple.EnumNameMapper;
import br.net.buzu.parsing.simple.EnumPplSerializableMapper;
import br.net.buzu.parsing.simple.bool.*;
import br.net.buzu.parsing.simple.number.IntegerMapper;
import br.net.buzu.parsing.simple.number.LongMapper;
import br.net.buzu.parsing.simple.number.NumberMapper;
import br.net.buzu.parsing.simple.oldtime.*;
import br.net.buzu.parsing.simple.text.CharMapper;
import br.net.buzu.parsing.simple.text.StringMapper;
import br.net.buzu.parsing.simple.time.*;
import br.net.buzu.api.PayloadMapper;
import br.net.buzu.context.ParserFactory;
import br.net.buzu.model.Metaclass;
import br.net.buzu.model.Subtype;
import br.net.buzu.util.Reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic Implementation for ParserFactory
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicParserFactory implements ParserFactory {
	
	public static final ParserFactory INSTANCE = new BasicParserFactory();

	public static final int TIME_OFFSET = 11;

	private static final PayloadMapper[] PARSER_ARRAY = new PayloadMapper[Subtype.values().length + TIME_OFFSET];

	static {

		// Var Size (1 ValueParser per Subtype)
		PARSER_ARRAY[Subtype.CHAR.ordinal()] = CharMapper.INSTANCE;
		PARSER_ARRAY[Subtype.STRING.ordinal()] = StringMapper.INSTANCE;
		PARSER_ARRAY[Subtype.NUMBER.ordinal()] = NumberMapper.INSTANCE;
		PARSER_ARRAY[Subtype.INTEGER.ordinal()] = IntegerMapper.INSTANCE;
		PARSER_ARRAY[Subtype.LONG.ordinal()] = LongMapper.INSTANCE;

		// Fixed Size (Many parsers per Subtype)
		PARSER_ARRAY[Subtype.BOOLEAN.ordinal()] = BooleanMapper.INSTANCE;
		PARSER_ARRAY[Subtype.BOZ.ordinal()] = BozMapper.INSTANCE;
		PARSER_ARRAY[Subtype.BTF.ordinal()] = BtfMapper.INSTANCE;
		PARSER_ARRAY[Subtype.BYN.ordinal()] = BynMapper.INSTANCE;
		PARSER_ARRAY[Subtype.BSN.ordinal()] = BsnMapper.INSTANCE;

		// Timestamp
		PARSER_ARRAY[Subtype.TIMESTAMP.ordinal()] = TimestampMapper.INSTANCE;
		PARSER_ARRAY[Subtype.TIMESTAMP_AND_MILLIS.ordinal()] = TimestampAndMillisMapper.INSTANCE;
		PARSER_ARRAY[Subtype.ISO_TIMESTAMP.ordinal()] = IsoTimestampMapper.INSTANCE;
		PARSER_ARRAY[Subtype.UTC_TIMESTAMP.ordinal()] = UtcTimestampMapper.INSTANCE;
		// Date
		PARSER_ARRAY[Subtype.DATE.ordinal()] = DateMapper.INSTANCE;
		PARSER_ARRAY[Subtype.ISO_DATE.ordinal()] = IsoDateMapper.INSTANCE;
		PARSER_ARRAY[Subtype.UTC_DATE.ordinal()] = UtcDateMapper.INSTANCE;
		// Time
		PARSER_ARRAY[Subtype.TIME.ordinal()] = TimeMapper.INSTANCE;
		PARSER_ARRAY[Subtype.TIME_AND_MILLIS.ordinal()] = TimestampAndMillisMapper.INSTANCE;
		PARSER_ARRAY[Subtype.ISO_TIME.ordinal()] = IsoTimeMapper.INSTANCE;
		PARSER_ARRAY[Subtype.UTC_TIME.ordinal()] = UtcTimeMapper.INSTANCE;

		// OLD Timestamp
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIMESTAMP.ordinal()] = OldTimestampMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIMESTAMP_AND_MILLIS.ordinal()] = OldTimestampAndMillisMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.ISO_TIMESTAMP.ordinal()] = OldIsoTimestampMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.UTC_TIMESTAMP.ordinal()] = OldUtcTimestampMapper.INSTANCE;
		// OLD Date
		PARSER_ARRAY[TIME_OFFSET + Subtype.DATE.ordinal()] = OldDateMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.ISO_DATE.ordinal()] = OldIsoDateMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.UTC_DATE.ordinal()] = OldUtcDateMapper.INSTANCE;
		// OLD Time
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIME.ordinal()] = OldTimeMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIME_AND_MILLIS.ordinal()] = OldTimestampAndMillisMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.ISO_TIME.ordinal()] = OldIsoTimeMapper.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.UTC_TIME.ordinal()] = OldUtcTimeMapper.INSTANCE;

	}
	
	@Override
	public PayloadMapper create(Metaclass metaclass) {
		if (metaclass.hasCustomParser()) {
			return (PayloadMapper) Reflect.newInstance(metaclass.mapperType());
		}
		return metaclass.kind().isComplex() ? createComplex(metaclass) : createSimple(metaclass);
	}

	protected PayloadMapper createSimple(Metaclass metaclass) {
		if (metaclass.isEnum()) {
			if (metaclass.isPplSerializable()){
				return EnumPplSerializableMapper.INSTANCE;
			}
			return EnumNameMapper.INSTANCE;
		}
		int parserIndex = metaclass.info().getSubtype().ordinal();
		if (metaclass.isOldDate()) {
			parserIndex += TIME_OFFSET;
		}
		return PARSER_ARRAY[parserIndex];
	}

	protected PayloadMapper createComplex(Metaclass metaClass) {
		List<PayloadMapper> children = new ArrayList<>();
		metaClass.children().forEach(m-> children.add(create((Metaclass) m)));
		return new ComplexMapper(children);
	}

}
