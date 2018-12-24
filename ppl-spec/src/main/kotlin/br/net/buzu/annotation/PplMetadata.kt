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
package br.net.buzu.annotation

import br.net.buzu.lang.EMPTY
import br.net.buzu.model.Subtype
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * Metadata information about some annotated field or class. This information is mandatory
 * over reflection information.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class PplMetadata(

        // Basic
        val index: Int = EMPTY_INTEGER,
        val name: String = EMPTY_NAME,
        val subtype: Subtype = Subtype.EMPTY_SUBTYPE,
        val size: Int = EMPTY_INTEGER,
        val scale: Int = EMPTY_INTEGER,
        val minOccurs: Int = EMPTY_INTEGER,
        val maxOccurs: Int = EMPTY_INTEGER,

        // Extension
        val defaultValue: String = EMPTY_STRING,
        val domain: Array<String> = [],
        val tags: String = EMPTY_STRING,
        val key: Boolean = false,
        val indexed: Boolean = false,
        val align: Char = EMPTY_CHAR,
        val fillChar: Char = EMPTY_CHAR,
        val nullChar: Char = EMPTY_CHAR) {

    companion object {

        const val EMPTY_NAME = "EMPTY NAME"
        const val EMPTY_STRING = EMPTY
        const val EMPTY_INTEGER = -1
        const val EMPTY_CHAR = ' '
    }

}
