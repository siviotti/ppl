package br.net.buzu.lib

import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.StaticStructure

internal val META_INFO_MUST_BE_COMPLETE = "MetaInfo must be complete (size and occurrences) for static behave:"
internal val META_INFO_CANNOT_BE_UNBOUNDED = "MetaInfo cannot be Unbounded (no limit) for static behave:"
internal val INVALID_STATIC_CHILD = "Invalid Static child:"


fun calcSimpleSerialMaxSize(metaInfo: MetaInfo): Int = metaInfo.size * metaInfo.maxOccurs

fun calcComplexSerialMaxSize(metaInfo: MetaInfo, children: List<Metadata>): Int {
    var tmp = 0
    for (child in children) {
        checkStaticChild(child)
        tmp += (child as StaticStructure).serialMaxSize()
    }
    return tmp * metaInfo.maxOccurs

}

fun checkStaticInfo(metaInfo: MetaInfo): MetaInfo {
    if (!metaInfo.isComplete) {
        throw IllegalArgumentException(META_INFO_MUST_BE_COMPLETE + metaInfo)
    }
    if (metaInfo.isUnbounded) {
        throw IllegalArgumentException(META_INFO_CANNOT_BE_UNBOUNDED + metaInfo)
    }
    return metaInfo
}


fun checkStaticChild(child: Metadata) {
    if (child !is StaticStructure) {
        throw IllegalArgumentException(
                INVALID_STATIC_CHILD + child.info().id + " is not a " + StaticStructure::class.java.simpleName)
    }
}

fun isStaticChidren(children: List<Metadata>): Boolean {
    for (child in children) {
        if (!child.isStatic()) {
            return false
        }
    }
    return true
}

fun complexSize(children: List<Metadata>): Int {
    var complexSize = 0
    for (child in children) {
        complexSize += child.info().size
    }
    return complexSize
}