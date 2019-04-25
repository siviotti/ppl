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
package br.net.buzu.pplimpl.api;

import br.net.buzu.pplspec.api.PplMapper;
import br.net.buzu.pplspec.api.PplSimpleMapper;
import br.net.buzu.pplspec.model.Dialect;
import br.net.buzu.sample.order.Order;
import br.net.buzu.sample.pojo.Person;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static br.net.buzu.pplimpl.api.PplMappingKt.*;

public class JavaTest {

    @Test
    public void testSimpleMapper() {
        PplSimpleMapper mapper = PplMappingKt.pplSimpleMapper();

        String name = "Ladybug";
        int age = 15;
        String city = "Paris";
        Person person = new Person(name, age, city);

        assertEquals(person, mapper.fromPpl("(name:S7;age:I2;city:S5)Ladybug15Paris", Person.class));
        assertEquals("(name:S7;age:I2;city:S5)Ladybug15Paris", mapper.toPpl(person));

        assertEquals(null, mapper.fromPpl("()", Object.class));
        assertEquals(Person.Companion.getINSTANCE(), mapper.fromPpl(Person.Companion.getPPL_STRING(), Person.class));
        assertEquals(Person.Companion.getPPL_STRING(), mapper.toPpl(Person.Companion.getINSTANCE()));
        assertEquals(Order.Companion.getINSTANCE(), mapper.fromPpl(Order.Companion.getPPL_STRING(), Order.class));
        assertEquals(Order.Companion.getPPL_STRING(), mapper.toPpl(Order.Companion.getINSTANCE()));
    }

    @Test
    public void testGenericMapper() {
        PplMapper mapper = PplMappingKt.pplMapper();

        String name = "Ladybug";
        int age = 15;
        String city = "Paris";
        Person person = new Person(name, age, city);

        assertEquals(person, mapper.fromPpl("(name:S7;age:I2;city:S5)Ladybug15Paris", Person.class));
        assertEquals("(name:S7;age:I2;city:S5)Ladybug15Paris", mapper.toPpl(person));

        assertEquals(null, mapper.fromPpl("()", Object.class));
        assertEquals(Person.Companion.getINSTANCE(), mapper.fromPpl(Person.Companion.getPPL_STRING(), Person.class));
        assertEquals(Person.Companion.getPPL_STRING(), mapper.toPpl(Person.Companion.getINSTANCE()));
        assertEquals(Order.Companion.getINSTANCE(), mapper.fromPpl(Order.Companion.getPPL_STRING(), Order.class));
        assertEquals(Order.Companion.getPPL_STRING(), mapper.toPpl(Order.Companion.getINSTANCE()));
    }

    @Test
    public void testFactoryMethod() {
        PplMapper mapper1 = pplMapperOf();
        assertEquals(GenericPplMapper.class, mapper1.getClass());

        GenericPplMapper mapper2 = (GenericPplMapper) pplMapperOf();
        assertEquals(Dialect.Companion.getDEFAULT(), mapper2.getDialect());
    }

}
