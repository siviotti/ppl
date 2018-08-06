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
package br.net.buzu.pplspec.model;

import java.util.Objects;

import br.net.buzu.pplspec.annotation.PplMetadata;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.lang.Token;

/**
 * Basic Pojo resulting of annotations information or manual setting. This class
 * represents the PPL elements: name, type, size, scale, occurs , defaultValue,
 * domain, tags, key and indexed.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public final class MetaInfo implements Comparable<MetaInfo> {

	public static final int NO_SCALE = 0;

	// Basic
	private final String id;
	private final int index;
	private final String name;
	private final Subtype subtype;
	private final int size;
	private final int scale;
	private final int minOccurs;
	private final int maxOccurs;
	// Extension
	private final boolean extended;
	private final Domain domain;
	private final String defaultValue;
	private final String tags;
	private final Align align;
	private final char fillChar;

	public MetaInfo(String parentId, int index, String name, Subtype subtype, int size, int scale, int minOccurs,
			int maxOccurs) {
		this(parentId, index, name, subtype, size, scale, minOccurs, maxOccurs, null, Syntax.EMPTY, Syntax.EMPTY);
	}

	public MetaInfo(String parentId, PplMetadata pplMetadata, String originalFieldName, Subtype originalSubtype) {
		this(parentId, pplMetadata.index(), getName(pplMetadata, originalFieldName),
				getSubtype(pplMetadata, originalSubtype), pplMetadata.size(), pplMetadata.scale(),
				pplMetadata.minOccurs(), pplMetadata.maxOccurs(), Domain.of(pplMetadata.domain()),
				pplMetadata.defaultValue(), buildTags(pplMetadata));
	}

	/**
	 * @param index
	 * @param name
	 * @param subtype
	 *            The subtype based on Subtype enum. [CANNOT BE NULL]
	 * @param size
	 * @param scale
	 * @param minOccurs
	 * @param maxOccurs
	 * @param defaultValue
	 * @param domain
	 * @param tags
	 */
	public MetaInfo(String parentId, int index, String name, Subtype subtype, int size, int scale, int minOccurs,
			int maxOccurs, Domain domain, String defaultValue, String tags) {
		super();
		this.id = createId(parentId, name);
		this.index = index;
		this.name = name;
		this.subtype = Objects.requireNonNull(subtype, "'subtype' cannot be null");
		this.size = subtype.isFixedSizeType() ? subtype.fixedSize() : size;
		this.scale = (scale > NO_SCALE) ? scale : 0;
		checkOccurs(minOccurs, maxOccurs);
		this.minOccurs = minOccurs < 0 ? 0 : minOccurs;
		this.maxOccurs = maxOccurs;
		this.domain = (domain != null) ? domain : Domain.EMPTY;
		this.defaultValue = (defaultValue != null) ? defaultValue : Syntax.EMPTY;
		this.tags = (tags != null) ? tags : Syntax.EMPTY;
		this.extended = isExtended(this.domain, this.defaultValue, this.tags);
		this.align = getAlign(subtype, tags);
		this.fillChar = subtype.dataType().fillChar();
	}

	// static

	private static String createId(String parentId, String name) {
		if (parentId == null || parentId.isEmpty()) {
			return name != null ? name : Syntax.EMPTY;
		}
		return parentId + Token.PATH_SEP + name;
	}

	private static void checkOccurs(int minOccurs, int maxOccurs) {
		if (maxOccurs > 0 && minOccurs > maxOccurs) {
			throw new IllegalArgumentException(
					"minOccurs cannot be bigger than maxOccurs:" + minOccurs + ">" + maxOccurs);
		}
	}

	@SuppressWarnings("deprecation")
	private static Subtype getSubtype(PplMetadata pplMetadata, Subtype originalSubtype) {
		return Subtype.EMPTY_SUBTYPE.equals(pplMetadata.subtype()) ? originalSubtype : pplMetadata.subtype();
	}

	private static String getName(PplMetadata pplMetadata, String originalFieldName) {
		return PplMetadata.EMPTY_NAME.equals(pplMetadata.name()) ? originalFieldName : pplMetadata.name();
	}

	private static String buildTags(PplMetadata pplMetadata) {
		String tags = pplMetadata.tags() != null ? pplMetadata.tags() : "";
		StringBuilder sb = new StringBuilder(tags);
		if (tags == null) {
			return sb.append(Token.KEY).append(Token.INDEXED).toString();
		}
		if (pplMetadata.key() && tags.indexOf(Token.KEY) < 0) {
			sb.append(Token.KEY);
		}
		if (pplMetadata.indexed() && tags.indexOf(Token.INDEXED) < 0) {
			sb.append(Token.INDEXED);
		}
		if (PplMetadata.EMPTY_CHAR != pplMetadata.align()) {
			sb.append(pplMetadata.align());
		}

		return sb.toString();
	}

	private static String serializeDefaultvalue(String defaultValue) {
		if (defaultValue == null || defaultValue.trim().isEmpty()) {
			return Syntax.EMPTY;
		}
		return "" + Token.DEFAULT_VALUE + Token.VALUE_BEGIN + defaultValue + Token.VALUE_END;
	}

	private static boolean isExtended(Domain domain, String defaultValue, String tags) {
		return !domain.isEmpty() || !defaultValue.isEmpty() || !tags.isEmpty();
	}

	private static Align getAlign(final Subtype subtype, final String tags) {
		if (tags != null) {
			if (tags.indexOf(Token.LEFT) > -1) {
				return Align.LEFT;
			}
			if (tags.indexOf(Token.RIGHT) > -1) {
				return Align.RIGHT;
			}
		}
		return subtype.dataType().align();
	}

	// API

	/**
	 * A complete MetaInfo must have SIZE (size > -1) and MAX_OCCURS (maxOccurs >
	 * -1).
	 * 
	 * @return <code>true</code> if the MetaInfo is complete (has size and
	 *         maxOccurs) or <code>false</code> is is incomplete, has no size and/or
	 *         no maxOccurs.
	 */
	public boolean isComplete() {
		return hasSize() && hasMaxOccurs();
	}

	/**
	 * A static MetaInfo must be 'Complete' and not 'Unbounded'.
	 * 
	 * @return <code>true</code> if the MetaInfo is static (has size and maxOccurs
	 *         but not Unbounded) or <code>false</code> if is not static.
	 */
	public boolean isStatic() {
		return isComplete() && !isUnbounded();
	}

	public boolean hasSize() {
		return PplMetadata.EMPTY_INTEGER != size;
	}

	public boolean hasMaxOccurs() {
		return PplMetadata.EMPTY_INTEGER != maxOccurs;
	}

	public boolean hasScale() {
		return scale > NO_SCALE;
	}

	/**
	 * Returns if the Metainfo has no maxOccurs limit (maxOccurs == 0).
	 * 
	 * @return <code>true</code> if maxOccurs is equals zero<code>false</code> if
	 *         maxOccurs has a value and this is the limit.
	 */
	public boolean isUnbounded() {
		return Syntax.UNBOUNDED == maxOccurs;
	}

	/**
	 * Returns if the MetaInfo has domain, defaultValue or some tag defined.
	 * 
	 * @return <code>true</code> if use any extension property or <code>false</code>
	 *         if has no extension.
	 */
	public boolean isExtended() {
		return extended;
	}

	public MetaInfo update(int newSize, int newMaxOccurs) {
		return new MetaInfo(parentId(), maxOccurs, name, subtype, newSize, scale, minOccurs, newMaxOccurs, domain,
				defaultValue, tags);
	}

	public boolean hasIndex() {
		return PplMetadata.EMPTY_INTEGER != index;
	}

	public boolean isMultiple() {
		return maxOccurs != 1;
	}

	/**
	 * Indicates if the MetaInfo has 'minOccurs' bigger than 0. It means is
	 * required.
	 * 
	 * @return <code>true</code> if is required or <code>false</code>otherwise.
	 */
	public boolean isRequired() {
		return minOccurs > 0;
	}

	public boolean hasDomain() {
		return !domain.isEmpty();
	}

	public boolean hasDefaultValue() {
		return defaultValue != null && !defaultValue.isEmpty();
	}

	public boolean hasTags() {
		return tags != null && !tags.isEmpty();
	}

	public boolean inDomain(String valueItem) {
		if (domain.isEmpty()) {
			// Domain not defined
			return true;
		}
		if (valueItem == null) {
			// Domain defined and invalid item
			return false;
		}
		return domain.containsValue(valueItem);
	}

	public boolean isTagPresent(String tag) {
		return tags != null && tag != null && tags.contains(tag);
	}

	// att

	public String id() {
		return id;
	}

	public String parentId() {
		int pos = id.lastIndexOf(Token.PATH_SEP);
		return pos > -1 ? id.substring(0, pos) : "";
	}

	public int index() {
		return index;
	}

	public String name() {
		return name;
	}

	public Subtype subtype() {
		return subtype;
	}

	public int size() {
		return size;
	}

	public int scale() {
		return scale;
	}

	public int minOccurs() {
		return minOccurs;
	}

	public int maxOccurs() {
		return maxOccurs;
	}

	public String defaultValue() {
		return defaultValue;
	}

	public Domain domain() {
		return domain;
	}

	public String tags() {
		return tags;
	}

	public Align align() {
		return align;
	}

	public char fillChar() {
		return fillChar;
	}

	public String ppl() {
		return name + Token.NAME_END + subtype.getId() + size + Token.OCCURS_BEGIN + minOccurs + Token.OCCURS_RANGE
				+ maxOccurs + domain.asSerial() + serializeDefaultvalue(defaultValue) + tags;

	}

	// Common Methods

	@Override
	public int compareTo(MetaInfo o) {
		if (index == o.index) {
			return 0;
		}
		return (index < o.index) ? -1 : 1;
	}

	@Override
	public String toString() {
		return id + "[" + index + "] " + ppl();
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() == obj.getClass()) {
			return id.equals(((MetaInfo) obj).id);
		}

		return false;
	}

}
