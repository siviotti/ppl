/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplspec.exception

import java.util.*

/**
 * Thrown when there is a type mismatch between a value and an metadata type.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class PplTypeMismatchException : PplException {

    constructor(toClass: Class<*>, text: String, vararg expected: Class<*>) : super("Type mismatch:" + text + "\nSupported list : " + Arrays.asList<Class<*>>(*expected) + " \nfound (toClass): " + toClass.canonicalName)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    companion object {

        private val serialVersionUID = 1L
    }

}
