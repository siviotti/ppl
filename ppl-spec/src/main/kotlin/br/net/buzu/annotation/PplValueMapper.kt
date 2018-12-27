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
package br.net.buzu.annotation

import br.net.buzu.model.ValueMapper
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass

/**
 * Indicates that a given "ValueMapper" must be used over the default
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(RetentionPolicy.RUNTIME)
annotation class PplValueMapper(val value: KClass<out ValueMapper>)


