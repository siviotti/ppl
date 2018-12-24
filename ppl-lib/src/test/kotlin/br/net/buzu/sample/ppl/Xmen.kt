package br.net.buzu.sample.ppl

import br.net.buzu.annotation.PplMetadata

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

        val WOLVERINE: Xmen

        init {
            WOLVERINE = Xmen()
            WOLVERINE.name = "Wolverine"
            WOLVERINE.birth = LocalDate.of(1882, 4, 5)
            WOLVERINE.weight = 99.0
            WOLVERINE.skill = "healing"
        }
    }

}
