/*
 *	This file is part domainOf Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms domainOf the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 domainOf the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy domainOf the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.metadata.code;

import br.net.buzu.pplspec.api.MetadataCoder;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.*;

/**
 * Verbose Serializer.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class VerboseMetadataCoder implements MetadataCoder {

	public static final MetadataCoder INSTANCE = new VerboseMetadataCoder();

	static final String ENTER = "" + Syntax.ENTER;
	static final String TAB = "" + Syntax.TAB;
	static final String SPACE = "" + Syntax.SPACE;
	static final String EMPTY = Syntax.EMPTY;
	static final Subtype DEFAULT_SINGLE = Subtype.Companion.getDEFAULT_SINGLE();

	@Override
	public String code(Metadata meta) {
		StringBuilder sb = new StringBuilder();
		sb.append(serialize(meta, 0));
		return sb.toString();
	}

	public Dialect dialect() {
		return Dialect.VERBOSE;
	}

	protected void addVAr(String varName, String value, StringBuilder sb) {
		sb.append(Token.VAR).append(varName).append(Token.NAME_END).append(Token.DEFAULT_VALUE).append(Token.VALUE_BEGIN)
				.append(value).append(Token.VALUE_END).append(Token.METADATA_END);
	}

	protected String serialize(Metadata meta, int level) {
		MetaInfo metaInfo = meta.info();
		StringBuilder sb = new StringBuilder();
		// Name
		sb.append(serializeName(metaInfo.getName()));
		// Subtype
		sb.append(serializeType(meta, level, afterMetadataEnd(), indentationElement()));
		// Size and Precision
		sb.append(serializeSize(meta));
		// Occurs
		sb.append(serializeOccurs(metaInfo));
		// Extension
		sb.append(serializeExtension(metaInfo));
		return sb.toString();
	}

	protected String serializeName(String name) {
		return hasName(name) ? name + Token.NAME_END + afterName() : EMPTY;
	}

	protected boolean hasName(String name) {
		return name != null && !EMPTY.equals(name) && !name.startsWith(Syntax.NO_NAME_START);
	}

	protected String afterName() {
		return SPACE;
	}

	protected String serializeType(Metadata meta, final int level, String afterMeta, String indentation) {
		StringBuilder sb = new StringBuilder();
		if (meta.kind().isComplex()) {
			sb.append(Token.SUB_OPEN).append(afterSubOpen());
			for (Metadata child : meta.children()) {
				for (int i = 0; i < level; i++) {
					sb.append(indentation);
				}
				sb.append(serialize(child, level + 1)).append(Token.METADATA_END).append(afterMeta);
			}
			sb.deleteCharAt(sb.length() - 1); // remove last
			for (int i = 0; i < level; i++) {
				sb.append(indentation);
			}
			sb.append(Token.SUB_CLOSE);
		} else {
			sb.append(serializeSimpleType(meta.info()));
		}
		sb.append(afterType());
		return sb.toString();
	}

	protected String serializeSimpleType(MetaInfo meta) {
		return meta.getSubtype().getId();
	}

	protected String afterMetadataEnd() {
		return ENTER;
	}

	protected String afterSubOpen() {
		return ENTER;
	}

	protected String indentationElement() {
		return TAB;
	}

	protected String afterType() {
		return SPACE;
	}

	protected String serializeSize(Metadata meta) {
		if (meta.info().getSubtype().isFixedSizeType()) {
			return EMPTY;
		}
		return (meta.kind().isComplex()) ? serializeComplexSize(meta.info()) : serializaSimpleSize(meta.info());
	}

	protected String serializaSimpleSize(MetaInfo meta) {
		if (meta.hasScale()) {
			return "" + meta.getSize() + Token.DECIMAL_SEP + meta.getScale() + afterSize();
		}
		return meta.getSize() + afterSize();
	}

	protected String serializeComplexSize(MetaInfo meta) {
		return meta.getSize() + afterSize();
	}

	protected String afterSize() {
		return SPACE;
	}

	protected String serializeOccurs(MetaInfo meta) {
		return "" + Token.OCCURS_BEGIN + meta.getMinOccurs() + Token.OCCURS_RANGE + meta.getMaxOccurs();
	}

	protected String serializeExtension(MetaInfo meta) {
		if (!meta.isExtended()) {
			return Syntax.EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		if (meta.hasDomain()) {
			String valueBegin = "";
			String valueEnd = "";
			if (meta.getSubtype().dataType().group().isDelimited()) {
				valueBegin = "" + Token.VALUE_BEGIN;
				valueEnd = "" + Token.VALUE_END;
			}
			sb.append(Token.DOMAIN_BEGIN);
			for (DomainItem item : meta.getDomain().items()) {
				sb.append(valueBegin).append(item.asSerial()).append(valueEnd).append(Token.DOMAIN_SEPARATOR);
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(Token.DOMAIN_END);
		}
		if (meta.hasDefaultValue()) {
			sb.append(serializaTag(Token.DEFAULT_VALUE, meta.getDefaultValue()));
		}
		if (meta.hasTags()) {
			sb.append(meta.getTags());
		}
		return sb.toString();
	}

	protected String serializaTag(char token, String value) {
		return SPACE + token + Token.VALUE_BEGIN + value + Token.VALUE_END;
	}

}
