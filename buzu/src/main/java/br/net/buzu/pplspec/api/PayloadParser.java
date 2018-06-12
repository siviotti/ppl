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

import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.exception.PplSerializeException;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * Specification of a parser based on Metadata and positional split to
 * parse/serialize a Payload.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public interface PayloadParser {

	/**
	 * Convets the payload text to object based on Metadata.
	 * 
	 * @param metadata
	 *            The StaticMetadata used to parse the object.
	 * @param text
	 *            The text to be parsed.
	 * @param toClass
	 *            The class of the resulting object.
	 * @return The object resulting of parsing process.
	 * @throws PplParseException
	 */
	<T> T parse(StaticMetadata metadata, String text, Metaclass toClass);

	/**
	 * Convert the payload object to text based on Metadata.
	 * 
	 * @param metadata
	 *            The StaticMetadata used to serialize the object.
	 * @param obj
	 *            The serializable object.
	 * @param fromClass
	 *            Class information for serialization
	 * @return A String containing the object as text.
	 * @throws PplSerializeException
	 */
	String serialize(StaticMetadata metadata, Object obj, Metaclass fromClass);

}
