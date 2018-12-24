package br.net.buzu.model

interface AtomicMapper {


    fun parse(text: String): Any

    fun serialize(value: Any): String

    fun getValueSize(value: Any): Int

}