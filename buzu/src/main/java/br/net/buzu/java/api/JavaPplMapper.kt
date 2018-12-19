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
import br.net.buzu.java.model.Metaclass
import br.net.buzu.java.model.StaticMetadata

/**
 * Mapper to transform PPL to Object and Object to PPL.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface JavaPplMapper {

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
     * indicated elementType. If the PPL text represents multiple records, the result will
     * be a Collection parametrized to the 'elementType'. If the PPL text represents only
     * one record, the result is a single object.
     *
     * @param text
     * The PPL text at the format `(METADATA)PAYLOAD`.
     * @param type
     * The elementType of the object will be created.
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
     * The elementType of the element of the List which will be created.
     * @return The Collection corresponding to the PPL text.
     * @throws PplParseException
     */
    fun <T> fromPplList(text: String, elementType: Class<T>): List<T>

    /**
     * Parsers a PPL text and returns the correspondent object based on the
     * indicated Metaclass (class information).
     *
     * @param text
     * The PPL text at the format `(METADATA)PAYLOAD`.
     * @param toClass
     * The Metaclass containing the elementType of the created object.
     * @return The object corresponding to the PPL text.
     * @throws PplParseException
     */
    fun <T> fromPpl(text: String, toClass: Metaclass): T

    /**
     * Parsers a Payload (Positional Text) and returns the correspondent object
     * based on the indicated StaticMetadata and the Metaclass.
     *
     * @param metadata
     * The static Metadata wich describes the payload.
     * @param payload
     * The positional text described by the static metadata
     * @param toClass
     * The Metaclass of the resulted object after parsing.
     * @return The object created from the payload.
     */
    fun <T> fromPayload(metadata: StaticMetadata, payload: String, toClass: Metaclass): T

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

    /**
     * Transforms an object into PPL text (Serialization).
     *
     * @param source
     * The object to be serialized. A StaticMetadata will be created
     * based on this object.
     * @param fromClass
     * The metaclass corresponding the object structure based on its
     * class.
     * @return The PPL text corresponding to the object.
     * @throws PplSerializeException
     */
    fun toPpl(source: Any, fromClass: Metaclass): String

    /**
     * Transforms an object into PPL text (Serialization) using a StaticMetadata.
     *
     * @param metadata
     * The StaticMetadata used to serialize the source object.
     * @param source
     * The object to be serialized.
     * @param fromClass
     * The metaclass corresponding the object structure based on its
     * class..
     * @return The PPL text corresponding to the object.
     * @throws PplSerializeException
     */
    fun toPpl(metadata: StaticMetadata, source: Any, fromClass: Metaclass): String

}
