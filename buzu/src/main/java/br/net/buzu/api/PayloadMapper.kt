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
import br.net.buzu.model.Metaclass
import br.net.buzu.model.StaticMetadata

/**
 * Specification of a mapper based on Metadata and positional split to
 * stringToValue/serialize a Payload.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface PayloadMapper  {
    /**
     * Convets the payload text to object based on Metadata.
     *
     * @param metadata
     * The StaticMetadata used to stringToValue the object.
     * @param text
     * The text to be parsed.
     * @param toClass
     * The class of the resulting object.
     * @return The object resulting domainOf parsing process.
     * @throws PplParseException
     */
    fun <T> parse(metadata: StaticMetadata, text: String, toClass: Metaclass): T

    /**
     * Convert the payload object to text based on Metadata.
     *
     * @param metadata
     * The StaticMetadata used to serialize the object.
     * @param obj
     * The serializable object.
     * @param fromClass
     * Class information for serialization
     * @return A String containing the object as text.
     * @throws PplSerializeException
     */
    fun serialize(metadata: StaticMetadata, obj: Any, fromClass: Metaclass): String

}
