package br.net.buzu.sample.ppl

import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.model.Subtype

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

    constructor() {

    }

    constructor(name: String, age: Int, city: String) : super() {
        this.name = name
        this.age = age
        this.city = city
    }

    override fun toString(): String {
        return "Person [name=$name, age=$age, city=$city]"
    }


}