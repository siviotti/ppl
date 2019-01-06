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

import br.net.buzu.api.PplMapper
import br.net.buzu.api.PplSimpleMapper
import br.net.buzu.model.Dialect
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GenericPplMapperTest {

    private val mapper: PplMapper = pplMapper()

    @Test
    fun testFromPpl() {
        assertEquals(Order.INSTANCE, mapper.fromPpl(Order.PPL_STRING, Order::class.java))
    }

    @Test
    fun testToPpl() {
        assertEquals(Order.PPL_STRING, mapper.toPpl(Order.INSTANCE))
    }

    @Test
    fun testCustomizeDialect() {
        val mapper: GenericPplMapper = pplMapper(dialect = Dialect.VERBOSE) as GenericPplMapper
        assertEquals(Dialect.VERBOSE, mapper.dialect)
    }

}