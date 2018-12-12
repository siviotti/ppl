package br.net.buzu.sample.enums

import br.net.buzu.pplspec.model.PplSerializable

enum class Gender private constructor(val code: Char, val description: String) : PplSerializable {

    MALE('M', "Male"), FEMALE('F', "Female");

    override fun asPplSerial(): String {
        return "" + code
    }

}
