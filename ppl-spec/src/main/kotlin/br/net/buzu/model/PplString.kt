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
package br.net.buzu.model


import br.net.buzu.exception.PplParseException
import br.net.buzu.lang.SUB_CLOSE
import br.net.buzu.lang.SUB_OPEN
import br.net.buzu.lang.pplExtractMetadata
import br.net.buzu.lang.pplToString
import java.io.Serializable
import java.text.ParseException

fun pplStringOf(text: String): PplString {
    return PplString(text)
}

/**
 * **Immutable** representation of a PPL String.
 *
 * <pre>
 * (METADATA) PAYLOAD
</pre> *
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class PplString internal constructor(val text: String) : Serializable {

    val metadata: String
    val payload: String

    val pplMetadata: String
        get() = metadata.substring(1, metadata.length - 1)

    init {
        if (text.isEmpty()) {
            throw IllegalArgumentException("text cannot be Empty!")
        }
        try {
            metadata = pplExtractMetadata(text)
            payload = text.substring(metadata.length)
        } catch (pe: ParseException) {
            throw PplParseException("PPL Parse error. Text:\n$text", pe)
        }
    }

    override fun hashCode(): Int = metadata.hashCode() * 31 + payload.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other is PplString) {
            val other = other as PplString?
            return metadata == other!!.metadata && payload == other.payload
        }
        return false
    }

    override fun toString(): String = pplToString(metadata, payload)

    companion object {

        private const val serialVersionUID = 1L

        val EMPTY = PplString("" + SUB_OPEN + SUB_CLOSE)
    }

}
