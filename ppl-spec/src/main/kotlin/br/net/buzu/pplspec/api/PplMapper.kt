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
package br.net.buzu.pplspec.api

import br.net.buzu.pplspec.exception.PplParseException
import br.net.buzu.pplspec.exception.PplSerializeException
import br.net.buzu.pplspec.model.MetaType
import br.net.buzu.pplspec.model.StaticMetadata

/**
 * Extended Mapper to transform PPL to Object and Object to PPL using parameterized methods.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface PplMapper : PplSimpleMapper {

    /**
     * Parsers a PPL text and returns the correspondent object based on the
     * indicated MetaType. If the PPL text represents multiple records, the result will
     * be a Collection parametrized to the 'type'. If the PPL text represents only
     * one record, the result is a single object.
     *
     * @param text
     * The Metadata (Static) corresponding to the first part of 'text' parameter.
     * @param metaType
     * The MetaType corresponding to the original Type (class).
     * @return The object corresponding to the PPL text.
     * @throws PplParseException
     */
    fun <T> fromPpl(text: String, metaType: MetaType): T

    /**
     * Parsers a PPL text and returns the correspondent object based on the
     * indicated MetaType. If the PPL text represents multiple records, the result will
     * be a Collection parametrized to the 'type'. If the PPL text represents only
     * one record, the result is a single object.
     *
     * @param text
     * The Metadata (Static) corresponding to the first part of 'text' parameter.
     * @param metaType
     * The PPL text at the format `(METADATA)PAYLOAD`.
     * @param metadata
     * The MetaType corresponding to the original Type (class).
     * @return The object corresponding to the PPL text.
     * @throws PplParseException
     */
    fun <T> fromPpl(text: String, metaType: MetaType, metadata: StaticMetadata): T

    /**
     * Transforms an object into PPL text (Serialization).
     *
     * @param source
     * The object to be serialized.
     * @param metaType
     * The MetaType corresponding to the original Type (class) of 'source' parameter.
     * @return The PPL text corresponding to the object.
     * @throws PplSerializeException
     */
    fun toPpl(source: Any, metaType: MetaType): String
    /**
     * Transforms an object into PPL text (Serialization).
     *
     * @param source
     * The object to be serialized.
     * @param metaType
     * The MetaType corresponding to the original Type (class) of 'source' parameter.
     * @param metadata
     * The Metadata (Static) corresponding to the 'source' parameter
     * @return The PPL text corresponding to the object.
     * @throws PplSerializeException
     */
    fun toPpl(source: Any, metaType: MetaType, metadata: StaticMetadata): String

}