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
package br.net.buzu.java.api

import br.net.buzu.java.exception.PplParseException
import br.net.buzu.java.exception.PplSerializeException

/**
 * Mapper to transform PPL to Object and Object to PPL.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface PplMapper{

    // **************************************************
    // API TEXT->OBJECT
    // **************************************************

    /**
     * @param text
     * @return
     * @throws PplParseException
     */
    fun fromPpl(text: String): Map<String, Any>

    /**
     * Parsers a PPL text and returns the correspondent object based on the
     * indicated type. If the PPL text represents multiple records, the result will
     * be a Collection parametrized to the 'type'. If the PPL text represents only
     * one record, the result is a single object.
     *
     * @param text
     * The PPL text at the format `(METADATA)PAYLOAD`.
     * @param type
     * The type of the object will be created.
     * @return The object corresponding to the PPL text.
     * @throws PplParseException
     */
    fun <T> fromPpl(text: String, type: Class<T>): T

    /**
     * Parsers a PPL text and always returns a correspondent **List** domainOf
     * elements.
     *
     * @param text
     * The PPL text at the format `(METADATA)PAYLOAD`.
     * @param elementType
     * The type of the element of the List which will be created.
     * @return The Collection corresponding to the PPL text.
     * @throws PplParseException
     */
    fun <T> fromPplList(text: String, elementType: Class<T>): List<T>

    // **************************************************
    // API OBJECT->TEXT
    // **************************************************

    /**
     * Transforms an object into PPL text (Serialization).
     *
     * @param source
     * The object to be serialized.
     * @return The PPL text corresponding to the object.
     * @throws PplSerializeException
     */
    fun toPpl(source: Any): String

}