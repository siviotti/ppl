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
package br.net.buzu.pplspec.lang;

import java.io.Serializable;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * Stateless Syntax definition.
 *
 * @author Douglas Siviotti
 * @since 1.0
 * 
 * @see Token
 */
public class Syntax implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Syntax INSTANCE = new Syntax();

	// **************************************************
	// Constants
	// **************************************************

	public static final String EMPTY = "";
	public static final char ENTER = '\n';
	public static final char TAB = '\t';
	public static final char SPACE = ' ';
	/** Prefix used when the Metadata has no name. */
	public static final String NO_NAME_START = "__";
	public static final int UNBOUNDED = 0;

	// **************************************************
	// Defaults
	// **************************************************

	public static final char DEFAULT_TYPE = Token.TYPE_STRING;
	public static final int DEFAULT_SIZE = 0;
	public static final int DEFAULT_MIN_OCCURS = 0;
	public static final int DEFAULT_MAX_OCCURS = 1;
	public static final String DEFAULT_OCCURS = "" + Token.OCCURS_BEGIN + Syntax.DEFAULT_MIN_OCCURS + Token.OCCURS_RANGE
			+ Syntax.DEFAULT_MAX_OCCURS;
	public static final int SINGLE_OCCURS = 1;
	public static final String SINGLE_REQUIRED_OCCURS_VALUE = "" + SINGLE_OCCURS + Token.OCCURS_RANGE + SINGLE_OCCURS;
	public static final String SINGLE_REQUIRED_OCCURS = Token.OCCURS_BEGIN + SINGLE_REQUIRED_OCCURS_VALUE;

	// **************************************************
	// Regex
	// **************************************************

	public static final String PPL_SPEC = "(METADATA)PAYLOAD";
	public static final String META = "[NAME]:[TYPE(SUB)][SIZE|{FORMAT}]#[OCCURS][EXTENSION]";

	public static final String NAME_REGEX = "[a-zA-Z_][a-zA-Z0-9-_]{0,49}:";
	public static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
	public static final int NAME_MAX_SIZE = 50;

	public static final String TYPE_REGEX = "[a-zA-Z]{1,50}(\\(.*\\))?";
	public static final Pattern TYPE_PATTERN = Pattern.compile(TYPE_REGEX);

	public static final String SIZE_REGEX = "[0-9]{1,9}(,[0-9]{1,9})?";
	public static final Pattern SIZE_PATTERN = Pattern.compile(SIZE_REGEX);

	public static final String OCCURS_REGEX = "[0-9]{1,9}(-[0-9]{1,9})?";
	public static final Pattern OCCURS_PATTERN = Pattern.compile(OCCURS_REGEX);

	public static final String META_REGEX = "(" + NAME_REGEX + ")?(" + TYPE_REGEX + ")?((" + SIZE_REGEX + ")" + ")?("
			+ OCCURS_REGEX + ")?.*;";
	public static final Pattern META_PATTERN = Pattern.compile(META_REGEX);

	// **************************************************
	// Var
	// **************************************************

	/** Define the PPL encoding */
	public static final String VAR_ENCODING = "encoding";
	/** Define the PPL version */
	public static final String VAR_VERSION = "version";
	/** Define the expirition time of a Metadata */
	public static final String VAR_EXPIRES = "expires";
	/** Define the root name */
	public static final String VAR_ROOT = "root";

	// **************************************************
	// Util
	// **************************************************

	public static final String UNTERMINATED_STRING = "Unterminated String. Close delimiter not found:";
	public static final String BLOCK_ERROR = "Block Error:";
	public static final String BLOCK_ERROR_INVALID_OPEN_CHAR = "Block Error: Invalid openChar '";

	// **************************************************
	// Metadata
	// **************************************************

	/**
	 * Indicates if the char 'c' is ignored as part of Metadata.
	 * 
	 * @param c
	 *            the Character to check.
	 * @return <code>true</code> if the Character 'c' is ignored as part of a
	 *         Metadata, <code>false</code> otherwise.
	 */
	public boolean isIgnored(char c) {
		return c == SPACE || c == ENTER || c == TAB;
	}

	/**
	 * Indicates if a 'name' is a valid Metadata name.
	 * 
	 * @param name
	 *            The name to validate.
	 * @return <code>true</code> if the name is valid, <code>false</code> otherwise.
	 */
	public boolean isValidMetaName(String name) {
		if (name == null || name.length() < 1) {
			return false;
		}
		if (name.length() > NAME_MAX_SIZE) {
			return false;
		}
		if (!isValidFirstCharMetaName(name.charAt(0))) {
			return false;
		}
		for (int i = 1; i < name.length(); i++) {
			if (!isValidCharMetaName(name.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Indicates if the char 'c' can be the first char of a Metadata name.
	 * 
	 * @param c
	 *            the Character to check.
	 * @return <code>true</code> if the Character c can be the first char of a
	 *         Metadata. name, <code>false</code> otherwise.
	 */
	public boolean isValidFirstCharMetaName(char c) {
		return Character.isLetter(c) || c == '_';
	}

	/**
	 * Indicates if the char 'c' can be part of a Metadata name.
	 * 
	 * @param c
	 *            the Character to check.
	 * @return <code>true</code> if the Character c can be part of a Metadata. name,
	 *         <code>false</code> otherwise.
	 */
	public boolean isValidCharMetaName(char c) {
		return Character.isLetterOrDigit(c) || c == '-' || c == '_';
	}

	/**
	 * Joins and formats a metadata and a payload.
	 * 
	 * @param metadata
	 *            The metadata part.
	 * @param payload
	 *            The payload part.
	 * @return The String PPL on format "(METADATA)PAYLOAD".
	 */
	public String asPplString(String metadata, String payload) {
		if (metadata.charAt(0) == Token.SUB_OPEN && metadata.charAt(metadata.length() - 1) == Token.SUB_CLOSE) {
			return metadata + payload;
		}
		return Token.SUB_OPEN + metadata + Token.SUB_CLOSE + payload;
	}

	/**
	 * Extracts the Metadata part from a PPL text.
	 * 
	 * @param text
	 *            The PPL text like '(MEDATATA)PAYLOAD'.
	 * @return The metadata.
	 * @throws ParseException
	 */
	public String extractMetadata(String text) throws ParseException {
		if (text.charAt(0) != Token.SUB_OPEN) {
			throw new ParseException("PPL String must start with '" + Token.SUB_OPEN + "'.", 0);
		}
		int endIndex = blockEndIndex(text, 0, Token.SUB_OPEN, Token.SUB_CLOSE);
		return text.substring(0, endIndex + 1);
	}

	public int firstChar(final String text, int beginIndex) {
		int firstChar = beginIndex;
		while (firstChar < text.length() && isIgnored(text.charAt(firstChar))) {
			firstChar++;
			if (firstChar == text.length()) {
				return firstChar - 1;
			}
		}
		return firstChar;
	}
	
	public int lastNumberIndex(final String text, final int beginIndex, final char separator) {
		int endIndex = beginIndex;
		boolean separatorFound = false;
		char c;
		for (int i = beginIndex; i < text.length(); i++) {
			c = text.charAt(i);
			if (c >= '0' && c <= '9') { // 0-9
				endIndex++;
			} else if (c == separator && !separatorFound) {
				endIndex++;
				separatorFound = true;
			} else {
				break;
			}
		}
		return endIndex;
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
	public int blockEndIndex(final String text, final int beginIndex, final char openChar, final char closeChar)
			throws ParseException {
		Block block = new Block(openChar, closeChar);
		char c = text.charAt(beginIndex);
		if (c != openChar) {
			throw new ParseException(BLOCK_ERROR_INVALID_OPEN_CHAR + c + "' Expected:'" + openChar + "'", beginIndex);
		}
		int i = beginIndex;
		while (i < text.length()) {
			c = text.charAt(i);
			if (isStringDelimiter(c)) {
				i = nextStringDelimeter(text, i, c) + 1;
				continue;
			}
			block.next(c);
			if (block.isClosed()) {
				return i;
			}
			i++;
		}
		throw new ParseException(BLOCK_ERROR + " Open '" + openChar + "'=" + block.open + "   Close '" + closeChar
				+ "'=" + block.close + "\n" + text.substring(beginIndex), beginIndex);
	}

	// **************************************************
	// String
	// **************************************************

	public boolean isStringDelimiter(char c) {
		return c == Token.PLIC || c == Token.QUOTE;
	}

	public boolean isString(final String text) {
		if (text != null && text.length() >= 2) {
			char first = text.charAt(0);
			char last = text.charAt(text.length() - 1);
			return isStringDelimiter(first) && first == last;
		}
		return false;
	}

	public String extractString(final String text, final int beginIndex, final char delimeter) throws ParseException {
		return text.substring(beginIndex, nextStringDelimeter(text, beginIndex, delimeter) + 1);
	}

	public int nextStringDelimeter(final String text, final int beginIndex, final char delimeter)
			throws ParseException {
		for (int i = beginIndex + 1; i < text.length(); i++) {
			if (text.charAt(i) == delimeter) {
				return i;
			}
		}
		throw new ParseException(UNTERMINATED_STRING + delimeter + ". Text:\n" + text, beginIndex);
	}

	public int nextCharOrLast(final String text, final int beginIndex, final char c) {
		int i = beginIndex;
		while (i < text.length()) {
			if (text.charAt(i) == c) {
				return i;
			}
			i++;
		}
		return i;
	}

}

class Block {
	private final char openChar;
	private final char closeChar;
	int open;
	int close;

	public Block(char openChar, char closeChar) {
		super();
		this.openChar = openChar;
		this.closeChar = closeChar;
	}

	public void next(char c) {
		if (c == openChar) {
			open++;
		} else if (c == closeChar) {
			close++;
		}
	}

	public boolean isClosed() {
		return open == close;
	}
}
