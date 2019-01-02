package br.net.buzu.model

interface TypeMapper {

    fun parse(text: String): Any?

    fun serialize(value: Any): String

}