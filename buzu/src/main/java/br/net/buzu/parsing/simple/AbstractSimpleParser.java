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
package br.net.buzu.parsing.simple;

import static br.net.buzu.lib.TextKt.*;

import br.net.buzu.parsing.AbstractPositionalParser;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * Superclass domainOf simple parsers (String, Integer Boolean etc).
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public abstract class AbstractSimpleParser extends AbstractPositionalParser {

	// ******************** PARSE ********************
	@Override
	protected Object doParse(StaticMetadata metadata, String text, Metaclass toClass) {
		if (isNull(text, metadata.info().getSubtype().dataType().nullChar())) {
			if (metadata.info().hasDefaultValue()) {
				text = metadata.info().getDefaultValue();
			} else {
				return null;
			}
		}
		int beginIndex = 0;
		int endIndex = 0;
		Object[] array = createAndFillArray(toClass, metadata.info().getMaxOccurs());
		for (int i = 0; i < array.length; i++) {
			endIndex += metadata.info().getSize();
			array[i] = asSingleObject(metadata, text.substring(beginIndex, endIndex), toClass);
			beginIndex += metadata.info().getSize();
		}
		return fromArray(array, toClass);

	}

	protected boolean isNull(String text, char nullChar) {
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) != nullChar) {
				return false;
			}
		}
		return true;
	}

	protected abstract Object asSingleObject(StaticMetadata metadata, String text, Metaclass metaClass);

	// ******************** SERIALIZE ********************

	protected String serializeNotNull(StaticMetadata metadata, Object obj, Metaclass metaClass) {
		StringBuilder sb = new StringBuilder();
		Object[] array = toMaxArray(obj, metadata.info().getMaxOccurs());
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				sb.append(serializeNullElement(metadata.info()));
			} else {
				sb.append(serializeElement(metadata.info(), array[i]));
			}
		}
		return sb.toString();
	}

	/**
	 * @see MetaInfo#getDefaultValue()
	 */
	protected String serializeNullElement(MetaInfo meta) {
		return fill(meta.getAlign(), meta.getDefaultValue(), meta.getSize(), meta.getSubtype().dataType().nullChar());
	}

	protected String serializeElement(MetaInfo meta, Object obj) {
		return fit(meta.getAlign(), asStringFromNotNull(meta, obj), meta.getSize(), meta.getFillChar());
	}

	protected abstract String asStringFromNotNull(MetaInfo meta, Object obj);

}
