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
package br.net.buzu.api

import br.net.buzu.exception.PplParseException
import br.net.buzu.exception.PplSerializeException

/**
 * Specification of the most simple mapper to transform PPL to Object and Object to PPL.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface PplSimpleMapper{

    /**
     * Parsers a PPL text and returns the correspondent object based on the
     * indicated elementType. If the PPL text represents multiple records, the result will
     * be a Collection parametrized to the 'elementType'. If the PPL text represents only
     * one record, the result is a single object.
     *
     * @param text
     * The PPL text at the format `(METADATA)PAYLOAD`.
     * @param elementType
     * The elementType of the object will be created.
     * @return The object corresponding to the PPL text (single or list).
     * @throws PplParseException
     */
    fun fromPpl(text: String, elementType: Class<*>): Any?


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