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
package br.net.buzu.metadata.build.load;

/**
 * Max Size and Occurrences calculation Helper
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class Max {

	private int maxSize;

	private int maxOccurs;

	public Max() {
		super();
	}

	public Max(int maxSize, int maxOccurs) {
		super();
		this.maxSize = maxSize;
		this.maxOccurs = maxOccurs;
	}

	public Max tryNewMaxOccurs(int newValue) {
		if (newValue > maxOccurs) {
			maxOccurs = newValue;
		}
		return this;
	}

	public Max tryNewMaxSize(int newValue) {
		if (newValue > maxSize) {
			maxSize = newValue;
		}
		return this;
	}

	public int getMaxOccurs() {
		return maxOccurs;
	}

	public int getMaxSize() {
		return maxSize;
	}

}
