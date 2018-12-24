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
package br.net.buzu.metadata.build.parse;

import static br.net.buzu.lang.Syntax.*;

import br.net.buzu.exception.PplParseException;
import br.net.buzu.lang.Token;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * [STATELESS] Class to perform the split domainOf a String into <code>Meta</code>
 * objects.
 * 
 * @author Douglas Siviotti
 * @since 1.0
 */
public class Splitter {

	static final String UNTERMINATED_STRING_LITERAL = "Unterminated String Literal using ";
	static final String WRONG_USE_OF_BLOCK = "Wrong use domainOf block ";
	static final String WRONG_USE_OF_KEYWORD = "Wrong use domainOf keyword '";


	// **************************************************
	// Split
	// **************************************************

	/**
	 * Perform the split domainOf a String
	 * [NAME]:[TYPE][SIZE.SCALE]#[OCCURS][DOMAIN][DEFAULT][TAGS] into a
	 * <code>ParseNode<code> instance.
	 * &#64;param text
	 *            The String to split.
	 * @return The <code>ParseNode</code> created.
	 */
	public ParseNode splitMeta(final String text) {
		try {
			ParseNode node = new ParseNode();
			int lastChar = text.length() - 1;
			int index = extractName(text, 0, node);

			index = extractType(text, index, node);

			if (index > lastChar) {
				return node;
			}

			index = extractSize(text, index, node);

			if (index > lastChar) {
				return node;
			}

			index = extractOccurs(text, index, node);

			if (index > lastChar) {
				return node;
			}

			index = extractDomain(text, index, node);

			if (index > lastChar) {
				return node;
			}

			index = extractDefaultvalue(text, index, node);

			if (index > lastChar) {
				return node;
			}

			node.tags = extractTags(text, index);

			return node;
		} catch (ParseException pe) {
			throw new PplParseException("PPL Split error. Text:\n" + text, pe);
		}
	}

	/**
	 * Efetua Split no padrão [META];[META];...[META] E MONTA UMA LISTA DE mETAS.
	 * 
	 * @param text
	 *            O texto contendo os Metas.
	 * @return Um List com as instâncias de Meta.
	 * @throws ParseException
	 */
	public List<ParseNode> splitLayout(final String text) throws ParseException {
		ArrayList<ParseNode> list = new ArrayList<>();
		char c = ' ';
		int sub = 0;
		int brace = 0;
		int array = 0;
		int beginMeta = 0;
		int i = 0;
		while (i < text.length()) {
			c = text.charAt(i);
			sub = countBlock(sub, c, Token.SUB_OPEN, Token.SUB_CLOSE);
			brace = countBlock(brace, c, Token.BRACE_OPEN, Token.BRACE_CLOSE);
			array = countBlock(array, c, Token.ARRAY_OPEN, Token.ARRAY_CLOSE);
			if (pplIsStringDelimiter(c)) {
				i = pplNextStringDelimiter(text, i, c) + 1;
				continue;
			}
			if (c == Token.METADATA_END && (sub + brace + array) == 0) {
				list.add(splitMeta(text.substring(beginMeta, i)));
				beginMeta = i + 1;
			}
			i++;
		}
		checkBlock("( )", sub, text);
		checkBlock("{ }", brace, text);
		checkBlock("[ ]", array, text);
		if (c != Token.METADATA_END) {
			list.add(splitMeta(text.substring(beginMeta, i)));
		}
		return list;
	}

	private int countBlock(final int value, char c, char open, char close) {
		if (c == open) {
			return value + 1;
		}
		if (c == close) {
			return value - 1;
		}
		return value;
	}

	protected void checkBlock(String element, int count, String text) {
		if (count != 0) {
			throw new PplParseException(WRONG_USE_OF_BLOCK + element + ". Too many "
					+ ((count > 0) ? "'Open'" : "'Close'") + " Text:\n" + text);
		}
	}

	protected void checkStr(char c, boolean noOpened, String text) {
		if (!noOpened) {
			throw new PplParseException(UNTERMINATED_STRING_LITERAL + c + " Text:\n" + text);
		}
	}

	// ********** NAME **********

	protected int extractName(final String text, final int beginIndex, ParseNode node) throws ParseException {
		if (text.isEmpty()) {
			return 0;
		}	
		int firstChar = pplFirstChar(text, 0);
		char c = text.charAt(firstChar);
		int count = 0;
		if (pplIsValidFirstCharMetaName(c) || c == Token.VAR) {
			count++;
		} else {
			return (c == Token.NAME_END) ? firstChar + 1 : firstChar;
		}
		for (int i = firstChar + 1; i < text.length(); i++) {
			c = text.charAt(i);
			if (pplIsValidCharMetaName(c)) {
				count++;
			} else if (c == Token.NAME_END) {
				node.name = text.substring(firstChar, firstChar + count);
				return i + 1; // ignore :
			} else {
				break;
			}
		}
		return firstChar;
	}
	
	// ********** TYPE **********

	protected int extractType(final String text, final int beginIndex, final ParseNode node) throws ParseException {
		int firstChar = pplFirstChar(text, beginIndex);
		int endIndex = firstChar;
		int offset = 0;
		int endBlock = 0;
		char c;
		for (int i = firstChar; i < text.length(); i++) {
			c = text.charAt(i);
			if (Character.isLetter(c)) { // A-Za-z c >= 'A' && c <= 'Z'
				endIndex++;
			} else if (c == Token.SUB_OPEN) {
				endBlock = pplBlockEndIndex(text, i, Token.SUB_OPEN, Token.SUB_CLOSE);
				node.children = splitLayout(text.substring(i + 1, endBlock));
				offset = endBlock + 1;// primeiro após )
				break;
			} else {
				break;
			}
		}
		node.type = text.substring(firstChar, endIndex);

		if (!node.isComplex()) {
			return endIndex;
		}
		return offset;

	}
	

	// ********** SIZE AND SCALE **********

	protected int extractSize(final String text, final int beginIndex, ParseNode node) {
		int firstChar = pplFirstChar(text, beginIndex);
		int endIndex = pplLastNumberIndex(text, firstChar, Token.DECIMAL_SEP);
		if (endIndex > beginIndex) {
			node.size = text.substring(firstChar, endIndex);
		}
		return endIndex;
	}

	// ********** OCCURS **********

	protected int extractOccurs(final String text, final int beginIndex, ParseNode node) {
		int firstChar = pplFirstChar(text, beginIndex);
		if (text.charAt(firstChar) != Token.OCCURS_BEGIN) {
			return firstChar;
		}
		firstChar++; // skip #
		if (firstChar == text.length()) {
			return firstChar; // returns the # treeIndex
		}
		int endIndex = pplLastNumberIndex(text, firstChar, Token.OCCURS_RANGE);
		if (endIndex > beginIndex) {
			node.occurs = text.substring(firstChar, endIndex);
		}
		return endIndex;
	}

	// ********** DOMAIN **********

	protected int extractDomain(final String text, final int beginIndex, ParseNode node) throws ParseException {
		int firstChar = pplFirstChar(text, beginIndex);
		if (text.charAt(firstChar) != Token.DOMAIN_BEGIN) {
			return firstChar;
		}
		int endIndex = pplBlockEndIndex(text, firstChar, Token.DOMAIN_BEGIN, Token.DOMAIN_END);
		node.domain = text.substring(firstChar, endIndex + 1);
		return endIndex + 1;
	}

	// ********** DEFAULT **********

	protected int extractDefaultvalue(final String text, final int beginIndex, ParseNode node) throws ParseException {
		int firstChar = pplFirstChar(text, beginIndex);
		char c = text.charAt(firstChar); 
		if (c != Token.DEFAULT_VALUE) {
			return firstChar;
		}
		int index = firstChar + 1;
		if (index > text.length() - 1) {
			return firstChar;
		}
		c = text.charAt(index);
		int endIndex;
		if (pplIsStringDelimiter(c)) {
			endIndex = pplNextStringDelimiter(text, index, c)+1;
		} else {
			endIndex = pplNextCharOrLast(text, index, SPACE);
		} 
		node.defaultValue = text.substring(firstChar + 2, endIndex-1);
		return endIndex+1;
	}

	// ********** TAGS **********

	protected String extractTags(final String text, final int beginIndex) {
		int firstChar = pplFirstChar(text, beginIndex);
		int endIndex = firstChar;
		for (int i = firstChar; i < text.length(); i++) {
			if (!pplIsIgnored(text.charAt(i))) {
				endIndex = i;
			}
		}
		return text.substring(firstChar, endIndex + 1);
	}
}
