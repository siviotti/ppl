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
package br.net.buzu.parsing.filter;

import java.util.logging.Logger;

import br.net.buzu.parsing.Text;
import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.model.Align;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public abstract class ParserFilter implements PayloadParser {

	private final PayloadParser next;

	public ParserFilter(PayloadParser next) {
		super();
		this.next = next;
	}

	@Override
	public <T> T parse(StaticMetadata metadata, String text, Metaclass toClass) {
		beforeParse(metadata, text, toClass);
		T t = onParse(next.parse(metadata, text, toClass));
		afterParse(metadata, text, toClass, t);
		return t;
	}

	protected abstract void beforeParse(StaticMetadata metadata, String text, Metaclass toClass);

	protected abstract <T> T onParse(Object parse);

	protected abstract void afterParse(StaticMetadata metadata, String text, Metaclass toClass, Object t);

	@Override
	public String serialize(StaticMetadata metadata, Object obj, Metaclass fromClass) {
		beforeSerialize(metadata, obj);
		String s = onSerialize(next.serialize(metadata, obj, fromClass));
		afterSerialize(metadata, obj, s);
		return s;
	}

	protected abstract void beforeSerialize(Metadata metadata, Object obj);

	protected abstract String onSerialize(String serialize);

	protected abstract void afterSerialize(Metadata metadata, Object obj, String s);
	
	protected void log(Metadata metadata, String text) {
		String parser;
		if (metadata.kind().isComplex()) {
			parser = "+[" + Text.fit(Align.LEFT, "" + getClass().getSimpleName(), 10, ' ') + "]";
		} else {
			parser = "-[" + Text.fit(Align.LEFT, "" + getClass().getSimpleName(), 10, ' ') + "]";
		}
		String meta = "[" + Text.fit(Align.LEFT, "" + metadata.name(), 10, ' ') + "]";
		String size = "[" + Text.fit(Align.RIGHT, "" + text.length(), 4, '0') + "]";
		Logger.getLogger(getClass().getCanonicalName()).info(parser + meta + size + text);
	}

}
