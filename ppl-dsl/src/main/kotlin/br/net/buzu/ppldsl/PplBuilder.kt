package br.net.buzu.ppldsl

import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.PplNode

class PplBuilder{

    var nodes: List<PplNode> = mutableListOf()

    fun asMetadata( ): Metadata?{
        println(nodes)
        return null
    }
}

fun ppl(initializer: PplBuilder.() -> Unit): PplBuilder {
    return PplBuilder().apply(initializer)
}