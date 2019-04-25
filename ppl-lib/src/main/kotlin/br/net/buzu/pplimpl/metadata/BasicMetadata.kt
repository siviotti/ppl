/*
 *	This file is part of DefaultPplMapper.
 *
 *   DefaultPplMapper is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DefaultPplMapper is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with DefaultPplMapper.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.lang.PATH_SEP
import br.net.buzu.pplspec.model.Kind
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.kindOf

/**
 * Most basic abstract implementation of Metadata.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
abstract class BasicMetadata(private val metaInfo: MetaInfo) : Metadata {

    private val kind: Kind = kindOf(metaInfo.isMultiple, metaInfo.subtype.dataType.isComplex)

    override fun name(): String = metaInfo.name

    override fun kind(): Kind = kind

    override fun info(): MetaInfo = metaInfo

    override fun toTree(level: Int): String {
        val sb = StringBuilder()
        for (i in 0 until level) {
            sb.append(PATH_SEP)
        }
        sb.append(toString()).append("\n")
        if (hasChildren()) {
            for (child in children<Metadata>()) {
                sb.append(child.toTree(level + 1))
            }
        }
        return sb.toString()
    }

    override fun hashCode(): Int = kind.ordinal * 31 + metaInfo.hashCode() * 31

    override fun equals(obj: Any?): Boolean {
        if (obj === this) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (obj is Metadata) {
            val other = obj as Metadata?
            return kind == other!!.kind() && metaInfo == other.info()
        }
        return false
    }

    override fun toString(): String = kind.toString() + " " + metaInfo.ppl()

    companion object {
        private const val META_INFO_MUST_BE_COMPLETE = "MetaInfo must be complete (size and occurrences) for static behave:"
        private const val META_INFO_CANNOT_BE_UNBOUNDED = "MetaInfo cannot be Unbounded (no limit) for static behave:"

        internal fun checkStaticInfo(metaInfo: MetaInfo): MetaInfo {
            if (!metaInfo.isComplete) {
                throw IllegalArgumentException(META_INFO_MUST_BE_COMPLETE + metaInfo)
            }
            if (metaInfo.isUnbounded) {
                throw IllegalArgumentException(META_INFO_CANNOT_BE_UNBOUNDED + metaInfo)
            }
            return metaInfo
        }
    }
}
