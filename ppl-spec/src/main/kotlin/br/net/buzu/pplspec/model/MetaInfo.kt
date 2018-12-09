/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplspec.model

import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.lang.*
import java.util.*

    /**
     * Basic Pojo resulting of annotations information or manual setting. This class
     * represents the PPL elements: name, type, size, scale, occurs , defaultValue,
     * domain, tags, key and indexed.
     *
     * @author Douglas Siviotti
     * @since 1.0
     */
class MetaInfo
/**
 * @param index
 * @param name
 * @param subtype
 * The subtype based on Subtype enum. [CANNOT BE NULL]
 * @param size
 * @param scale
 * @param minOccurs
 * @param maxOccurs
 * @param defaultValue
 * @param domain
 * @param tags
 */
@JvmOverloads constructor(parentId: String, val index: Int, val name: String, subtype: Subtype, size: Int, scale: Int, minOccurs: Int,
                          val maxOccurs: Int, domain: Domain? = null, defaultValue: String? = EMPTY, tags: String? = EMPTY) : Comparable<MetaInfo> {

    // Basic
    val id: String
    val subtype: Subtype
    val size: Int
    val scale: Int
    val minOccurs: Int
    // Extension
    /**
     * Returns if the MetaInfo has domain, defaultValue or some tag defined.
     *
     * @return `true` if use any extension property or `false`
     * if has no extension.
     */
    val isExtended: Boolean
    val domain: Domain
    val defaultValue: String?
    val tags: String?
    val align: Align
    val fillChar: Char

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
    val isRequired: Boolean
        get() = minOccurs > 0

    constructor(parentId: String, pplMetadata: PplMetadata, originalFieldName: String, originalSubtype: Subtype) : this(parentId, pplMetadata.index, getName(pplMetadata, originalFieldName),
            getSubtype(pplMetadata, originalSubtype), pplMetadata.size, pplMetadata.scale,
            pplMetadata.minOccurs, pplMetadata.maxOccurs, domainOf(*pplMetadata.domain),
            pplMetadata.defaultValue, buildTags(pplMetadata))

    init {
        this.id = createId(parentId, name)
        this.subtype = Objects.requireNonNull(subtype, "'subtype' cannot be null")
        this.size = if (subtype.isFixedSizeType) subtype.fixedSize() else size
        this.scale = if (scale > NO_SCALE) scale else 0
        checkOccurs(minOccurs, maxOccurs)
        this.minOccurs = if (minOccurs < 0) 0 else minOccurs
        this.domain = domain ?: Domain.EMPTY
        this.defaultValue = defaultValue ?: EMPTY
        this.tags = tags ?: EMPTY
        this.isExtended = isExtended(this.domain, this.defaultValue, this.tags)
        this.align = getAlign(subtype, tags)
        this.fillChar = subtype.dataType().fillChar()
    }

    fun hasSize(): Boolean {
        return PplMetadata.EMPTY_INTEGER != size
    }

    fun hasMaxOccurs(): Boolean {
        return PplMetadata.EMPTY_INTEGER != maxOccurs
    }

    fun hasScale(): Boolean {
        return scale > NO_SCALE
    }

    fun update(newSize: Int, newMaxOccurs: Int): MetaInfo {
        return MetaInfo(parentId(), maxOccurs, name, subtype, newSize, scale, minOccurs, newMaxOccurs, domain,
                defaultValue, tags)
    }

    fun hasIndex(): Boolean {
        return PplMetadata.EMPTY_INTEGER != index
    }

    fun hasDomain(): Boolean {
        return !domain.isEmpty
    }

    fun hasDefaultValue(): Boolean {
        return defaultValue != null && !defaultValue.isEmpty()
    }

    fun hasTags(): Boolean {
        return tags != null && !tags.isEmpty()
    }

    fun inDomain(valueItem: String?): Boolean {
        if (domain.isEmpty) {
            // Domain not defined
            return true
        }
        return if (valueItem == null) {
            // Domain defined and invalid item
            false
        } else domain.containsValue(valueItem)
    }

    fun isTagPresent(tag: String?): Boolean {
        return tags != null && tag != null && tags.contains(tag)
    }

    fun parentId(): String {
        val pos = id.lastIndexOf(PATH_SEP)
        return if (pos > -1) id.substring(0, pos) else ""
    }

    fun ppl(): String {
        return (name + NAME_END + subtype.id + size + OCCURS_BEGIN + minOccurs + OCCURS_RANGE
                + maxOccurs + domain.asSerial() + serializeDefaultvalue(defaultValue) + tags)

    }

    // Common Methods

    override fun compareTo(o: MetaInfo): Int {
        if (index == o.index) {
            return 0
        }
        return if (index < o.index) -1 else 1
    }

    override fun toString(): String {
        return id + "[" + index + "] " + ppl()
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        return if (this.javaClass == obj.javaClass) {
            id == (obj as MetaInfo).id
        } else false

    }

    companion object {

        val NO_SCALE = 0

        // static

        private fun createId(parentId: String?, name: String?): String {
            return if (parentId == null || parentId.isEmpty()) {
                name ?: EMPTY
            } else parentId + PATH_SEP + name
        }

        private fun checkOccurs(minOccurs: Int, maxOccurs: Int) {
            if (maxOccurs > 0 && minOccurs > maxOccurs) {
                throw IllegalArgumentException(
                        "minOccurs cannot be bigger than maxOccurs:$minOccurs>$maxOccurs")
            }
        }

        private fun getSubtype(pplMetadata: PplMetadata, originalSubtype: Subtype): Subtype {
            return if (Subtype.EMPTY_SUBTYPE == pplMetadata.subtype) originalSubtype else pplMetadata.subtype
        }

        private fun getName(pplMetadata: PplMetadata, originalFieldName: String): String {
            return if (PplMetadata.EMPTY_NAME == pplMetadata.name) originalFieldName else pplMetadata.name
        }

        private fun buildTags(pplMetadata: PplMetadata): String {
            val tags = if (pplMetadata.tags != null) pplMetadata.tags else ""
            val sb = StringBuilder(tags)
            if (tags == null) {
                return sb.append(KEY).append(INDEXED).toString()
            }
            if (pplMetadata.key && tags.indexOf(KEY) < 0) {
                sb.append(KEY)
            }
            if (pplMetadata.indexed && tags.indexOf(INDEXED) < 0) {
                sb.append(INDEXED)
            }
            if (PplMetadata.EMPTY_CHAR != pplMetadata.align) {
                sb.append(pplMetadata.align)
            }

            return sb.toString()
        }

        private fun serializeDefaultvalue(defaultValue: String?): String {
            return if (defaultValue == null || defaultValue.trim { it <= ' ' }.isEmpty()) {
                EMPTY
            } else "" + DEFAULT_VALUE + VALUE_BEGIN + defaultValue + VALUE_END
        }

        private fun isExtended(domain: Domain, defaultValue: String, tags: String): Boolean {
            return !domain.isEmpty || !defaultValue.isEmpty() || !tags.isEmpty()
        }

        private fun getAlign(subtype: Subtype, tags: String?): Align {
            if (tags != null) {
                if (tags.indexOf(LEFT) > -1) {
                    return Align.LEFT
                }
                if (tags.indexOf(RIGHT) > -1) {
                    return Align.RIGHT
                }
            }
            return subtype.dataType().align()
        }
    }

}
