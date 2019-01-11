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

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * Represents the element able to convert value to text (toText) and text to value (toValue).
 * This capability also allows to determinate the serial size of a given value (getValueSize).
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
interface ValueMapper {

    fun getValueSize(value: Any?, metaInfo: MetaInfo): Int

    fun toValue(text: String, metaInfo: MetaInfo): Any?

    fun toText(value: Any, metaInfo: MetaInfo): String

    @Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
    @Retention(RetentionPolicy.RUNTIME)
    annotation class MappedBy(val mapper: KClass<out ValueMapper>)

}