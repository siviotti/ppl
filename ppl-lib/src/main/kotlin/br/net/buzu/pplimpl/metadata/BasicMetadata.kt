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
package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.lang.*
import br.net.buzu.pplspec.model.Kind
import br.net.buzu.pplspec.model.MetaInfo
import br.net.buzu.pplspec.model.Metadata

import br.net.buzu.pplspec.model.kindOf

/**
 * Most basic abstract implementation domainOf `Metadata`.
 *
 * @author Douglas Siviotti
 * @since 1.0 (15/05/2017)
 */
abstract class BasicMetadata(private val metaInfo: MetaInfo) : Metadata {

    private val kind: Kind

    init {
        this.kind = kindOf(this.metaInfo.isMultiple, this.metaInfo.subtype.dataType().isComplex)
    }

    override fun name(): String {
        return metaInfo.name
    }

    override fun kind(): Kind {
        return kind
    }


    override fun info(): MetaInfo {
        return metaInfo
    }

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

    override fun toPlain(): String {
        val sb = StringBuilder()
        sb.append(info()).append("\n")
        if (hasChildren()) {
            children<Metadata>().forEach { c -> sb.append(c.toPlain()) }
        }
        return sb.toString()
    }

    // **************************************************
    // hashcode, equals, toString
    // **************************************************

    override fun hashCode(): Int {
        return kind.ordinal * 31 + metaInfo.hashCode() * 31
    }

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

    override fun toString(): String {
        return kind.toString() + " " + metaInfo.ppl()
    }

}
