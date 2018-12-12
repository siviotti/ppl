package br.net.buzu.sample.enums

class Animal {

    var name: String? = null
    var species: Species? = null
    var gender: Gender? = null

    constructor() {

    }

    constructor(name: String, species: Species, gender: Gender) : super() {
        this.name = name
        this.species = species
        this.gender = gender
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (gender == null) 0 else gender!!.hashCode()
        result = prime * result + if (name == null) 0 else name!!.hashCode()
        result = prime * result + if (species == null) 0 else species!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (obj !is Animal) {
            return false
        }
        val other = obj as Animal?
        if (gender != other!!.gender) {
            return false
        }
        if (name == null) {
            if (other.name != null) {
                return false
            }
        } else if (name != other.name) {
            return false
        }
        return species == other.species
    }

    override fun toString(): String {
        return species.toString() + "(" + gender!!.code + ") " + name
    }

}
