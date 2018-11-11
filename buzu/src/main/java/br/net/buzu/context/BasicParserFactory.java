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

import java.util.ArrayList;
import java.util.List;

import br.net.buzu.parsing.complex.ComplexParser;
import br.net.buzu.parsing.simple.EnumNameParser;
import br.net.buzu.parsing.simple.EnumPplSerializableParser;
import br.net.buzu.parsing.simple.bool.BooleanParser;
import br.net.buzu.parsing.simple.bool.BozParser;
import br.net.buzu.parsing.simple.bool.BsnParser;
import br.net.buzu.parsing.simple.bool.BtfParser;
import br.net.buzu.parsing.simple.bool.BynParser;
import br.net.buzu.parsing.simple.number.IntegerParser;
import br.net.buzu.parsing.simple.number.LongParser;
import br.net.buzu.parsing.simple.number.NumberParser;
import br.net.buzu.parsing.simple.oldtime.OldDateParser;
import br.net.buzu.parsing.simple.oldtime.OldIsoDateParser;
import br.net.buzu.parsing.simple.oldtime.OldIsoTimeParser;
import br.net.buzu.parsing.simple.oldtime.OldIsoTimestampParser;
import br.net.buzu.parsing.simple.oldtime.OldTimeParser;
import br.net.buzu.parsing.simple.oldtime.OldTimestampAndMillisParser;
import br.net.buzu.parsing.simple.oldtime.OldTimestampParser;
import br.net.buzu.parsing.simple.oldtime.OldUtcDateParser;
import br.net.buzu.parsing.simple.oldtime.OldUtcTimeParser;
import br.net.buzu.parsing.simple.oldtime.OldUtcTimestampParser;
import br.net.buzu.parsing.simple.text.CharParser;
import br.net.buzu.parsing.simple.text.StringParser;
import br.net.buzu.parsing.simple.time.DateParser;
import br.net.buzu.parsing.simple.time.IsoDateParser;
import br.net.buzu.parsing.simple.time.IsoTimeParser;
import br.net.buzu.parsing.simple.time.IsoTimestampParser;
import br.net.buzu.parsing.simple.time.TimeParser;
import br.net.buzu.parsing.simple.time.TimestampAndMillisParser;
import br.net.buzu.parsing.simple.time.TimestampParser;
import br.net.buzu.parsing.simple.time.UtcDateParser;
import br.net.buzu.parsing.simple.time.UtcTimeParser;
import br.net.buzu.parsing.simple.time.UtcTimestampParser;
import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.context.ParserFactory;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Subtype;
import br.net.buzu.util.Reflect;

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

	private static final PayloadParser[] PARSER_ARRAY = new PayloadParser[Subtype.values().length + TIME_OFFSET];

	static {

		// Var Size (1 Parser per Subtype)
		PARSER_ARRAY[Subtype.CHAR.ordinal()] = CharParser.INSTANCE;
		PARSER_ARRAY[Subtype.STRING.ordinal()] = StringParser.INSTANCE;
		PARSER_ARRAY[Subtype.NUMBER.ordinal()] = NumberParser.INSTANCE;
		PARSER_ARRAY[Subtype.INTEGER.ordinal()] = IntegerParser.INSTANCE;
		PARSER_ARRAY[Subtype.LONG.ordinal()] = LongParser.INSTANCE;

		// Fixed Size (Many parsers per Subtype)
		PARSER_ARRAY[Subtype.BOOLEAN.ordinal()] = BooleanParser.INSTANCE;
		PARSER_ARRAY[Subtype.BOZ.ordinal()] = BozParser.INSTANCE;
		PARSER_ARRAY[Subtype.BTF.ordinal()] = BtfParser.INSTANCE;
		PARSER_ARRAY[Subtype.BYN.ordinal()] = BynParser.INSTANCE;
		PARSER_ARRAY[Subtype.BSN.ordinal()] = BsnParser.INSTANCE;

		// Timestamp
		PARSER_ARRAY[Subtype.TIMESTAMP.ordinal()] = TimestampParser.INSTANCE;
		PARSER_ARRAY[Subtype.TIMESTAMP_AND_MILLIS.ordinal()] = TimestampAndMillisParser.INSTANCE;
		PARSER_ARRAY[Subtype.ISO_TIMESTAMP.ordinal()] = IsoTimestampParser.INSTANCE;
		PARSER_ARRAY[Subtype.UTC_TIMESTAMP.ordinal()] = UtcTimestampParser.INSTANCE;
		// Date
		PARSER_ARRAY[Subtype.DATE.ordinal()] = DateParser.INSTANCE;
		PARSER_ARRAY[Subtype.ISO_DATE.ordinal()] = IsoDateParser.INSTANCE;
		PARSER_ARRAY[Subtype.UTC_DATE.ordinal()] = UtcDateParser.INSTANCE;
		// Time
		PARSER_ARRAY[Subtype.TIME.ordinal()] = TimeParser.INSTANCE;
		PARSER_ARRAY[Subtype.TIME_AND_MILLIS.ordinal()] = TimestampAndMillisParser.INSTANCE;
		PARSER_ARRAY[Subtype.ISO_TIME.ordinal()] = IsoTimeParser.INSTANCE;
		PARSER_ARRAY[Subtype.UTC_TIME.ordinal()] = UtcTimeParser.INSTANCE;

		// OLD Timestamp
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIMESTAMP.ordinal()] = OldTimestampParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIMESTAMP_AND_MILLIS.ordinal()] = OldTimestampAndMillisParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.ISO_TIMESTAMP.ordinal()] = OldIsoTimestampParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.UTC_TIMESTAMP.ordinal()] = OldUtcTimestampParser.INSTANCE;
		// OLD Date
		PARSER_ARRAY[TIME_OFFSET + Subtype.DATE.ordinal()] = OldDateParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.ISO_DATE.ordinal()] = OldIsoDateParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.UTC_DATE.ordinal()] = OldUtcDateParser.INSTANCE;
		// OLD Time
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIME.ordinal()] = OldTimeParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.TIME_AND_MILLIS.ordinal()] = OldTimestampAndMillisParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.ISO_TIME.ordinal()] = OldIsoTimeParser.INSTANCE;
		PARSER_ARRAY[TIME_OFFSET + Subtype.UTC_TIME.ordinal()] = OldUtcTimeParser.INSTANCE;

	}
	
	@Override
	public PayloadParser create(Metaclass metaclass) {
		if (metaclass.hasCustomParser()) {
			return (PayloadParser) Reflect.newInstance(metaclass.parserType());
		}
		return metaclass.kind().isComplex() ? createComplex(metaclass) : createSimple(metaclass);
	}

	protected PayloadParser createSimple(Metaclass metaclass) {
		if (metaclass.isEnum()) {
			if (metaclass.isPplSerializable()){
				return EnumPplSerializableParser.INSTANCE;
			}
			return EnumNameParser.INSTANCE;
		}
		int parserIndex = metaclass.info().getSubtype().ordinal();
		if (metaclass.isOldDate()) {
			parserIndex += TIME_OFFSET;
		}
		return PARSER_ARRAY[parserIndex];
	}

	protected PayloadParser createComplex(Metaclass metaClass) {
		List<PayloadParser> children = new ArrayList<>();
		metaClass.children().forEach(m-> children.add(create((Metaclass) m)));
		return new ComplexParser(children);
	}

}
