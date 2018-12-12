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
package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplspec.annotation.PplIgnore
import br.net.buzu.pplspec.annotation.PplUse
import br.net.buzu.pplspec.api.SkipStrategy
import br.net.buzu.pplspec.exception.PplReflectionException
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Indicates if a field must be skipped
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
class BasicSkipStrategy(private val ignoreAnnotation: Class<out Annotation>, private val useAnnotation: Class<out Annotation>,
                        private val next: SkipStrategy?) : SkipStrategy {

    @JvmOverloads
    constructor(next: SkipStrategy? = null) : this(PplIgnore::class.java, PplUse::class.java, next) {
    }


    /**
     * Indicates if the field must be skipped
     *
     * @param field
     * @return
     */
    override fun skip(field: Field): Boolean {
        // Precedence 1: Field explict ignore
        if (field.isAnnotationPresent(ignoreAnnotation)) {
            return true
        }

        // Precedence 2: Field explicit use
        if (field.isAnnotationPresent(useAnnotation)) {
            return false
        }

        // Precedence 3: Static, Transient or Ignore
        if (Modifier.isStatic(field.modifiers)) {
            return true
        }
        if (Modifier.isTransient(field.modifiers)) {
            return true
        }

        // Precedence 4: Field Class
        var fieldClass: Class<*>
        try {
            fieldClass = getElementType(field)
        } catch (pre: PplReflectionException) {
            fieldClass = field.javaClass
        }

        return if (fieldClass.isAnnotationPresent(ignoreAnnotation)) {
            true
        } else next != null && next.skip(field)

    }

}
