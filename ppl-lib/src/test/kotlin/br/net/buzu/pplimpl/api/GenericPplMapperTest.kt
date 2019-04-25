/*
 *	This file is part of DefaultPplMapper.
 *
 *   DefaultPplMapper is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DefaultPplMapper is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with DefaultPplMapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.api

import br.net.buzu.pplspec.api.PplMapper
import br.net.buzu.pplspec.model.Dialect
import br.net.buzu.sample.order.Order
import br.net.buzu.sample.pojo.Person
import br.net.buzu.sample.ppl.Human
import br.net.buzu.sample.ppl.StaticPerson
import br.net.buzu.sample.ppl.Xmen
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GenericPplMapperTest {

    private val mapper: PplMapper = pplMapperOf()

    @Test
    fun testShouldParseUsingFromPpl() {
        assertEquals(Person.INSTANCE, mapper.fromPpl(Person.PPL_STRING, Person::class.java))
        assertEquals(Person.MULTI_INSTANCE, mapper.fromPpl(Person.PPL_MULTI_STRING, Person::class.java))
        assertEquals(StaticPerson.INSTANCE, mapper.fromPpl(StaticPerson.PPL_STRING, StaticPerson::class.java))
        assertEquals(Human.INSTANCE, mapper.fromPpl(Human.PPL_STRING, Human::class.java))
        assertEquals(Xmen.INSTANCE, mapper.fromPpl(Xmen.PPL_STRING, Xmen::class.java))
        assertEquals(Order.INSTANCE, mapper.fromPpl(Order.PPL_STRING, Order::class.java))
    }

    @Test
    fun testShouldSerializeUsingToPpl() {
        assertEquals(Person.PPL_STRING, mapper.toPpl(Person.INSTANCE))
        assertEquals(Person.PPL_MULTI_STRING, mapper.toPpl(Person.MULTI_INSTANCE))
        assertEquals(StaticPerson.PPL_STRING, mapper.toPpl(StaticPerson.INSTANCE))
        assertEquals(Human.PPL_STRING, mapper.toPpl(Human.INSTANCE))
        assertEquals(Xmen.PPL_STRING, mapper.toPpl(Xmen.INSTANCE))
        assertEquals(Order.PPL_STRING, mapper.toPpl(Order.INSTANCE))
    }

    @Test
    fun testCustomizeDialect() {
        val mapper: GenericPplMapper = pplMapperOf(dialect = Dialect.VERBOSE) as GenericPplMapper
        assertEquals(Dialect.VERBOSE, mapper.dialect)
    }

}