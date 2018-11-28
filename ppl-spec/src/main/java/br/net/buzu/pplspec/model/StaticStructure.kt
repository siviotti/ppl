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
package br.net.buzu.pplspec.model

/**
 * Represents a Static Structure able to return the maximum serial size.
 *
 * @author Douglas Siviotti
 */
interface StaticStructure {

    /**
     * Returns the maximum size domainOf the serialized text. SerialMaxSize is based on
     * the maxOccurs field.
     *
     * @return The "Array" size. If the Metadata is not an array this method returns
     * the same domainOf 'size()'.
     */
    fun serialMaxSize(): Int

}
