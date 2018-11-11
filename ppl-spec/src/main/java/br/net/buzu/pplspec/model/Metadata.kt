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
package br.net.buzu.pplspec.model

/**
 * Represents a element of a positional tree.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface Metadata : Nameable {

    /**
     * Indicates if the Structure (Metadata) is Static or Dynamic. <BR></BR>
     * Static - All 'sizes' and 'maxOccurs' has a value. <BR></BR>
     * Dynamic - Any 'size' has no value or any maxOccurs is Unbounded (no limit).
     *
     * @return `true` if the Structure or Metadata is static or
     * `false` is not (Dynamic).
     */
    val isStatic: Boolean
        get() = if (hasChildren()) isStatic(children()) else info().isStatic

    /**
     * Retuns the Kind of the Element: ATOMIC, ARRAY, RECORD or TABLE.
     *
     * @return The Kind instance of [Kind]
     */
    fun kind(): Kind

    /**
     * Returns the informations about an element.
     *
     * @return The instance of `MetaInfo` containing the basic element
     * informations like name, size, ocurrences, subtype etc.
     * @see MetaInfo
     */
    fun info(): MetaInfo

    /**
     * Returns tha list of subElements.
     *
     * @return The children elements.
     */
    fun <T : Metadata> children(): List<T>

    /**
     * Indicates if the element has children.
     *
     * @return `true` if has children or `false` if not.
     */
    fun hasChildren(): Boolean

    /**
     * [Debug method] Returns ths Metadata as a Tree.
     *
     * @param level
     * @return
     */
    fun toTree(level: Int): String

    /**
     * [Debug method] Returns ths Metadata as plain data
     *
     * @param level
     * @return
     */
    fun toPlain(): String

    companion object {

        /**
         * @param children
         * @return
         */
        fun isStatic(children: Collection<Metadata>): Boolean {
            for (child in children) {
                if (!child.isStatic) {
                    return false
                }
            }
            return true
        }
    }

}
