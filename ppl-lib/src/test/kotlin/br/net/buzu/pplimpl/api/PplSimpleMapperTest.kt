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
package br.net.buzu.pplimpl.api

import br.net.buzu.api.PplSimpleMapper
import br.net.buzu.sample.order.Order
import br.net.buzu.sample.pojo.Person
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PplSimpleMapperTest {

    private val mapper: PplSimpleMapper = pplSimpleMapper()

    @Test
    fun testFromPpl() {
        assertEquals(Person.INSTANCE, mapper.fromPpl(Person.PPL_STRING, Person::class.java))
        assertEquals(Order.INSTANCE, mapper.fromPpl(Order.PPL_STRING, Order::class.java))
    }

    @Test
    fun testToPpl() {
        assertEquals(Person.PPL_STRING, mapper.toPpl(Person.INSTANCE))
        assertEquals(Order.PPL_STRING, mapper.toPpl(Order.INSTANCE))
    }
}