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
package br.net.buzu.pplspec.model

import java.io.Serializable
import java.text.ParseException

import br.net.buzu.pplspec.exception.PplParseException
import br.net.buzu.pplspec.lang.Syntax
import br.net.buzu.pplspec.lang.Token

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
class PplString
/**
 * Context constructor.
 *
 * @param text
 * The PPL String as text.
 * @param syntax
 * The alternative Syntax.
 */
@JvmOverloads constructor(text: String, val syntax: Syntax = Syntax()) : Serializable {

    // **************************************************
    // get / set
    // **************************************************

    val metadata: String
    val payload: String

    val pplMetadata: String
        get() = metadata.substring(1, metadata.length - 1)

    init {
        if (text == null || text.isEmpty()) {
            throw IllegalArgumentException("text cannot be 'null' or Empty!")
        }
        if (syntax == null) {
            throw IllegalArgumentException("syntax cannot be null!")
        }
        try {
            metadata = syntax.extractMetadata(text)
            payload = text.substring(metadata.length)
        } catch (pe: ParseException) {
            throw PplParseException("PPL Parse error. Text:\n$text", pe)
        }

    }

    // **************************************************
    // hashcode, equals, toString
    // **************************************************

    override fun hashCode(): Int {
        return metadata.hashCode() * 31 + payload.hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (obj is PplString) {
            val other = obj as PplString?
            return metadata == other!!.metadata && payload == other.payload
        }
        return false
    }

    override fun toString(): String {
        return syntax.asPplString(metadata, payload)
    }

    companion object {

        private const val serialVersionUID = 1L

        val EMPTY = PplString("" + Token.SUB_OPEN + Token.SUB_CLOSE)
        // **************************************************
        // factory methods
        // **************************************************

        fun of(text: String): PplString {
            return PplString(text)
        }
    }

}
/**
 * Simple constructor.
 *
 * @param text
 * The PPL String as text.
 */
