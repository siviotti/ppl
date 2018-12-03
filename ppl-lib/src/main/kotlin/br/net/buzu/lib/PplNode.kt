package br.net.buzu.lib

import br.net.buzu.pplspec.lang.EMPTY;

data class PplNode(val name: String = EMPTY, val type: String= EMPTY, val size: String= EMPTY, val occurs: String= EMPTY,
                   val domain: String= EMPTY, val defaultValue: String= EMPTY, val tags: String= EMPTY,
                   val children: Array<PplNode> = arrayOf()) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PplNode

        if (name != other.name) return false
        if (type != other.type) return false
        if (size != other.size) return false
        if (occurs != other.occurs) return false
        if (domain != other.domain) return false
        if (defaultValue != other.defaultValue) return false
        if (tags != other.tags) return false
        if (!children.contentEquals(other.children)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + occurs.hashCode()
        result = 31 * result + domain.hashCode()
        result = 31 * result + defaultValue.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + children.contentHashCode()
        return result
    }
}




