package br.net.buzu.model

interface TypeAdapter {

    fun getValueSize(value: Any?): Int

    fun stringToValue(positionalText: String, metaInfo: MetaInfo): Any?

    fun valueToString(value: Any): String

}