package br.net.buzu.model

interface TypeAdapter {

    fun getFieldValue(parentObject: Any): Any?

    fun setFieldValue(parentObject: Any, paramValue: Any?)

}