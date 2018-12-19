package br.net.buzu.pplimpl.jvm

import br.net.buzu.java.model.Subtype
import java.lang.IllegalArgumentException

internal typealias Serializer = (value: Any) -> String

val stringSerializer: Serializer = { it.toString().trim() }
val charSerializer: Serializer = { it.toString() }
val integerSerializer: Serializer = { it.toString() }
val longSerializer: Serializer = { it.toString() }
val numberSerializer: Serializer = { it.toString() }

val TIME_OFFSET = 11

val INTERNAL_SERIALIZER_MAPPING = createSerializerArray()

fun createSerializerArray(): Array<Serializer?> {
    val array = arrayOfNulls<Serializer>(Subtype.values().size + TIME_OFFSET)

    // Var Size (1 Parser per Subtype)
    array[Subtype.CHAR.ordinal] = charSerializer
    array[Subtype.STRING.ordinal] = stringSerializer
    array[Subtype.NUMBER.ordinal] = numberSerializer
    array[Subtype.INTEGER.ordinal] = integerSerializer
    array[Subtype.LONG.ordinal] = longSerializer

    // Fixed Size (Many parsers per Subtype)
    array[Subtype.BOOLEAN.ordinal] = charSerializer
    array[Subtype.BOZ.ordinal] = charSerializer
    array[Subtype.BTF.ordinal] = charSerializer
    array[Subtype.BYN.ordinal] = charSerializer
    array[Subtype.BSN.ordinal] = charSerializer

    // Timestamp
    array[Subtype.TIMESTAMP.ordinal] = charSerializer
    array[Subtype.TIMESTAMP_AND_MILLIS.ordinal] = charSerializer
    array[Subtype.ISO_TIMESTAMP.ordinal] = charSerializer
    array[Subtype.UTC_TIMESTAMP.ordinal] = charSerializer
    // Date
    array[Subtype.DATE.ordinal] = charSerializer
    array[Subtype.ISO_DATE.ordinal] = charSerializer
    array[Subtype.UTC_DATE.ordinal] = charSerializer
    // Time
    array[Subtype.TIME.ordinal] = charSerializer
    array[Subtype.TIME_AND_MILLIS.ordinal] = charSerializer
    array[Subtype.ISO_TIME.ordinal] = charSerializer
    array[Subtype.UTC_TIME.ordinal] = charSerializer

    // OLD Timestamp
    array[TIME_OFFSET + Subtype.TIMESTAMP.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.TIMESTAMP_AND_MILLIS.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.ISO_TIMESTAMP.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.UTC_TIMESTAMP.ordinal] = charSerializer
    // OLD Date
    array[TIME_OFFSET + Subtype.DATE.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.ISO_DATE.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.UTC_DATE.ordinal] = charSerializer
    // OLD Time
    array[TIME_OFFSET + Subtype.TIME.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.TIME_AND_MILLIS.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.ISO_TIME.ordinal] = charSerializer
    array[TIME_OFFSET + Subtype.UTC_TIME.ordinal] = charSerializer

    return array
}


internal fun getSerializer(subtype: Subtype, useOldDate: Boolean): Serializer {
    val index = if (useOldDate) subtype.ordinal + TIME_OFFSET else subtype.ordinal
    return INTERNAL_SERIALIZER_MAPPING[index]
            ?: throw IllegalArgumentException("Missing Serializer for subtype '$subtype'")
}

