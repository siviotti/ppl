package br.net.buzu.sample.ppl

import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.annotation.PplUse
import br.net.buzu.pplspec.model.Subtype

import java.io.Serializable
import java.time.LocalDate

/**
 * Plain annotation sample.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
open class Human : Serializable {

    @PplMetadata(index = 0, name = "fullName", size = 10, subtype = Subtype.CHAR)
    var name: String? = null

    @PplMetadata(index = 3, name = "birthDay", subtype = Subtype.ISO_DATE)
    var birth: LocalDate? = null

    @PplMetadata(index = 2, size = 5)
    var weight: Double = 0.toDouble()

    constructor(name: String, birth: LocalDate, weight: Double) : super() {
        this.name = name
        this.birth = birth
        this.weight = weight
    }

    constructor()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Human

        if (name != other.name) return false
        if (birth != other.birth) return false
        if (weight != other.weight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (birth?.hashCode() ?: 0)
        result = 31 * result + weight.hashCode()
        return result
    }

    override fun toString(): String {
        return "Human(name=$name, birth=$birth, weight=$weight)"
    }

    companion object {
        val HUMAN_NAME = "Logan     "
        val HUMAN_BIRTH = LocalDate.of(1882, 4, 5)
        val HUMAN_WEIGHT = 99.9
        val PPL_STRING = "(fullName:C10;fixed-field:C5;weight:N5;birthDay:DI)Logan     fix  099.91882-04-05"

        val INSTANCE = Human(HUMAN_NAME, HUMAN_BIRTH, HUMAN_WEIGHT)
        private const val serialVersionUID = 1L
        internal val IGNORED_FIELD = "ignore"

        @PplUse
        @PplMetadata(index =1,name = "fixed-field", size = 5, subtype = Subtype.CHAR)
        var fixeD_FIELD = "fix"
            set(fIXED_FIELD) {}
    }

}
