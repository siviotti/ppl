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

/**
 * Exception relative to Reflection.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class PplReflectionException : PplException {

    constructor(notFoundMethod: String, beanClass: Class<*>, cause: Throwable) : super(msgMethodNotFound(beanClass, notFoundMethod), cause)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String) : super(message)

    companion object {

        private val serialVersionUID = 1L

        private fun msgMethodNotFound(beanClass: Class<*>, notFoundMethod: String): String {
            val sb = StringBuilder()
            sb.append("Method '").append(notFoundMethod).append("' not found in class ")
                    .append(beanClass.canonicalName).append(". ")
            return sb.toString()
        }
    }

}
