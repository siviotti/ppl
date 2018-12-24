package br.net.buzu.sample.ppl

import br.net.buzu.annotation.PplMetadata
import br.net.buzu.annotation.PplUse
import br.net.buzu.model.Subtype

import java.io.Serializable
import java.time.LocalDate

/**
 * Plain annotation sample.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
open class Human : Serializable {

    @PplMetadata(name = "fullName", size = 10, subtype = Subtype.CHAR)
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

    companion object {

        private const val serialVersionUID = 1L
        internal val IGNORED_FIELD = "ignore"

        @PplUse
        @PplMetadata(name = "fixed-field", size = 5, subtype = Subtype.CHAR)
        var fixeD_FIELD = "fix"
            set(fIXED_FIELD) {}
    }

}
