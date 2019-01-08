package br.net.buzu.pplimpl.jvm

import br.net.buzu.model.MetaType
import br.net.buzu.model.Subtype
import br.net.buzu.sample.order.Order
import br.net.buzu.sample.pojo.Person
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MetaTypeReadTest {

    @Test
    fun testReadPerson() {
        val metaType=readMetaType(Person::class.java)
        println(metaType)
        assertMetaType(metaType, "", Subtype.OBJ, -1, 0, -1)
    }


    @Test
    fun testRead() {
        val metaType=readMetaType(Order::class.java)

    }

    fun assertMetaType(metaType: MetaType, name: String, type: Subtype, size: Int =0, minOccurs:Int=0, maxOccurs:Int=1){
        assertEquals(name, metaType.metaInfo.name)
        assertEquals(type, metaType.metaInfo.subtype)
        assertEquals(size, metaType.metaInfo.size)
        assertEquals(minOccurs, metaType.metaInfo.minOccurs)
        assertEquals(maxOccurs, metaType.metaInfo.maxOccurs)
    }

}