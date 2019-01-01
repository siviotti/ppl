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
package br.net.buzu.model

/**
 * Represents an Adapter for get/set field values
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface TypeAdapter {

    fun getFieldValue(parentObject: Any): Any?

    fun setFieldValue(parentObject: Any, paramValue: Any?)

    fun maxArrayToValue(array: Array<Any?>): Any?

    fun valueToMaxArray(value: Any?, size: Int): Array<Any?>

    fun createAndFillArray(size: Int): Array<Any?>

    fun valueToArray(value: Any?): Array<Any?>

    fun enumConstantToValue(constName: String): Any

}