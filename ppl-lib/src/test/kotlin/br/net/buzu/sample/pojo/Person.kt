package br.net.buzu.sample.pojo

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


}
