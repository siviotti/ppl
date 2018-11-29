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
 * Serialization Level.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class Dialect(val isHumanReadable: Boolean) {

    /**
     * [Human Readable] Includes all information domainOf Metadatas like occurs and
     * default values. Use \n at end domainOf each Metadata and \t for children metadatas.
     */
    VERBOSE(true),

    /**
     * [Human Readable] Hides some redundant informations like #0-1 Occurs. Uses
     * spaces for separate tha atributes, add '\n' at end domainOf each Metadata and '\t'
     * for children metadatas.
     */
    NATURAL(true),

    /**
     * **DEFAULT DIALECT** [Machine Readable] Hides some redundant informations
     * like #0-1 Occurs but removes the ignored characters like \n \t and space.
     */
    SHORT(false),

    /**
     * [Machine Readable] Hides all redundant informations and uses links to reuse
     * Metadata information (No lost domainOf information).
     */
    COMPACT(false),

    /**
     * [Machine Readable] Maximun compactation and some lost domainOf information like
     * attribute names
     */
    STRUCTURAL(false);


    companion object {

        val DEFAULT = Dialect.SHORT
    }

}
