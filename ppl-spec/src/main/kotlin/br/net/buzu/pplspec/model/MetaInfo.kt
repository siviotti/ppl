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
package br.net.buzu.pplspec.model

import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.lang.*

/**
 * Basic POJO resulting of annotations information or manual setting. This class
 * represents the PPL elements: name, type, size, scale, occurs , defaultValue,
 * domain, tags, key, indexed, align, fillChar, nullChar
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class MetaInfo @JvmOverloads
constructor(val index: Int, val name: String, val subtype: Subtype, size: Int, scale: Int, minOccurs: Int, val maxOccurs: Int,
            val domain: Domain = Domain.EMPTY, val defaultValue: String = EMPTY, val tags: String = EMPTY) : Comparable<MetaInfo> {
    // Basic
    val size: Int = if (subtype.isFixedSizeType) subtype.fixedSize() else size
    val scale: Int = if (scale > NO_SCALE) scale else 0
    val minOccurs: Int
    // Extension
    val isExtended: Boolean
    val align: Align
    val fillChar: Char
    val nullChar: Char

    // API

    /**
     * A complete MetaInfo must have SIZE (size > -1) and MAX_OCCURS (maxOccurs >
     * -1).
     *
     * @return `true` if the MetaInfo is complete (has size and
     * maxOccurs) or `false` is is incomplete, has no size and/or
     * no maxOccurs.
     */
    val isComplete: Boolean
        get() = hasSize() && hasMaxOccurs()

    /**
     * A static MetaInfo must be 'Complete' and not 'Unbounded'.
     *
     * @return `true` if the MetaInfo is static (has size and maxOccurs
     * but not Unbounded) or `false` if is not static.
     */
    val isStatic: Boolean
        get() = isComplete && !isUnbounded

    /**
     * Returns if the Metainfo has no maxOccurs limit (maxOccurs == 0).
     *
     * @return `true` if maxOccurs is equals zero`false` if
     * maxOccurs has a value and this is the limit.
     */
    val isUnbounded: Boolean
        get() = UNBOUNDED == maxOccurs

    val isMultiple: Boolean
        get() = maxOccurs != 1

    /**
     * Indicates if the MetaInfo has 'minOccurs' bigger than 0. It means is
     * required.
     *
     * @return `true` if is required or `false`otherwise.
     */
    val isRequired
        get() = minOccurs > 0

    constructor(pplMetadata: PplMetadata, originalFieldName: String, originalSubtype: Subtype)
            : this(pplMetadata.index, getName(pplMetadata, originalFieldName),
            getSubtype(pplMetadata, originalSubtype), pplMetadata.size, pplMetadata.scale,
            pplMetadata.minOccurs, pplMetadata.maxOccurs, domainOf(*pplMetadata.domain),
            pplMetadata.defaultValue, buildTags(pplMetadata))

    init {
        checkOccurs(minOccurs, maxOccurs)
        this.minOccurs = if (minOccurs < 0) 0 else minOccurs
        this.isExtended = isExtended(this.domain, this.defaultValue, this.tags)
        if (tags.isNotEmpty()) {
            this.align = getAlign(subtype, tags)
            this.fillChar = getTagChar(FILL_CHAR, subtype.dataType.fillChar, tags)
            this.nullChar = getTagChar(NULL_CHAR, subtype.dataType.nullChar, tags)
        } else {
            this.align = subtype.dataType.group.align
            this.fillChar = subtype.dataType.fillChar
            this.nullChar = subtype.dataType.nullChar
        }
    }

    fun hasSize(): Boolean = PplMetadata.EMPTY_INTEGER != size

    fun hasMaxOccurs(): Boolean = PplMetadata.EMPTY_INTEGER != maxOccurs

    fun hasScale(): Boolean = scale > NO_SCALE

    fun update(newSize: Int, newMaxOccurs: Int): MetaInfo {
        return MetaInfo(index, name, subtype, newSize, scale, minOccurs, newMaxOccurs, domain,
                defaultValue, tags)
    }

    fun hasDomain(): Boolean = !domain.isEmpty

    fun hasDefaultValue(): Boolean = defaultValue != null && !defaultValue.isEmpty()

    fun hasTags(): Boolean = tags != null && !tags.isEmpty()

    fun inDomain(valueItem: String?): Boolean {
        // Domain not defined
        if (domain.isEmpty) return true
        return if (valueItem == null) {
            // Domain defined and invalid item
            false
        } else domain.containsValue(valueItem)
    }

    fun isTagPresent(tag: String): Boolean = tags.contains(tag)

    fun isTagPresent(tag: Char): Boolean = tags.contains(tag)

    fun isKey() = isTagPresent(KEY)

    fun isIndexd() = isTagPresent(INDEXED)

    fun ppl(): String = (name + NAME_END + subtype.id + size + OCCURS_BEGIN + minOccurs + OCCURS_RANGE
            + maxOccurs + domain.asSerial() + serializeDefaultvalue(defaultValue) + tags)

    // Common Methods

    override fun compareTo(other: MetaInfo): Int {
        if (index == other.index) {
            return 0
        }
        return if (index < other.index) -1 else 1
    }

    override fun toString(): String {
        return "[" + index + "] " + ppl()
    }

    override fun hashCode(): Int {
        return ppl().hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) return true
        if (obj == null) return false
        return if (this.javaClass == obj.javaClass) {
            ppl() == (obj as MetaInfo).ppl()
        } else false
    }

    companion object {

        const val NO_SCALE = 0

        private fun checkOccurs(minOccurs: Int, maxOccurs: Int) {
            if (maxOccurs > 0 && minOccurs > maxOccurs) {
                throw IllegalArgumentException("minOccurs cannot be bigger than maxOccurs:$minOccurs>$maxOccurs")
            }
        }

        private fun getSubtype(pplMetadata: PplMetadata, originalSubtype: Subtype): Subtype {
            return if (Subtype.EMPTY_SUBTYPE == pplMetadata.subtype) originalSubtype else pplMetadata.subtype
        }

        private fun getName(pplMetadata: PplMetadata, originalFieldName: String): String {
            return if (PplMetadata.EMPTY_NAME == pplMetadata.name) originalFieldName else pplMetadata.name
        }

        private fun buildTags(pplMetadata: PplMetadata): String {
            val tags = if (pplMetadata.tags.isNotEmpty()) pplMetadata.tags else return EMPTY
            val sb = StringBuilder(tags)
            if (pplMetadata.key && tags.indexOf(KEY) < 0) {
                sb.append(KEY)
            }
            if (pplMetadata.indexed && tags.indexOf(INDEXED) < 0) {
                sb.append(INDEXED)
            }
            if (PplMetadata.EMPTY_CHAR != pplMetadata.align) {
                sb.append(pplMetadata.align)
            }
            if (PplMetadata.EMPTY_CHAR != pplMetadata.fillChar) {
                sb.append(FILL_CHAR).append(pplMetadata.fillChar)
            }
            if (PplMetadata.EMPTY_CHAR != pplMetadata.nullChar) {
                sb.append(NULL_CHAR).append(pplMetadata.nullChar)
            }
            return sb.toString()
        }

        private fun serializeDefaultvalue(defaultValue: String): String {
            return if (defaultValue.trim { it <= ' ' }.isEmpty()) {
                EMPTY
            } else "" + DEFAULT_VALUE + VALUE_BEGIN + defaultValue + VALUE_END
        }

        private fun isExtended(domain: Domain, defaultValue: String, tags: String): Boolean {
            return !domain.isEmpty || !defaultValue.isEmpty() || !tags.isEmpty()
        }

        private fun getAlign(subtype: Subtype, tags: String): Align {
            return if (tags.indexOf(LEFT) > -1) Align.LEFT else {
                if (tags.indexOf(RIGHT) > -1) Align.RIGHT else subtype.dataType.align
            }
        }

        private fun getTagChar(tagChar: Char, defaulChar: Char, tags: String): Char {
            val index = tags.indexOf(tagChar)
            return if (index > -1 && index <= tags.length - 2) tags[index + 1] else defaulChar
        }
    }
}
