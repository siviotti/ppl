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
package br.net.buzu.model

fun kindOf(multiple: Boolean, complex: Boolean): Kind {
    return if (multiple) {
        if (complex) Kind.TABLE else Kind.ARRAY
    } else {
        if (complex) Kind.RECORD else Kind.ATOMIC
    }
}

/**
 * List of Metadata Kind: ATOMIC, ARRAY, RECORD and TABLE
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
enum class Kind(
        /**
         * Indicates if a metadata allows multiple values.
         *
         * @return `true` if the metadata supports many values or
         * `false` if supports only one.
         */
        val isMultiple: Boolean,
        /**
         * Indicates if a metadata is composed for other metadatas.
         *
         * @return `true` if a metadata is composed by others or
         * `false` if its value is a simple value.
         */
        val isComplex: Boolean) {

    /** One single value. String, Integer, Boolean etc.  */
    ATOMIC(false, false),
    /** Many single values. List, Set, Arrays etc.  */
    ARRAY(true, false),
    /** One complex value. Customer, Person etc.  */
    RECORD(false, true),
    /** Many complex values. List(Customer), Set(Person) etc.  */
    TABLE(true, true);


}
