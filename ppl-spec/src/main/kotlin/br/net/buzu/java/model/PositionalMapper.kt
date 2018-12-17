package br.net.buzu.java.model

interface PositionalMapper{

    fun fromPosicional(positionalText: String): Any?

    fun toPosicional(value: Any?): String
}