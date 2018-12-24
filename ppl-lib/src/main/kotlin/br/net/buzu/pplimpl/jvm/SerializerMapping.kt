package br.net.buzu.pplimpl.jvm

import br.net.buzu.java.model.Subtype
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


// Var Size
val charSerializer: ValueSerializer = { it.toString() }
val stringSerializer: ValueSerializer = { it.toString() }
val numberSerializer: ValueSerializer = { if (it is BigDecimal) it.toPlainString() else it.toString() }
val integerSerializer: ValueSerializer = { it.toString() }
val longSerializer: ValueSerializer = { it.toString() }

// Fixed Size

// Boolean
val booleanSerializer: ValueSerializer = { it.toString() }
val bozSerializer: ValueSerializer = { if (it as Boolean) "1" else "0"}
val btfSerializer: ValueSerializer = { it.toString() }
val bynSerializer: ValueSerializer = { it.toString() }
val bsnSerializer: ValueSerializer = { it.toString() }

// Timestamp
val timestampSerializer: ValueSerializer = { it.toString() }
val timestampAndMillisSerializer: ValueSerializer = { it.toString() }
val isoTimestampSerializer: ValueSerializer = { it.toString() }
val utcTimestampSerializer: ValueSerializer = { it.toString() }

// Date
val dateSerializer: ValueSerializer = { (it as LocalDate).format(DateTimeFormatter.BASIC_ISO_DATE) }
val isoDateSerializer: ValueSerializer = { (it as LocalDate).format(DateTimeFormatter.ISO_DATE) }
val utcDateSerializer: ValueSerializer = { (it as LocalDate).format(DateTimeFormatter.ISO_OFFSET_DATE)}

// Times
val timeSerializer: ValueSerializer = { it.toString() }
val timeAndMillisSerializer: ValueSerializer = { it.toString() }
val isoTimeSerializer: ValueSerializer = { it.toString() }
val utcTimeSerializer: ValueSerializer = { it.toString() }

// Old Timestamp
val oldTimestampSerializer: ValueSerializer = { it.toString() }
val oldTimestampAndMillisSerializer: ValueSerializer = { it.toString() }
val oldIsoTimestampSerializer: ValueSerializer = { it.toString() }
val oldUtcTimestampSerializer: ValueSerializer = { it.toString() }

// Old Date
val oldDateSerializer: ValueSerializer = { SimpleDateFormat("yyyy-MM-dd+HH:mm").format(it as Date) }
val oldIsoDateSerializer: ValueSerializer = { it.toString() }
val oldUtcDateSerializer: ValueSerializer = { it.toString() }

// Old Times
val oldTimeSerializer: ValueSerializer = { it.toString() }
val oldTimeAndMillisSerializer: ValueSerializer = { it.toString() }
val oldIsoTimeSerializer: ValueSerializer = { it.toString() }
val oldUtcTimeSerializer: ValueSerializer = { it.toString() }

const val TIME_OFFSET = 11

private val INTERNAL_SERIALIZER_MAPPING = createSerializerArray()

private fun createSerializerArray(): Array<ValueSerializer?> {
    val array = arrayOfNulls<ValueSerializer>(Subtype.values().size + TIME_OFFSET)

    // Var Size (1 ValueParser per Subtype)
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


internal fun getPayloadSerializer(subtype: Subtype, useOldDate: Boolean): ValueSerializer {
    val index = if (useOldDate) subtype.ordinal + TIME_OFFSET else subtype.ordinal
    return INTERNAL_SERIALIZER_MAPPING[index]
            ?: throw IllegalArgumentException("Missing ValueSerializer for subtype '$subtype'")
}

