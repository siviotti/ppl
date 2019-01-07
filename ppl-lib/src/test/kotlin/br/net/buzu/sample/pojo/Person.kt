package br.net.buzu.sample.pojo

import br.net.buzu.lang.METADATA_END
import br.net.buzu.lang.NAME_END
import br.net.buzu.sample.ppl.StaticPerson



/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class Person {

    var name: String? = null
    var age: Int? = null
    var city: String? = null

    constructor()

    constructor(name: String, age: Int, city: String) : super() {
        this.name = name
        this.age = age
        this.city = city
    }

    override fun toString(): String {
        return "Person [name=$name, age=$age, city=$city]"
    }

    companion object {
        private val PERSON_METADATA_NAMES = arrayOf("name", "age", "city")
        val PERSON_METADATA_TYPES = arrayOf("S", "I", "S")
        val PERSON_NAME = "Ladybug"
        val PERSON_AGE = 15
        val PERSON_CITY = "Paris"

        val PERSON_METADATA_SIZES = intArrayOf(PERSON_NAME.length, PERSON_AGE.toString().length, PERSON_CITY.length)
        // metadata = (name:S7;age:I2;city:S5)
        val PERSON_METADATA = ("(" + PERSON_METADATA_NAMES[0] + NAME_END
                + PERSON_METADATA_TYPES[0] + PERSON_METADATA_SIZES[0] + METADATA_END + PERSON_METADATA_NAMES[1]
                + NAME_END + PERSON_METADATA_TYPES[1] + PERSON_METADATA_SIZES[1] + METADATA_END
                + PERSON_METADATA_NAMES[2] + NAME_END + PERSON_METADATA_TYPES[2] + PERSON_METADATA_SIZES[2] + ")")
        val PERSON_PAYLOAD = "Ladybug15Paris"
        // PPL = (name:S7;age:I2;city:S5)Ladybug15Paris
        val PPL_STRING = PERSON_METADATA + PERSON_PAYLOAD

        val INSTANCE = Person(PERSON_NAME, PERSON_AGE, PERSON_CITY)


    }
}
