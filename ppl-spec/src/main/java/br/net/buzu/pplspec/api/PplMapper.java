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
package br.net.buzu.pplspec.api;

import java.util.List;
import java.util.Map;

import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.exception.PplSerializeException;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * Mapper to transform PPL to Object and Object to PPL.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public interface PplMapper {

	// **************************************************
	// API TEXT->OBJECT
	// **************************************************

	/**
	 * @param text
	 * @return
	 * @throws PplParseException
	 */
	Map<String, Object> fromPpl(String text);

	/**
	 * Parsers a PPL text and returns the correspondent object based on the
	 * indicated type. If the PPL text represents multiple records, the result will
	 * be a Collection parametrized to the 'type'. If the PPL text represents only
	 * one record, the result is a single object.
	 * 
	 * @param text
	 *            The PPL text at the format <code>(METADATA)PAYLOAD</code>.
	 * @param type
	 *            The type of the object will be created.
	 * @return The object corresponding to the PPL text.
	 * @throws PplParseException
	 */
	<T> T fromPpl(String text, Class<T> type);

	/**
	 * Parsers a PPL text and always returns a correspondent <b>List</b> of
	 * elements.
	 * 
	 * @param text
	 *            The PPL text at the format <code>(METADATA)PAYLOAD</code>.
	 * @param elementType
	 *            The type of the element of the List which will be created.
	 * @return The Collection corresponding to the PPL text.
	 * @throws PplParseException
	 */
	<T> List<T> fromPplList(String text, Class<T> elementType);

	/**
	 * Parsers a PPL text and returns the correspondent object based on the
	 * indicated Metaclass (class information).
	 * 
	 * @param text
	 *            The PPL text at the format <code>(METADATA)PAYLOAD</code>.
	 * @param toClass
	 *            The Metaclass containing the type of the created object.
	 * @return The object corresponding to the PPL text.
	 * @throws PplParseException
	 */
	<T> T fromPpl(String text, Metaclass toClass);

	/**
	 * Parsers a Payload (Positional Text) and returns the correspondent object
	 * based on the indicated StaticMetadata and the Metaclass.
	 * 
	 * @param metadata
	 *            The static Metadata wich describes the payload.
	 * @param payload
	 *            The positional text described by the static metadata
	 * @param toClass
	 *            The Metaclass of the resulted object after parsing.
	 * @return The object created from the payload.
	 */
	<T> T fromPayload(StaticMetadata metadata, String payload, Metaclass toClass);

	// **************************************************
	// API OBJECT->TEXT
	// **************************************************

	/**
	 * Transforms an object into PPL text (Serialization).
	 * 
	 * @param source
	 *            The object to be serialized.
	 * @return The PPL text corresponding to the object.
	 * @throws PplSerializeException
	 */
	String toPpl(Object source);

	/**
	 * Transforms an object into PPL text (Serialization).
	 * 
	 * @param source
	 *            The object to be serialized. A StaticMetadata will be created
	 *            based on this object.
	 * @param fromClass
	 *            The metaclass corresponding the object structure based on its
	 *            class.
	 * @return The PPL text corresponding to the object.
	 * @throws PplSerializeException
	 */
	String toPpl(Object source, Metaclass fromClass);

	/**
	 * Transforms an object into PPL text (Serialization) using a StaticMetadata.
	 * 
	 * @param metadata
	 *            The StaticMetadata used to serialize the source object.
	 * @param source
	 *            The object to be serialized.
	 * @param fromClass
	 *            The metaclass corresponding the object structure based on its
	 *            class..
	 * @return The PPL text corresponding to the object.
	 * @throws PplSerializeException
	 */
	String toPpl(StaticMetadata metadata, Object source, Metaclass fromClass);

}
