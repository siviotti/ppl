/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.lib

import br.net.buzu.java.model.MetaInfo
import br.net.buzu.java.model.Metadata
import br.net.buzu.java.model.StaticStructure

internal val META_INFO_MUST_BE_COMPLETE = "MetaInfo must be complete (size and occurrences) for static behave:"
internal val META_INFO_CANNOT_BE_UNBOUNDED = "MetaInfo cannot be Unbounded (no limit) for static behave:"
internal val INVALID_STATIC_CHILD = "Invalid Static child:"


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
                INVALID_STATIC_CHILD + child.info() + " is not a " + StaticStructure::class.java.simpleName)
    }
}

fun isStaticChildren(children: List<Metadata>): Boolean {
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