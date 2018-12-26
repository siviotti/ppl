package br.net.buzu.model

interface ValueMapper {

    val subtype: Subtype

    fun getValueSize(value: Any?): Int

    fun toValue(text: String, metaInfo: MetaInfo): Any?

    fun toText(value: Any): String

}