package br.net.buzu.sample.ppl

import br.net.buzu.pplspec.annotation.PplMetadata

import java.time.LocalDate

/**
 * Extension sample.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class Xmen : Human() {

    @PplMetadata(index = 4, name = "power", size = 20)
    var skill: String? = null

    companion object {

        private val serialVersionUID = 1L
        val PPL_STRING = "(fullName:C10;fixed-field:C5;weight:N5;birthDay:DI;power:S20)Wolverine fix  099.01882-04-05healing             "
        val INSTANCE: Xmen

        init {
            INSTANCE = Xmen()
            INSTANCE.name = "Wolverine "
            INSTANCE.birth = LocalDate.of(1882, 4, 5)
            INSTANCE.weight = 99.0
            INSTANCE.skill = "healing"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Xmen

        if (name != other.name) return false
        if (birth != other.birth) return false
        if (weight != other.weight) return false
        if (skill != other.skill) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (skill?.hashCode() ?: 0)
        return result
    }

}
