package br.net.buzu.sample.enums

import br.net.buzu.java.model.PplSerializable

enum class Gender(val code: Char, val description: String) : PplSerializable {

    MALE('M', "Male"), FEMALE('F', "Female");

    override fun asPplSerial(): String {
        return "" + code
    }

}
