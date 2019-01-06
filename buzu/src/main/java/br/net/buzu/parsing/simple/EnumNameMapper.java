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
package br.net.buzu.parsing.simple;

import br.net.buzu.exception.PplParseException;
import br.net.buzu.model.MetaInfo;
import br.net.buzu.model.Metaclass;
import br.net.buzu.model.StaticMetadata;

import java.lang.reflect.Field;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class EnumNameMapper extends AbstractSimpleMapper {

	public static final EnumNameMapper INSTANCE = new EnumNameMapper();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object asSingleObject(StaticMetadata metadata, String text, Metaclass metaClass) {
		Field[] fields = metaClass.elementType().getDeclaredFields();
		String constName = text.trim();
		for (Field field : fields) {
			if (field.isEnumConstant() && (field.getName().equals(constName))) {
				return Enum.valueOf((Class<Enum>) field.getType(), constName);
			}
		}
		throw new PplParseException("The text '" + text + "' is missing at enum " + metaClass.elementType());
	}

	@Override
	protected String asStringFromNotNull(MetaInfo meta, Object obj) {
		return ((Enum<?>) obj).name();
	}

}
