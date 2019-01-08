package br.net.buzu.sample.ppl

import br.net.buzu.annotation.PplMetadata
import br.net.buzu.model.Subtype
import br.net.buzu.sample.pojo.Person

/**
 *
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@PplMetadata(maxOccurs = 1)
class StaticPerson {

    @PplMetadata(name = "personName", size = 10, maxOccurs = 1, subtype = Subtype.CHAR)
    var name: String? = null
    @PplMetadata(name = "personAge", size = 10, maxOccurs = 1, subtype = Subtype.INTEGER)
    var age: Int? = null
    @PplMetadata(name = "personCity", size = 10, maxOccurs = 1, subtype = Subtype.CHAR)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StaticPerson

        if (name != other.name) return false
        if (age != other.age) return false
        if (city != other.city) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (age ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        return result
    }


    companion object {
        const val PERSON_NAME = "Ladybug   "
        const val PERSON_AGE = 15
        const val PERSON_CITY = "Paris     "
        const val PPL_STRING = "(personName:C10;personAge:I10;personCity:C10)Ladybug   0000000015Paris     "
        val INSTANCE = StaticPerson(PERSON_NAME, PERSON_AGE, PERSON_CITY)
    }
}
