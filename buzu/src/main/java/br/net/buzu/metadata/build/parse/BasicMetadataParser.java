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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.net.buzu.context.BasicContext;
import br.net.buzu.pplspec.api.MetadataParser;
import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.Domain;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.PplString;
import br.net.buzu.pplspec.model.SizeType;
import br.net.buzu.pplspec.model.Subtype;

/**
 * Metadata parser to transform <code>Text</code> to <code>Metadata</code>.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class BasicMetadataParser implements MetadataParser {

	static final String UNTERMINATED_STRING = " (Unterminated String)";
	static final String SUBTYPE_NOT_FOUND = "Subtype not found:";
	static final String INVALID_DOMAIN = "Invalid domain:";
	static final String INVALID_DEFAULTVALUE = "Invalid default value:";

	private final Splitter splitter;
	private final PplContext context;

	private int count;

	/**
	 * Default constructor.
	 */
	public BasicMetadataParser() {
		this(new BasicContext(), new Splitter());
	}

	/**
	 * Default constructor.
	 */
	public BasicMetadataParser(PplContext context) {
		this(context, new Splitter());
	}

	/**
	 * Complete comstructor.
	 * 
	 * @param splitter
	 * @param context
	 */
	public BasicMetadataParser(PplContext context, Splitter splitter) {
		super();
		if (splitter == null) {
			throw new IllegalArgumentException("Splitter cannot be null!");
		}
		this.splitter = splitter;
		if (context == null) {
			throw new IllegalArgumentException("Context cannot be null!");
		}
		this.context = context;
	}

	// **************************************************
	// API
	// **************************************************

	@Override
	public Metadata parse(String text) {
		return parse(new PplString(text, splitter.getSyntax()));
	}

	@Override
	public Metadata parse(PplString pplString) {
		try {
			List<ParseNode> nodes = splitter.splitLayout(pplString.getPplMetadata());
			if (nodes.size() > 1) {
				ParseNode root = new ParseNode();
				root.children = nodes;
				return parse(Syntax.EMPTY, root, 0);
			} else {
				return parse(Syntax.EMPTY, nodes.get(0), 0);
			}
		} catch (ParseException e) {
			throw new PplParseException("Parsing error on text:\n" + pplString, e);
		}
	}

	protected Metadata parse(String parentId, ParseNode node, int index) {
		String name = parseName(node);
		Subtype subtype = parseSubtype(node);
		int size = parseSize(node, subtype);
		int scale = parseScale(node, subtype);
		int minOccurs = parseMinOccurs(node);
		int maxOccurs = parseMaxOccurs(node);
		Domain domain = parseDomain(node);
		MetaInfo metaInfo = new MetaInfo(parentId, index, name, subtype, size, scale, minOccurs, maxOccurs, domain,
				node.defaultValue, node.tags);
		return context.metadataFactory().create(metaInfo, parseChildren(metaInfo.id(), node));

	}

	protected List<Metadata> parseChildren(String parentId, ParseNode node) {
		if (!node.isComplex()) {
			return null;
		}
		List<Metadata> metas = new ArrayList<>();
		for (int i = 0; i < node.children.size(); i++) {
			metas.add(parse(parentId, node.children.get(i), i));
		}
		return metas;
	}

	protected String parseName(ParseNode node) {
		String name = (node.hasName()) ? node.getName() : Syntax.NO_NAME_START + count++;
		if (!splitter.getSyntax().isValidMetaName(name)) {
			throw new MetadataParseException("Invalid Metadata name:" + name, node);
		}
		return name;
	}

	protected Subtype parseSubtype(ParseNode node) {
		Subtype subtype = context.subtypeManager().fromText(node.getType(), node.isComplex());
		if (subtype == null) {
			throw new MetadataParseException(SUBTYPE_NOT_FOUND + node.getType(), node);
		}
		return subtype;
	}

	protected int parseSize(ParseNode node, Subtype subtype) {
		if (node.hasSize()) {
			if (subtype.dataType().sizeType().equals(SizeType.FIXED)) {
				throw new MetadataParseException(subtype + " do not support custom size." + subtype, node);
			}
			return extractSize(node);
		}
		return subtype.fixedSize();
	}

	private int parseScale(ParseNode node, Subtype subtype) {
		if (node.hasSize()) {
			if (subtype.dataType().sizeType().equals(SizeType.FIXED)) {
				throw new MetadataParseException(subtype + " do not support custom scale." + subtype, node);
			}
			return extractScale(node);
		}
		return 0;
	}

	protected int extractSize(ParseNode node) {
		String size = node.getSize();
		int index = size.indexOf(Token.DECIMAL_SEP);
		if (index > 0) {
			size = size.substring(0, index);
		}
		return Integer.parseInt(size);
	}

	protected int extractScale(ParseNode node) {
		String scale = node.getSize();
		int index = scale.indexOf(Token.DECIMAL_SEP);
		if (index > 0) {
			scale = scale.substring(index + 1, scale.length());
			return scale.length() > 0 ? Integer.parseInt(scale) : 0;
		}
		return 0;
	}

	protected int parseMinOccurs(ParseNode node) {
		return (node.hasOccurs()) ? extractMinOccurs(node.getOccurs()) : 0;
	}

	protected int extractMinOccurs(String occurs) {
		int index = occurs.indexOf(Token.OCCURS_RANGE);
		return (index < 0) ? Syntax.DEFAULT_MIN_OCCURS : Integer.parseInt(occurs.substring(0, index));
	}

	protected int parseMaxOccurs(ParseNode node) {
		return (node.hasOccurs()) ? extractMaxOccurs(node.getOccurs()) : 1;
	}

	protected int extractMaxOccurs(String occurs) {
		int index = occurs.indexOf(Token.OCCURS_RANGE);
		return (index < -1) ? Integer.parseInt(occurs) : Integer.parseInt(occurs.substring(index + 1, occurs.length()));
	}

	protected Domain parseDomain(ParseNode node) {
		String domainStr = node.getDomain();
		if (domainStr == null || domainStr.length() < 3) {
			return Domain.EMPTY;
		}
		if (domainStr.charAt(0) != Token.DOMAIN_BEGIN || domainStr.charAt(domainStr.length() - 1) != Token.DOMAIN_END) {
			throw new MetadataParseException(INVALID_DOMAIN + domainStr, node);
		}
		domainStr = domainStr.substring(1, domainStr.length() - 1);
		if (domainStr.trim().isEmpty()) {
			return Domain.EMPTY;
		}
		List<String> list = new ArrayList<>();
		char c;
		int beginIndex = 0;
		int endIndex = domainStr.length();
		for (int i = 0; i < domainStr.length(); i++) {
			c = domainStr.charAt(i);
			if (c == Token.PLIC || c == Token.QUOTE) {
				try {
					i = context.syntax().nextStringDelimeter(domainStr, i, c);
				} catch (ParseException e) {
					throw new MetadataParseException(INVALID_DOMAIN + domainStr, node, e);
				}
				continue;
			}
			if (c == Token.DOMAIN_SEPARATOR) {
				endIndex = i;
				list.add(extractItem(domainStr, beginIndex, endIndex));
				beginIndex = endIndex + 1;
			}

		}
		list.add(extractItem(domainStr, beginIndex, domainStr.length()));

		return Domain.create(node.name + Token.PATH_SEP + "domain", Domain.createItems(list));
	}

	private String extractItem(final String domain, int beginIndex, int endIndex) {
		String s = domain.substring(beginIndex, endIndex);
		char firstChar = s.charAt(0);
		if (firstChar == Token.QUOTE || firstChar == Token.PLIC) {
			char lastChar = s.charAt(s.length() - 1);
			if (firstChar == lastChar) {
				return s.substring(1, s.length() - 1);
			}
		}
		return s;
	}

	// **************************************************
	// get / set
	// **************************************************

	public Splitter getSplitter() {
		return splitter;
	}

	public PplContext getContext() {
		return context;
	}

}
