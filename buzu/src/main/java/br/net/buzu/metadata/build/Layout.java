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
package br.net.buzu.metadata.build;

import java.util.ArrayList;
import java.util.List;

import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Subtype;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class Layout {

	static final String ROOT = "root";
	static final int NO_SIZE = -1;
	static final int NO_PRECISION = 0;
	static final String NONE_OPENED_LAYOUT = "None opened Layout";

	private final MetaInfo metaInfo;
	private final List<Layout> children = new ArrayList<>();
	private final Layout parent;
	private final int level;

	public Layout() {
		this(new MetaInfo("", 0, ROOT, Subtype.OBJ, NO_SIZE, NO_PRECISION, 0, 0), null);
	}

	public Layout(String name) {
		this(new MetaInfo("", 0, name, Subtype.OBJ, NO_SIZE, NO_PRECISION, Syntax.DEFAULT_MIN_OCCURS,
				Syntax.DEFAULT_MAX_OCCURS), null);
	}

	public Layout(String name, Subtype subtype) {
		this(new MetaInfo("", 0, name, subtype, NO_SIZE, NO_PRECISION, Syntax.DEFAULT_MIN_OCCURS,
				Syntax.DEFAULT_MAX_OCCURS), null);
	}

	public Layout(String name, int size) {
		this(new MetaInfo("", 0, name, Subtype.OBJ, size, 0, Syntax.DEFAULT_MIN_OCCURS, Syntax.DEFAULT_MAX_OCCURS),
				null);
	}

	public Layout(String name, int size, int minOccurs, int maxOccurs) {
		this(new MetaInfo("", 0, name, Subtype.OBJ, size, NO_PRECISION, minOccurs, maxOccurs), null);
	}

	public Layout(String name, Subtype subtype, int size, int minOccurs, int maxOccurs) {
		this(new MetaInfo("", 0, name, subtype, size, NO_PRECISION, minOccurs, maxOccurs), null);
	}

	public Layout(String name, Subtype subtype, int size, int scale, int minOccurs, int maxOccurs) {
		this(new MetaInfo("", 0, name, subtype, size, scale, minOccurs, maxOccurs), null);
	}

	public Layout(String name, Subtype subtype, int size, int scale, int minOccurs, int maxOccurs, String defaultValue,
			List<String> domain, String tags) {
		this(new MetaInfo("", 0, name, subtype, size, scale, minOccurs, maxOccurs, domain, defaultValue, tags), null);
	}

	Layout(MetaInfo metaInfo, Layout parent) {
		super();
		this.metaInfo = metaInfo;
		this.parent = parent;
		this.level = parent != null ? parent.level + 1 : 0;
	}

	// **************************************************
	// DSL
	// **************************************************

	public Layout add(Layout child) {
		children.add(child);
		return this;
	}

	public Layout field(String name, Subtype subtype, int size, int precision, int minOccurs, int maxOccurs,
			String defaultValue, List<String> domain, String tags) {
		Layout child = new Layout(new MetaInfo(metaInfo.id(), childrenSize(), name, subtype, size, precision, minOccurs,
				maxOccurs, domain, defaultValue, tags), this);
		add(child);

		return subtype.dataType().isComplex() ? child : this;
	}

	public Layout field(String name, Subtype subtype, int size, int minOccurs, int maxOccurs) {
		return field(name, subtype, size, NO_PRECISION, minOccurs, maxOccurs, Syntax.EMPTY, null, Syntax.EMPTY);
	}

	public Layout field(String name, Subtype subtype, int size, int precision, int minOccurs, int maxOccurs) {
		return field(name, subtype, size, precision, minOccurs, maxOccurs, Syntax.EMPTY, null, Syntax.EMPTY);
	}

	public Layout end() {
		if (parent == null) {
			throw new IllegalStateException(NONE_OPENED_LAYOUT);
		}
		return parent;
	}

	// **************************************************
	// KIND API
	// **************************************************

	// Atomic

	public Layout atomic(String name) {
		return atomic(name, Subtype.DEFAULT_SINGLE);
	}

	public Layout atomic(String name, Subtype subtype) {
		return atomic(name, subtype, NO_SIZE);
	}

	public Layout atomic(String name, int size) {
		return atomic(name, Subtype.DEFAULT_SINGLE, size);
	}

	public Layout atomic(String name, Subtype subtype, int size) {
		return field(name, subtype, size, Syntax.DEFAULT_MIN_OCCURS, 1);
	}

	public Layout atomic(String name, Subtype subtype, boolean required) {
		return field(name, subtype, NO_SIZE, getMinOccurs(required), 1);
	}

	public Layout atomic(String name, Subtype subtype, int size, boolean required) {
		return field(name, subtype, size, getMinOccurs(required), 1);
	}

	public Layout atomic(String name, Subtype subtype, int size, int precison, boolean required) {
		return field(name, subtype, size, precison, getMinOccurs(required), 1);
	}

	public Layout atomic(String name, Subtype subtype, int size, boolean required, String defaultValue,
			List<String> domain, String tags) {
		return field(name, subtype, size, NO_PRECISION, getMinOccurs(required), 1, defaultValue, domain, tags);
	}

	public Layout atomic(String name, Subtype subtype, int size, int precision, boolean required, String defaultValue,
			List<String> domain, String tags) {
		return field(name, subtype, size, precision, getMinOccurs(required), 1, defaultValue, domain, tags);
	}

	// Array

	public Layout array(String name, Subtype subtype, int size) {
		return field(name, subtype, size, Syntax.DEFAULT_MIN_OCCURS, Syntax.UNBOUNDED);
	}

	public Layout array(String name, Subtype subtype, int size, boolean required) {
		return field(name, subtype, size, getMinOccurs(required), Syntax.UNBOUNDED);
	}

	public Layout array(String name, Subtype subtype, int size, int minOccurs, int maxOccurs) {
		return field(name, subtype, size, minOccurs, maxOccurs);
	}

	// Record

	public Layout record(String name) {
		return field(name, Subtype.OBJ, NO_SIZE, Syntax.DEFAULT_MIN_OCCURS, 1);
	}

	public Layout record(String name, boolean required) {
		return field(name, Subtype.OBJ, NO_SIZE, getMinOccurs(required), 1);
	}

	// Table

	public Layout table(String name) {
		return field(name, Subtype.OBJ, NO_SIZE, Syntax.DEFAULT_MIN_OCCURS, Syntax.UNBOUNDED);
	}

	public Layout table(String name, boolean required) {
		return field(name, Subtype.OBJ, NO_SIZE, getMinOccurs(required), Syntax.UNBOUNDED);
	}

	public Layout table(String name, int minOccurs, int maxOccurs) {
		return field(name, Subtype.OBJ, NO_SIZE, minOccurs, maxOccurs);
	}

	// **************************************************
	// ETC
	// **************************************************

	public String toTree(int level) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int i = 0; i < level; i++) {
			sb.append(".");
		}
		sb.append(metaInfo.toString());
		if (!children.isEmpty()) {
			for (Layout node : children) {
				sb.append(node.toTree(level + 1));
			}
		}
		return sb.toString();
	}

	private int getMinOccurs(boolean required) {
		return required ? 1 : 0;
	}

	public MetaInfo metaInfo() {
		return metaInfo;
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public int childrenSize() {
		return children.size();
	}

	public List<Layout> getChildren() {
		return new ArrayList<>(children);
	}

	public Layout last() {
		return children.get(0);
	}

	public Layout parent() {
		return parent;
	}

	@Override
	public String toString() {
		return metaInfo + " " + childrenSize();
	}

}
