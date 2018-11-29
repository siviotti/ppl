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
 * This exception is rised when a value violates a constraint (domain or regex).
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class PplConstraintViolationException : PplException {

    constructor(message: String) : super(message)

    constructor(value: String, constraint: String) : super("The value '$value' violates the constraint $constraint")

    companion object {

        private val serialVersionUID = 1L
    }

}
