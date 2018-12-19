package br.net.buzu.pplimpl.jvm

import br.net.buzu.java.model.Subtype
import java.lang.IllegalArgumentException
import java.math.BigDecimal

internal typealias Serializer = (value: Any) -> String

// Var Size
val charSerializer: Serializer = { it.toString() }
val stringSerializer: Serializer = { it.toString() }
val numberSerializer: Serializer = { if (it is BigDecimal) it.toPlainString() else it.toString() }
val integerSerializer: Serializer = { it.toString() }
val longSerializer: Serializer = { it.toString() }

// Fixedx Size

// Boolean
val booleanSerializer: Serializer = { it.toString() }
val bozSerializer: Serializer = { it.toString() }
val btfSerializer: Serializer = { it.toString() }
val bynSerializer: Serializer = { it.toString() }
val bsnSerializer: Serializer = { it.toString() }

// Timestamp
val timestampSerializer: Serializer = { it.toString() }
val timestampAndMillisSerializer: Serializer = { it.toString() }
val isoTimestampSerializer: Serializer = { it.toString() }
val utcTimestampSerializer: Serializer = { it.toString() }

// Date
val dateSerializer: Serializer = { it.toString() }
val isoDateSerializer: Serializer = { it.toString() }
val utcDateSerializer: Serializer = { it.toString() }

// Times
val timeSerializer: Serializer = { it.toString() }
val timeAndMillisSerializer: Serializer = { it.toString() }
val isoTimeSerializer: Serializer = { it.toString() }
val utcTimeSerializer: Serializer = { it.toString() }

// Old Timestamp
val oldTimestampSerializer: Serializer = { it.toString() }
val oldTimestampAndMillisSerializer: Serializer = { it.toString() }
val oldIsoTimestampSerializer: Serializer = { it.toString() }
val oldUtcTimestampSerializer: Serializer = { it.toString() }

// Old Date
val oldDateSerializer: Serializer = { it.toString() }
val oldIsoDateSerializer: Serializer = { it.toString() }
val oldUtcDateSerializer: Serializer = { it.toString() }

// Old Times
val oldTimeSerializer: Serializer = { it.toString() }
val oldTimeAndMillisSerializer: Serializer = { it.toString() }
val oldIsoTimeSerializer: Serializer = { it.toString() }
val oldUtcTimeSerializer: Serializer = { it.toString() }

val TIME_OFFSET = 11

private val INTERNAL_SERIALIZER_MAPPING = createSerializerArray()

fun createSerializerArray(): Array<Serializer?> {
    val array = arrayOfNulls<Serializer>(Subtype.values().size + TIME_OFFSET)

    // Var Size (1 Parser per Subtype)
    array[Subtype.CHAR.ordinal] = charSerializer
    array[Subtype.STRING.ordinal] = stringSerializer
    array[Subtype.NUMBER.ordinal] = numberSerializer
    array[Subtype.INTEGER.ordinal] = integerSerializer
    array[Subtype.LONG.ordinal] = longSerializer

    // Fixed Size (Many parsers per Subtype)

    // Boolean
    array[Subtype.BOOLEAN.ordinal] = booleanSerializer
    array[Subtype.BOZ.ordinal] = bozSerializer
    array[Subtype.BTF.ordinal] = btfSerializer
    array[Subtype.BYN.ordinal] = bynSerializer
    array[Subtype.BSN.ordinal] = bsnSerializer

    // Timestamp
    array[Subtype.TIMESTAMP.ordinal] = timestampSerializer
    array[Subtype.TIMESTAMP_AND_MILLIS.ordinal] = timestampAndMillisSerializer
    array[Subtype.ISO_TIMESTAMP.ordinal] = isoTimestampSerializer
    array[Subtype.UTC_TIMESTAMP.ordinal] = utcTimestampSerializer
    // Date
    array[Subtype.DATE.ordinal] = dateSerializer
    array[Subtype.ISO_DATE.ordinal] = isoDateSerializer
    array[Subtype.UTC_DATE.ordinal] = utcDateSerializer
    // Time
    array[Subtype.TIME.ordinal] = timeSerializer
    array[Subtype.TIME_AND_MILLIS.ordinal] = timeAndMillisSerializer
    array[Subtype.ISO_TIME.ordinal] = isoTimeSerializer
    array[Subtype.UTC_TIME.ordinal] = utcTimeSerializer

    // OLD Timestamp
    array[TIME_OFFSET + Subtype.TIMESTAMP.ordinal] = oldTimestampSerializer
    array[TIME_OFFSET + Subtype.TIMESTAMP_AND_MILLIS.ordinal] = oldTimestampAndMillisSerializer
    array[TIME_OFFSET + Subtype.ISO_TIMESTAMP.ordinal] = oldIsoTimestampSerializer
    array[TIME_OFFSET + Subtype.UTC_TIMESTAMP.ordinal] = oldUtcTimestampSerializer
    // OLD Date
    array[TIME_OFFSET + Subtype.DATE.ordinal] = oldDateSerializer
    array[TIME_OFFSET + Subtype.ISO_DATE.ordinal] = oldIsoDateSerializer
    array[TIME_OFFSET + Subtype.UTC_DATE.ordinal] = oldUtcDateSerializer
    // OLD Time
    array[TIME_OFFSET + Subtype.TIME.ordinal] = oldTimeSerializer
    array[TIME_OFFSET + Subtype.TIME_AND_MILLIS.ordinal] = oldTimeAndMillisSerializer
    array[TIME_OFFSET + Subtype.ISO_TIME.ordinal] = oldIsoTimeSerializer
    array[TIME_OFFSET + Subtype.UTC_TIME.ordinal] = oldUtcTimeSerializer

    return array
}


internal fun getSerializer(subtype: Subtype, useOldDate: Boolean): Serializer {
    val index = if (useOldDate) subtype.ordinal + TIME_OFFSET else subtype.ordinal
    return INTERNAL_SERIALIZER_MAPPING[index]
            ?: throw IllegalArgumentException("Missing Serializer for subtype '$subtype'")
}

