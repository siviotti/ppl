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

/**
 * Lista com as estratégias de definição de tamanho dos campos.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public enum SizeType {
	
	/** Tamanho fixo que não pode ser customizado */
	FIXED(true),
	/** Tamanho indefinido que deve ser customizado */
	CUSTOM(false),
	/** Tamanho dos tipos complexos que somam o tamanho das suas partes */
	SUM(true);
	
	private final boolean fixed;

	private SizeType(boolean fixed) {
		this.fixed = fixed;
	}

	public boolean isFixed() {
		return fixed;
	}

}
