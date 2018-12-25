package br.net.buzu.model

interface ValueMapper {

    fun getValueSize(value: Any?): Int

    fun toValue(text: String, metaInfo: MetaInfo): Any?

    fun toText(value: Any): String

}