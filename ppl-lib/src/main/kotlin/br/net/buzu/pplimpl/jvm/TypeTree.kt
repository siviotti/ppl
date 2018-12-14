package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplspec.exception.PplException
import br.net.buzu.pplspec.model.PplSerializable
import br.net.buzu.pplspec.model.SizeType
import br.net.buzu.pplspec.model.Subtype
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal


class LoadNode(originalValue: Any?, val typeInfo: TypeInfo, val fieldPath: String) {

    val value: Array<Any?>
    val subtype: Subtype =typeInfo.metaInfo.subtype
    val occurs: Int
        get() = value.size

    init {
        if (originalValue == null) {
            this.value = arrayOf(1)
        } else if (typeInfo.isCollection) {
            if ((originalValue as Collection<*>).isEmpty()) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue.toTypedArray()
            }
        } else if (typeInfo.isArray) {
            if ((originalValue as Array<Any>).size == 0) {
                this.value = arrayOf(1)
            } else {
                this.value = originalValue as Array<Any?>
            }
        } else {
            this.value = arrayOf(1)
            this.value[0] = originalValue
        }
    }


    fun calcMaxSize(): Int {
        if (subtype.dataType().sizeType() == SizeType.CUSTOM) {
            var max = 0
            var tmp = 0
            for (obj in value) {
                tmp = typeInfo.getValueSize(obj!!)
                if (tmp > max) {
                    max = tmp
                }
            }
            return max
        }
        return subtype.fixedSize()
    }

}


data class Max(var maxSize: Int = 0, var maxOccurs: Int = 0) {

    fun tryNewMaxOccurs(newValue: Int): Max {
        if (newValue > maxOccurs) {
            maxOccurs = newValue
        }
        return this
    }

    fun tryNewMaxSize(newValue: Int): Max {
        if (newValue > maxSize) {
            maxSize = newValue
        }
        return this
    }

}

class MaxMap (val size: Int){
    private val map = Array(size, {Max()})

    fun getMaxByIndex(index: Int): Max {
        return map[index]
    }
}

