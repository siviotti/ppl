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
package br.net.buzu.pplspec.lang

import java.io.Serializable
import java.text.ParseException
import java.util.regex.Pattern

/**
 * Stateless Syntax definition.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 * @see Token
 */
class Syntax : Serializable {

    // **************************************************
    // Metadata
    // **************************************************

    /**
     * Indicates if the char 'c' is ignored as part of Metadata.
     *
     * @param c
     * the Character to check.
     * @return `true` if the Character 'c' is ignored as part of a
     * Metadata, `false` otherwise.
     */
    fun isIgnored(c: Char): Boolean {
        return c == SPACE || c == ENTER || c == TAB
    }

    /**
     * Indicates if a 'name' is a valid Metadata name.
     *
     * @param name
     * The name to validate.
     * @return `true` if the name is valid, `false` otherwise.
     */
    fun isValidMetaName(name: String?): Boolean {
        if (name == null || name.length < 1) {
            return false
        }
        if (name.length > NAME_MAX_SIZE) {
            return false
        }
        if (!isValidFirstCharMetaName(name[0])) {
            return false
        }
        for (i in 1 until name.length) {
            if (!isValidCharMetaName(name[i])) {
                return false
            }
        }
        return true
    }

    /**
     * Indicates if the char 'c' can be the first char of a Metadata name.
     *
     * @param c
     * the Character to check.
     * @return `true` if the Character c can be the first char of a
     * Metadata. name, `false` otherwise.
     */
    fun isValidFirstCharMetaName(c: Char): Boolean {
        return Character.isLetter(c) || c == '_'
    }

    /**
     * Indicates if the char 'c' can be part of a Metadata name.
     *
     * @param c
     * the Character to check.
     * @return `true` if the Character c can be part of a Metadata. name,
     * `false` otherwise.
     */
    fun isValidCharMetaName(c: Char): Boolean {
        return Character.isLetterOrDigit(c) || c == '-' || c == '_'
    }

    /**
     * Joins and formats a metadata and a payload.
     *
     * @param metadata
     * The metadata part.
     * @param payload
     * The payload part.
     * @return The String PPL on format "(METADATA)PAYLOAD".
     */
    fun asPplString(metadata: String, payload: String): String {
        return if (metadata[0] == Token.SUB_OPEN && metadata[metadata.length - 1] == Token.SUB_CLOSE) {
            metadata + payload
        } else Token.SUB_OPEN + metadata + Token.SUB_CLOSE + payload
    }

    /**
     * Extracts the Metadata part from a PPL text.
     *
     * @param text
     * The PPL text like '(MEDATATA)PAYLOAD'.
     * @return The metadata.
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun extractMetadata(text: String): String {
        if (text[0] != Token.SUB_OPEN) {
            throw ParseException("PPL String must start with '" + Token.SUB_OPEN + "'.", 0)
        }
        val endIndex = blockEndIndex(text, 0, Token.SUB_OPEN, Token.SUB_CLOSE)
        return text.substring(0, endIndex + 1)
    }

    fun firstChar(text: String, beginIndex: Int): Int {
        var firstChar = beginIndex
        while (firstChar < text.length && isIgnored(text[firstChar])) {
            firstChar++
            if (firstChar == text.length) {
                return firstChar - 1
            }
        }
        return firstChar
    }

    fun lastNumberIndex(text: String, beginIndex: Int, separator: Char): Int {
        var endIndex = beginIndex
        var separatorFound = false
        var c: Char
        for (i in beginIndex until text.length) {
            c = text[i]
            if (c >= '0' && c <= '9') { // 0-9
                endIndex++
            } else if (c == separator && !separatorFound) {
                endIndex++
                separatorFound = true
            } else {
                break
            }
        }
        return endIndex
    }


    // **************************************************
    // Blocks
    // **************************************************

    /**
     * @param text
     * @param beginIndex
     * @param openChar
     * @param closeChar
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun blockEndIndex(text: String, beginIndex: Int, openChar: Char, closeChar: Char): Int {
        val block = Block(openChar, closeChar)
        var c = text[beginIndex]
        if (c != openChar) {
            throw ParseException("$BLOCK_ERROR_INVALID_OPEN_CHAR$c' Expected:'$openChar'", beginIndex)
        }
        var i = beginIndex
        while (i < text.length) {
            c = text[i]
            if (isStringDelimiter(c)) {
                i = nextStringDelimeter(text, i, c) + 1
                continue
            }
            block.next(c)
            if (block.isClosed) {
                return i
            }
            i++
        }
        throw ParseException(BLOCK_ERROR + " Open '" + openChar + "'=" + block.open + "   Close '" + closeChar
                + "'=" + block.close + "\n" + text.substring(beginIndex), beginIndex)
    }

    // **************************************************
    // String
    // **************************************************

    fun isStringDelimiter(c: Char): Boolean {
        return c == Token.PLIC || c == Token.QUOTE
    }

    fun isString(text: String?): Boolean {
        if (text != null && text.length >= 2) {
            val first = text[0]
            val last = text[text.length - 1]
            return isStringDelimiter(first) && first == last
        }
        return false
    }

    @Throws(ParseException::class)
    fun extractString(text: String, beginIndex: Int, delimeter: Char): String {
        return text.substring(beginIndex, nextStringDelimeter(text, beginIndex, delimeter) + 1)
    }

    @Throws(ParseException::class)
    fun nextStringDelimeter(text: String, beginIndex: Int, delimeter: Char): Int {
        for (i in beginIndex + 1 until text.length) {
            if (text[i] == delimeter) {
                return i
            }
        }
        throw ParseException("$UNTERMINATED_STRING$delimeter. Text:\n$text", beginIndex)
    }

    fun nextCharOrLast(text: String, beginIndex: Int, c: Char): Int {
        var i = beginIndex
        while (i < text.length) {
            if (text[i] == c) {
                return i
            }
            i++
        }
        return i
    }

    companion object {

        private const val serialVersionUID = 1L

        val INSTANCE = Syntax()

        // **************************************************
        // Constants
        // **************************************************

        const val EMPTY = ""
        val ENTER = '\n'
        val TAB = '\t'
        val SPACE = ' '
        /** Prefix used when the Metadata has no name.  */
        val NO_NAME_START = "__"
        val UNBOUNDED = 0

        // **************************************************
        // Defaults
        // **************************************************

        val DEFAULT_TYPE = Token.TYPE_STRING
        val DEFAULT_SIZE = 0
        val DEFAULT_MIN_OCCURS = 0
        val DEFAULT_MAX_OCCURS = 1
        val DEFAULT_OCCURS = ("" + Token.OCCURS_BEGIN + Syntax.DEFAULT_MIN_OCCURS + Token.OCCURS_RANGE
                + Syntax.DEFAULT_MAX_OCCURS)
        val SINGLE_OCCURS = 1
        val SINGLE_REQUIRED_OCCURS_VALUE = "" + SINGLE_OCCURS + Token.OCCURS_RANGE + SINGLE_OCCURS
        val SINGLE_REQUIRED_OCCURS = Token.OCCURS_BEGIN + SINGLE_REQUIRED_OCCURS_VALUE

        // **************************************************
        // Regex
        // **************************************************

        val PPL_SPEC = "(METADATA)PAYLOAD"
        val META = "[NAME]:[TYPE(SUB)][SIZE|{FORMAT}]#[OCCURS][EXTENSION]"

        val NAME_REGEX = "[a-zA-Z_][a-zA-Z0-9-_]{0,49}:"
        val NAME_PATTERN = Pattern.compile(NAME_REGEX)
        val NAME_MAX_SIZE = 50

        val TYPE_REGEX = "[a-zA-Z]{1,50}(\\(.*\\))?"
        val TYPE_PATTERN = Pattern.compile(TYPE_REGEX)

        val SIZE_REGEX = "[0-9]{1,9}(,[0-9]{1,9})?"
        val SIZE_PATTERN = Pattern.compile(SIZE_REGEX)

        val OCCURS_REGEX = "[0-9]{1,9}(-[0-9]{1,9})?"
        val OCCURS_PATTERN = Pattern.compile(OCCURS_REGEX)

        val META_REGEX = ("(" + NAME_REGEX + ")?(" + TYPE_REGEX + ")?((" + SIZE_REGEX + ")" + ")?("
                + OCCURS_REGEX + ")?.*;")
        val META_PATTERN = Pattern.compile(META_REGEX)

        // **************************************************
        // Var
        // **************************************************

        /** Define the PPL encoding  */
        val VAR_ENCODING = "encoding"
        /** Define the PPL version  */
        val VAR_VERSION = "version"
        /** Define the expirition time of a Metadata  */
        val VAR_EXPIRES = "expires"
        /** Define the root name  */
        val VAR_ROOT = "root"

        // **************************************************
        // Util
        // **************************************************

        val UNTERMINATED_STRING = "Unterminated String. Close delimiter not found:"
        val BLOCK_ERROR = "Block Error:"
        val BLOCK_ERROR_INVALID_OPEN_CHAR = "Block Error: Invalid openChar '"
    }

}

internal class Block(private val openChar: Char, private val closeChar: Char) {
    var open: Int = 0
    var close: Int = 0

    val isClosed: Boolean
        get() = open == close

    fun next(c: Char) {
        if (c == openChar) {
            open++
        } else if (c == closeChar) {
            close++
        }
    }
}
