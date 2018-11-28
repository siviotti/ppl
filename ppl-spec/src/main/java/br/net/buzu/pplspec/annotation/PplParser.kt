/*
 *	This file is part domainOf Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms domainOf the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 domainOf the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy domainOf the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.pplspec.annotation

import br.net.buzu.pplspec.api.PayloadParser
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * An annotation that indicates this member should be parsed/serialized with a
 * givem Parser.
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RetentionPolicy.RUNTIME)
annotation class PplParser(val value: KClass<out PayloadParser>)
