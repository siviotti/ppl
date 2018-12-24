package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.Subtype
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


val charParser: ValueParser = { text, metaInfo -> text }
val stringParser: ValueParser = { text, metaInfo -> text.trim { fillChar -> fillChar ==metaInfo.fillChar} }
val numberParser: ValueParser = { text, metaInfo -> 0.0 }
val integerParser: ValueParser = { text, metaInfo -> text.toInt() }
val longParser: ValueParser = { text, metaInfo -> text.toLong() }

// Fixed Size

// Boolean
val booleanParser: ValueParser = { text, metaInfo -> text.toBoolean() }
val bozParser: ValueParser = { text, metaInfo -> text }
val btfParser: ValueParser = { text, metaInfo -> text }
val bynParser: ValueParser = { text, metaInfo -> text }
val bsnParser: ValueParser = { text, metaInfo -> text }

// Timestamp
val timestampParser: ValueParser = { text, metaInfo -> text }
val timestampAndMillisParser: ValueParser = { text, metaInfo -> text }
val isoTimestampParser: ValueParser = { text, metaInfo -> text }
val utcTimestampParser: ValueParser = { text, metaInfo -> text }

// Date
val dateParser: ValueParser = { text, metaInfo -> LocalDate.parse(text, DateTimeFormatter.BASIC_ISO_DATE) }
val isoDateParser: ValueParser = { text, metaInfo -> DateTimeFormatter.ISO_DATE.parse(text) }
val utcDateParser: ValueParser = { text, metaInfo -> DateTimeFormatter.ISO_OFFSET_DATE.parse(text) }

// Times
val timeParser: ValueParser = { text, metaInfo -> text }
val timeAndMillisParser: ValueParser = { text, metaInfo -> text }
val isoTimeParser: ValueParser = { text, metaInfo -> text }
val utcTimeParser: ValueParser = { text, metaInfo -> text }

// Old Timestamp
val oldTimestampParser: ValueParser = { text, metaInfo -> text }
val oldTimestampAndMillisParser: ValueParser = { text, metaInfo -> text }
val oldIsoTimestampParser: ValueParser = { text, metaInfo -> text }
val oldUtcTimestampParser: ValueParser = { text, metaInfo -> text }

// Old Date
val oldDateParser: ValueParser = { text, metaInfo -> text }
val oldIsoDateParser: ValueParser = { text, metaInfo -> text }
val oldUtcDateParser: ValueParser = { text, metaInfo -> text }

// Old Times
val oldTimeParser: ValueParser = { text, metaInfo -> text }
val oldTimeAndMillisParser: ValueParser = { text, metaInfo -> text }
val oldIsoTimeParser: ValueParser = { text, metaInfo -> text }
val oldUtcTimeParser: ValueParser = { text, metaInfo -> text }

private val INTERNAL_PARSER_MAPPING = createParserArray()

private fun createParserArray(): Array<ValueParser?> {
    val array = arrayOfNulls<ValueParser>(Subtype.values().size + TIME_OFFSET)

    // Var Size (1 ValueParser per Subtype)
    array[Subtype.CHAR.ordinal] = charParser
    array[Subtype.STRING.ordinal] = stringParser
    array[Subtype.NUMBER.ordinal] = numberParser
    array[Subtype.INTEGER.ordinal] = integerParser
    array[Subtype.LONG.ordinal] = longParser

    // Fixed Size (Many parsers per Subtype)

    // Boolean
    array[Subtype.BOOLEAN.ordinal] = booleanParser
    array[Subtype.BOZ.ordinal] = bozParser
    array[Subtype.BTF.ordinal] = btfParser
    array[Subtype.BYN.ordinal] = bynParser
    array[Subtype.BSN.ordinal] = bsnParser

    // Timestamp
    array[Subtype.TIMESTAMP.ordinal] = timestampParser
    array[Subtype.TIMESTAMP_AND_MILLIS.ordinal] = timestampAndMillisParser
    array[Subtype.ISO_TIMESTAMP.ordinal] = isoTimestampParser
    array[Subtype.UTC_TIMESTAMP.ordinal] = utcTimestampParser
    // Date
    array[Subtype.DATE.ordinal] = dateParser
    array[Subtype.ISO_DATE.ordinal] = isoDateParser
    array[Subtype.UTC_DATE.ordinal] = utcDateParser
    // Time
    array[Subtype.TIME.ordinal] = timeParser
    array[Subtype.TIME_AND_MILLIS.ordinal] = timeAndMillisParser
    array[Subtype.ISO_TIME.ordinal] = isoTimeParser
    array[Subtype.UTC_TIME.ordinal] = utcTimeParser

    // OLD Timestamp
    array[TIME_OFFSET + Subtype.TIMESTAMP.ordinal] = oldTimestampParser
    array[TIME_OFFSET + Subtype.TIMESTAMP_AND_MILLIS.ordinal] = oldTimestampAndMillisParser
    array[TIME_OFFSET + Subtype.ISO_TIMESTAMP.ordinal] = oldIsoTimestampParser
    array[TIME_OFFSET + Subtype.UTC_TIMESTAMP.ordinal] = oldUtcTimestampParser
    // OLD Date
    array[TIME_OFFSET + Subtype.DATE.ordinal] = oldDateParser
    array[TIME_OFFSET + Subtype.ISO_DATE.ordinal] = oldIsoDateParser
    array[TIME_OFFSET + Subtype.UTC_DATE.ordinal] = oldUtcDateParser
    // OLD Time
    array[TIME_OFFSET + Subtype.TIME.ordinal] = oldTimeParser
    array[TIME_OFFSET + Subtype.TIME_AND_MILLIS.ordinal] = oldTimeAndMillisParser
    array[TIME_OFFSET + Subtype.ISO_TIME.ordinal] = oldIsoTimeParser
    array[TIME_OFFSET + Subtype.UTC_TIME.ordinal] = oldUtcTimeParser

    return array
}


internal fun getPayloadParser(subtype: Subtype, useOldDate: Boolean): ValueParser {
    val index = if (useOldDate) subtype.ordinal + TIME_OFFSET else subtype.ordinal
    return INTERNAL_PARSER_MAPPING[index]
            ?: throw IllegalArgumentException("Missing ValueParser for subtype '$subtype'")
}

