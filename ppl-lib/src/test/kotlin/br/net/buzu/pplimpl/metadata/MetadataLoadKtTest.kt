package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplimpl.jvm.readMetaType
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.Subtype
import br.net.buzu.sample.order.Order
import br.net.buzu.sample.pojo.Person
import br.net.buzu.sample.ppl.Human
import br.net.buzu.sample.ppl.StaticPerson
import br.net.buzu.sample.ppl.Xmen
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MetadataLoadKtTest {


    @Test
    fun testPerson(){
        val person = Person.INSTANCE // Simple Person Class - No Annotations
        val metaType = readMetaType(person.javaClass)
        val metadata = loadMetadata(person, metaType)
        assertMetadata(metadata, "", Subtype.OBJ, 14, 0, 1)
        val children = metadata.children<Metadata>()
        assertMetadata(children[0], "name", Subtype.STRING, 7, 0, 1)
        assertMetadata(children[1], "age", Subtype.INTEGER, 2, 0, 1)
        assertMetadata(children[2], "city", Subtype.STRING, 5, 0, 1)
    }

    @Test
    fun testStaticPerson(){
        val person = StaticPerson.INSTANCE // Annotated Person Class
        val metaType = readMetaType(person.javaClass)
        val metadata = loadMetadata(person, metaType)
        assertMetadata(metadata, "", Subtype.OBJ, 30, 0, 1)
        val children = metadata.children<Metadata>()
        assertMetadata(children[0], "personName", Subtype.CHAR, 10, 0, 1)
        assertMetadata(children[1], "personAge", Subtype.INTEGER, 10, 0, 1)
        assertMetadata(children[2], "personCity", Subtype.CHAR, 10, 0, 1)
    }

    @Test
    fun testHuman(){
        val human = Human.INSTANCE // Annotated Class with static fields
        val metaType = readMetaType(human.javaClass)
        val metadata = loadMetadata(human, metaType)
        assertMetadata(metadata, "", Subtype.OBJ, 30, 0, 1)
        val children = metadata.children<Metadata>()
        assertMetadata(children[0], "fullName", Subtype.CHAR, 10, 0, 1)
        assertMetadata(children[1], "fixed-field", Subtype.CHAR, 5, 0, 1)
        assertMetadata(children[2], "weight", Subtype.NUMBER, 5, 0, 1)
        assertMetadata(children[3], "birthDay", Subtype.ISO_DATE, 10, 0, 1)
    }

    @Test
    fun testXmen(){
        val wolverine = Xmen.INSTANCE // extended class (:Human)
        val metaType = readMetaType(wolverine.javaClass)
        val metadata = loadMetadata(wolverine, metaType)
        assertMetadata(metadata, "", Subtype.OBJ, 50, 0, 1)
        val children = metadata.children<Metadata>()
        assertMetadata(children[0], "fullName", Subtype.CHAR, 10, 0, 1)
        assertMetadata(children[1], "fixed-field", Subtype.CHAR, 5, 0, 1)
        assertMetadata(children[2], "weight", Subtype.NUMBER, 5, 0, 1)
        assertMetadata(children[3], "birthDay", Subtype.ISO_DATE, 10, 0, 1)
        assertMetadata(children[4], "power", Subtype.STRING, 20, 0, 1)
    }

    @Test
    fun testLoadOrder() {
        val order: Order = Order.INSTANCE
        val typeMapper = readMetaType(order.javaClass)
        val metadataOrder= loadMetadata(order, typeMapper)
        assertMetadata(metadataOrder, "", Subtype.OBJ, 96, 0,1)
        val metadataCustomer = metadataOrder.children<Metadata>()[1]
        assertMetadata(metadataCustomer, "customer", Subtype.OBJ, 53, 0,1)
    }


    fun assertMetadata(metadata: Metadata, name: String, type: Subtype, size: Int =0, minOccurs:Int=0, maxOccurs:Int=1){
        assertEquals(name, metadata.name())
        assertEquals(type, metadata.info().subtype)
        assertEquals(size, metadata.info().size)
        assertEquals(minOccurs, metadata.info().minOccurs)
        assertEquals(maxOccurs, metadata.info().maxOccurs)

    }

}