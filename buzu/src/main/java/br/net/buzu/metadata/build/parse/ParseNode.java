/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.metadata.build.parse;

import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.lang.Token;

import java.util.List;

/**
 * Represents a Metadata Statement.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public final class ParseNode {

	String name = Syntax.EMPTY;
	String type = Syntax.EMPTY;
	String size = Syntax.EMPTY;
	String occurs = Syntax.EMPTY;
	String domain = Syntax.EMPTY;
	String defaultValue = Syntax.EMPTY;
	String tags = Syntax.EMPTY;

	List<ParseNode> children = null;

	ParseNode() {

	}

	public ParseNode(String name, String type, String size, String occurs, List<ParseNode> children) {
		super();
		this.name = name;
		this.type = type;
		this.size = size;
		this.occurs = occurs;
		this.children = children;
	}

	public ParseNode(String name, String type, String size, String occurs, String domain, String defaultValue,
			String tags, List<ParseNode> children) {
		super();
		this.name = name;
		this.type = type;
		this.size = size;
		this.occurs = occurs;
		this.domain = domain;
		this.defaultValue = defaultValue;
		this.tags = tags;
		this.children = children;
	}

	public boolean isComplex() {
		return children != null;
	}

	public boolean isVar() {
		return name.length() > 0 && name.charAt(0) == Token.VAR;
	}

	public boolean hasName() {
		return name.length() > 0;
	}

	public boolean hasType() {
		return type.length() > 0;
	}

	public boolean hasSize() {
		return size.length() > 0;
	}

	public boolean hasOccurs() {
		return occurs.length() > 0;
	}

	public boolean hasExtension() {
		return domain.length() > 0 || defaultValue.length() > 0 || tags.length() > 0;
	}

	@Override
	public String toString() {
		return (name != null && !name.isEmpty() ? name + Token.NAME_END : Syntax.EMPTY)
				+ ((!isComplex()) ? type : getChildrensToString()) + size
				+ ((Syntax.EMPTY.equals(occurs)) ? Syntax.EMPTY : Token.OCCURS_BEGIN) + occurs + domain + "='"
				+ defaultValue + "'" + tags;
	}

	private String getChildrensToString() {
		StringBuilder sb = new StringBuilder();
		if (children != null) {
			sb.append("(");
			for (ParseNode child : children) {
				sb.append(child.toString()).append(Token.METADATA_END);
			}
			sb.append(")");
		}
		return sb.toString();
	}

	public List<ParseNode> getMetas() {
		return children;
	}

	public String toTree(int level) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int i = 0; i < level; i++) {
			sb.append(".");
		}
		sb.append("name:").append(name).append(" type:").append(type).append(" size:").append(size).append(" occurs:")
				.append(occurs).append(" domain:").append(domain).append(" defaultValue=").append(defaultValue)
				.append(" tags=").append(tags);
		if (children != null) {
			for (ParseNode node : children) {
				sb.append(node.toTree(level + 1));
			}
		}
		return sb.toString();
	}

	// **************************************************
	// Get / Set
	// **************************************************

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	public String getSize() {
		return size;
	}

	public String getOccurs() {
		return occurs;
	}

	public String getDomain() {
		return domain;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getTags() {
		return tags;
	}


}
