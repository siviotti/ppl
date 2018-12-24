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
package br.net.buzu.metadata.build.load;

import br.net.buzu.lang.Token;
import br.net.buzu.model.Metaclass;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class MaxMap {

	private Map<String, Max> map = new HashMap<>();

	public Max get(String fieldPath) {
		if (!map.containsKey(fieldPath)) {
			map.put(fieldPath, new Max());
		}
		return map.get(fieldPath);
	}

	static String getFieldPath(Metaclass metaclass, LoadNode node) {
		if (node.getFieldPath().isEmpty()) {
			return metaclass.fieldName();
		}
		return node.getFieldPath() + Token.PATH_SEP + metaclass.fieldName();
	}

}
