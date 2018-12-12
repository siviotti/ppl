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
package br.net.buzu.pplimpl.payload

import br.net.buzu.pplspec.api.ValueParser
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.StaticMetadata

fun parsePayload(text: String, metadata: StaticMetadata, valueParser: ValueParser): Any {
    return if (metadata.hasChildren()) {
        parseComplexPayload(text, 0, metadata, valueParser)
    } else {
        parseSimplePayload(text, 0, metadata, valueParser)
    }
}

fun parseComplexPayload(text: String, index: Int, metadata: StaticMetadata, valueParser: ValueParser): Any {
    for (metadataChild in metadata.children<Metadata>()){

    }
    return ""
}

fun parseSimplePayload(text: String, index: Int, metadata: StaticMetadata, valueParser: ValueParser): Any {
    return valueParser.parse(text.substring(index,metadata.serialMaxSize()))
}