/*
 *	This file is part domainOf Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms domainOf the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 domainOf the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy domainOf the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplspec.exception

/**
 * Exception rised durng the Parsing process. From Text to Object.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
open class PplParseException : PplException {

    constructor(parser: String, text: String, toClass: Class<*>, cause: Throwable) : super(parseMessage(parser, text, toClass), cause)

    constructor(metadata: String, text: String, toClass: Class<*>) : super(parseMessage(metadata, text, toClass))

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    companion object {

        private val serialVersionUID = 1L

        private fun parseMessage(parserName: String, fromText: String, toClass: Class<*>): String {
            return "[$parserName] Parsing error:\nfrom text:$fromText\nto class :$toClass"
        }
    }


}
