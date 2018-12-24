package br.net.buzu.java.model

interface TypeAdapter<T> {

    fun getValueSize(value: T?): Int

    fun stringToValue(positionalText: String, metaInfo: MetaInfo): T?

    fun valueToString(value: T): String

}