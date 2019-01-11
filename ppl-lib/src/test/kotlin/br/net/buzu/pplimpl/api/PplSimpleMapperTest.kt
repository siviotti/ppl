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

import br.net.buzu.pplspec.api.PplSimpleMapper
import br.net.buzu.pplspec.model.PplString
import br.net.buzu.pplspec.model.pplStringOf
import br.net.buzu.sample.order.Order
import br.net.buzu.sample.pojo.Person
import br.net.buzu.sample.ppl.Human
import br.net.buzu.sample.ppl.StaticPerson
import br.net.buzu.sample.ppl.Xmen
import br.net.buzu.sample.time.TimePojo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


internal class PplSimpleMapperTest {

    private val mapper: PplSimpleMapper = pplSimpleMapper()

    @Test
    fun testShouldParseUsingFromPpl() {
        assertEquals(null, mapper.fromPpl("()", Any::class.java))
        assertEquals(Person.INSTANCE, mapper.fromPpl(Person.PPL_STRING, Person::class.java))
        assertEquals(Person.MULTI_INSTANCE, mapper.fromPpl(Person.PPL_MULTI_STRING, Person::class.java))
        assertEquals(StaticPerson.INSTANCE, mapper.fromPpl(StaticPerson.PPL_STRING, StaticPerson::class.java))
        assertEquals(Human.INSTANCE, mapper.fromPpl(Human.PPL_STRING, Human::class.java))
        assertEquals(Xmen.INSTANCE, mapper.fromPpl(Xmen.PPL_STRING, Xmen::class.java))
        assertEquals(Order.INSTANCE, mapper.fromPpl(Order.PPL_STRING, Order::class.java))
    }

    @Test
    fun testShouldSerializeUsingToPpl() {
        assertEquals("(S0)", mapper.toPpl(""))
        assertEquals(Person.PPL_STRING, mapper.toPpl(Person.INSTANCE))
        assertEquals(Person.PPL_MULTI_STRING, mapper.toPpl(Person.MULTI_INSTANCE))
        assertEquals(StaticPerson.PPL_STRING, mapper.toPpl(StaticPerson.INSTANCE))
        assertEquals(Human.PPL_STRING, mapper.toPpl(Human.INSTANCE))
        assertEquals(Xmen.PPL_STRING, mapper.toPpl(Xmen.INSTANCE))
        assertEquals(Order.PPL_STRING, mapper.toPpl(Order.INSTANCE))
    }

    @Test
    fun testPersonFromPpl() {
        val person = mapper.fromPpl(Person.PPL_STRING, Person::class.java) as Person
        assertEquals(Person.NAME, person.name)
        assertEquals(Person.AGE, person.age)
        assertEquals(Person.CITY, person.city)
    }

    // ********** Time fields **********

    @Test
    fun testTimeFields() {
        val timePojo = TimePojo()
        timePojo.localDate = LOCAL_DATE
        timePojo.localTime = LOCAL_TIME
        timePojo.localDateTime = LOCAL_DATE_TIME
        timePojo.date = OLD_DATE

        val ppl = mapper.toPpl(timePojo)

        assertTrue(ppl.contains("localDate:D"))
        assertTrue(ppl.contains("localTime:t"))
        assertTrue(ppl.contains("localDateTime:T"))
        assertTrue(ppl.contains("date:T"))
    }

    // ********** Collections **********
    @Test
    fun testEmptyCollections() {
        assertEquals(PplString.EMPTY.metadata, mapper.toPpl(ArrayList<Any>()))
        assertEquals(PplString.EMPTY.metadata, mapper.toPpl(listOf<Any>()))
        assertEquals(PplString.EMPTY.metadata, mapper.toPpl(mutableListOf<Any>()))
        assertEquals(PplString.EMPTY.metadata, mapper.toPpl(hashSetOf<Any>()))
    }

    @Test
    fun testPersonCollections() {
        val people = ArrayList<Person>()
        people.add(Person.INSTANCE)
        people.add(Person.INSTANCE)
        people.add(Person.INSTANCE)
        val ppl = mapper.toPpl(people)
        val pplString = pplStringOf(Person.PPL_STRING)
        assertTrue(ppl.contains(pplString.metadata)) // name:S7;age:I2;city:S5
        assertTrue(ppl.contains("#0-" + people.size)) // #0-3
    }

    @Test
    fun testUnsafeCollection() {
        val people = ArrayList<Person>()
        people.add(Person.INSTANCE)
        people.add(Person("Catnoir", 16, "London"))
        var ppl = mapper.toPpl(people)

        // 2 Records = List<Person>
        var o = mapper.fromPpl(ppl, Person::class.java)// 2 Records
        assertTrue(o is Collection<*>) // 2 items
        val list = o as List<Person> // Is Collection
        assertEquals(2, list.size)

        // 1 Record = Single Person Object
        people.removeAt(1)
        ppl = mapper.toPpl(people)
        o = mapper.fromPpl(ppl, Person::class.java) // 1 Record
        assertFalse(o is Collection<*>)// Is not Collection
        //assertTrue(o is Person)
        assertEquals(Person.INSTANCE.name, (o as Person).name)
    }

    @Test
    fun testExplictCollection() {
        // Create a PPL text with 2 Records
        val people = ArrayList<Person>()
        people.add(Person.INSTANCE)
        people.add(Person("Catnoir", 16, "London"))
        var ppl = mapper.toPpl(people)

        // Explicit collection parseMetadata - List.size = 2
        var list = mapper.fromPpl(ppl, Person::class.java) as List<*>
        assertEquals(2, list.size)

        // Explicit collection parseMetadata - List.size = 1
        people.removeAt(1)
        ppl = mapper.toPpl(people)
        val singlePerson = mapper.fromPpl(ppl, Person::class.java) as Person
        assertEquals(Person::class.java, singlePerson.javaClass)
    }


    companion object {
        // *** TimePojo ***
        val YEAR = 2015
        val MONTH = 10
        val DAY = 21
        val HOUR = 15
        val MINUTE = 43
        val SECOND = 27

        val LOCAL_DATE = LocalDate.of(YEAR, MONTH, DAY)
        val LOCAL_TIME = LocalTime.of(HOUR, MINUTE, SECOND)
        val LOCAL_DATE_TIME = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND)
        val OLD_DATE = GregorianCalendar(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND).time
    }

}