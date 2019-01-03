package br.net.buzu.api

/**
 * Mapper to transform Positional Text to Payload Object and vice-versa.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface PositionalMapper {

    fun parse(text: String): Any?

    fun serialize(value: Any?): String

}