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
package br.net.buzu.parsing.simple.bool;

import br.net.buzu.pplspec.model.Subtype;

/**
 * Parser to {@link Subtype#BOZ}
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class BozParser extends AbstractBooleanCharParser {

	public static final BozParser INSTANCE = new BozParser();

	@Override
	protected char getTrueValue() {
		return '1'; // One
	}

	@Override
	protected char getFalseValue() {
		return '0'; // Zero
	}

}
