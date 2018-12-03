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
@file:JvmName("Domains")

package br.net.buzu.pplspec.model

import br.net.buzu.pplspec.lang.EMPTY
import br.net.buzu.pplspec.lang.LABEL_VALUE
import java.util.ArrayList

fun domainOf(vararg array: String): Domain {
    return Domain.create(EMPTY, domainItemListOf(*array))
}

fun createDomain(name: String, items:List<DomainItem>): Domain {
    return Domain.create(EMPTY, items)
}

fun toDomainItemList(stringList: List<String>): List<DomainItem> {
    val list = ArrayList<DomainItem>()
    for (item in stringList) {
        list.add(domainItemOf(item))
    }
    return list
}

fun domainItemListOf(vararg array: String): List<DomainItem> {
    val list = ArrayList<DomainItem>()
    for (i in array.indices) {
        list.add(domainItemOf(array[i]))
    }
    return list
}

fun domainItemOf(text: String): DomainItem {
    val pos = text.indexOf(LABEL_VALUE)
    if (pos < 0) {
        return DomainItem(text)
    }
    val value = text.substring(0, pos)
    val label = text.substring(pos + 1, text.length)
    return DomainItem(value, label)
}