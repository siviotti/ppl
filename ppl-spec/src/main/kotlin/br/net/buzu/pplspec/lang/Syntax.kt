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
@file:JvmName("Syntax")

package br.net.buzu.pplspec.lang

import java.text.ParseException
import java.util.regex.Pattern

// **************************************************
// Constants
// **************************************************

const val EMPTY = ""
const val ENTER = '\n'
const val TAB = '\t'
const val SPACE = ' '
const val NO_NAME_START = "__" // Prefix used when the Metadata has no name.
const val UNBOUNDED = 0

// **************************************************
// Defaults
// **************************************************
const val DEFAULT_TYPE = TYPE_STRING
const val DEFAULT_FILL_CHAR = ' '
const val DEFAULT_SIZE = 0
const val DEFAULT_MIN_OCCURS = 0
const val DEFAULT_MAX_OCCURS = 1
const val DEFAULT_OCCURS = ("" + OCCURS_BEGIN + DEFAULT_MIN_OCCURS + OCCURS_RANGE + DEFAULT_MAX_OCCURS)

// **************************************************
// Regex
// **************************************************

const val NAME_REGEX = "[a-zA-Z_][a-zA-Z0-9-_]{0,49}:"
val NAME_PATTERN: Pattern = Pattern.compile(NAME_REGEX)
const val NAME_MAX_SIZE = 50

const val TYPE_REGEX = "[a-zA-Z]{1,50}(\\(.*\\))?"
val TYPE_PATTERN: Pattern = Pattern.compile(TYPE_REGEX)

const val SIZE_REGEX = "[0-9]{1,9}(,[0-9]{1,9})?"
val SIZE_PATTERN: Pattern = Pattern.compile(SIZE_REGEX)

const val OCCURS_REGEX = "[0-9]{1,9}(-[0-9]{1,9})?"
val OCCURS_PATTERN: Pattern = Pattern.compile(OCCURS_REGEX)

const val META_REGEX = ("(" + NAME_REGEX + ")?(" + TYPE_REGEX + ")?((" + SIZE_REGEX + ")" + ")?("
        + OCCURS_REGEX + ")?.*;")
val META_PATTERN: Pattern = Pattern.compile(META_REGEX)

// **************************************************
// Var
// **************************************************

/** Define the PPL encoding  */
const val VAR_ENCODING = "encoding"
/** Define the PPL version  */
const val VAR_VERSION = "version"
/** Define the expirition time domainOf a Metadata  */
const val VAR_EXPIRES = "expires"
/** Define the root name  */
const val VAR_ROOT = "root"

// **************************************************
// Util
// **************************************************

const val UNTERMINATED_STRING = "Unterminated String. Close delimiter not found:"
const val BLOCK_ERROR = "Block Error:"
const val BLOCK_ERROR_INVALID_OPEN_CHAR = "Block Error: Invalid openChar '"


// **************************************************
// Metadata
// **************************************************

/**
 * Indicates if the char 'c' is ignored as part of Metadata.
 *
 * @param c
 * the Character to check.
 * @return `true` if the Character 'c' is ignored as part domainOf a
 * Metadata, `false` otherwise.
 */
fun pplIsIgnored(c: Char): Boolean {
    return c == SPACE || c == ENTER || c == TAB
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
fun pplToString(metadata: String, payload: String): String {
    return if (metadata[0] == SUB_OPEN && metadata[metadata.length - 1] == SUB_CLOSE) {
        metadata + payload
    } else SUB_OPEN + metadata + SUB_CLOSE + payload
}

/**
 * Indicates if a 'name' is a valid Metadata name.
 *
 * @param name
 * The name to validate.
 * @return `true` if the name is valid, `false` otherwise.
 */
fun pplIsValidMetaName(name: String?): Boolean {
    if (name == null || name.isEmpty()) {
        return false
    }
    if (name.length > NAME_MAX_SIZE) {
        return false
    }
    if (!pplIsValidFirstCharMetaName(name[0])) {
        return false
    }
    for (i in 1 until name.length) {
        if (!pplIsValidCharMetaName(name[i])) {
            return false
        }
    }
    return true
}

/**
 * Indicates if the char 'c' can be the first char domainOf a Metadata name.
 *
 * @param c
 * the Character to check.
 * @return `true` if the Character c can be the first char domainOf a
 * Metadata. name, `false` otherwise.
 */
fun pplIsValidFirstCharMetaName(c: Char): Boolean {
    return Character.isLetter(c) || c == '_'
}

/**
 * Indicates if the char 'c' can be part domainOf a Metadata name.
 *
 * @param c
 * the Character to check.
 * @return `true` if the Character c can be part domainOf a Metadata. name,
 * `false` otherwise.
 */
fun pplIsValidCharMetaName(c: Char): Boolean {
    return Character.isLetterOrDigit(c) || c == '-' || c == '_'
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
fun pplExtractMetadata(text: String): String {
    if (text[0] != SUB_OPEN) {
        throw ParseException("PPL String must start with '$SUB_OPEN'.", 0)
    }
    val endIndex = pplBlockEndIndex(text, 0, SUB_OPEN, SUB_CLOSE)
    return text.substring(0, endIndex + 1)
}

fun pplFirstChar(text: String, beginIndex: Int): Int {
    var firstChar = beginIndex
    while (firstChar < text.length && pplIsIgnored(text[firstChar])) {
        firstChar++
        if (firstChar == text.length) {
            return firstChar - 1
        }
    }
    return firstChar
}

fun pplLastNumberIndex(text: String, beginIndex: Int, separator: Char): Int {
    var endIndex = beginIndex
    var separatorFound = false
    var c: Char
    for (i in beginIndex until text.length) {
        c = text[i]
        if (c in '0'..'9') { // 0-9
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
fun pplBlockEndIndex(text: String, beginIndex: Int, openChar: Char, closeChar: Char): Int {
    val block = Block(openChar, closeChar)
    var c = text[beginIndex]
    if (c != openChar) {
        throw ParseException("$BLOCK_ERROR_INVALID_OPEN_CHAR$c' Expected:'$openChar'", beginIndex)
    }
    var i = beginIndex
    while (i < text.length) {
        c = text[i]
        if (pplIsStringDelimiter(c)) {
            i = pplNextStringDelimiter(text, i, c) + 1
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

inline fun pplIsStringDelimiter(c: Char): Boolean {
    return c == PLIC || c == QUOTE
}

fun pplIsString(text: String?): Boolean {
    if (text != null && text.length >= 2) {
        val first = text[0]
        val last = text[text.length - 1]
        return pplIsStringDelimiter(first) && first == last
    }
    return false
}

@Throws(ParseException::class)
fun pplExtractString(text: String, beginIndex: Int, delimeter: Char): String {
    return text.substring(beginIndex, pplNextStringDelimiter(text, beginIndex, delimeter) + 1)
}

@Throws(ParseException::class)
fun pplNextStringDelimiter(text: String, beginIndex: Int, delimeter: Char): Int {
    for (i in beginIndex + 1 until text.length) {
        if (text[i] == delimeter) {
            return i
        }
    }
    throw ParseException("$UNTERMINATED_STRING$delimeter. Text:\n$text", beginIndex)
}

fun pplNextCharOrLast(text: String, beginIndex: Int, c: Char): Int {
    var i = beginIndex
    while (i < text.length) {
        if (text[i] == c) {
            return i
        }
        i++
    }
    return i
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