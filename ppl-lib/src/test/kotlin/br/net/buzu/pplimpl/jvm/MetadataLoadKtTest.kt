package br.net.buzu.pplimpl.jvm

import br.net.buzu.lang.METADATA_END
import br.net.buzu.lang.NAME_END
import br.net.buzu.model.Metadata
import br.net.buzu.model.Subtype
import br.net.buzu.pplimpl.metadata.loadMetadata
import br.net.buzu.sample.order.*
import br.net.buzu.sample.pojo.Person
import br.net.buzu.sample.ppl.Human
import br.net.buzu.sample.ppl.StaticPerson
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

internal class MetadataLoadKtTest {



    @Test
    fun testLoadOrder() {
        val order: Order = ORDER_INSTANCE
        val typeMapper = readMetaType(order.javaClass, genericSkip)
        val metadataOrder= loadMetadata(order, typeMapper)
        assertMetadata(metadataOrder, "", Subtype.OBJ, 96, 0,1)
        val metadataCustomer = metadataOrder.children<Metadata>()[1]
        assertMetadata(metadataCustomer, "customer", Subtype.OBJ, 53, 0,1)
        "(" +
                "number:S10;" +
                "customer:(" +
                  "name:S7;" +
                  "addresses:(" +
                  "street:S17;" +
                  "city:S6;" +
                  "zip:S7;" +
                  "elementType:S8)#0-2;" +
                "phones:S8#0-2);" +
                "date:D;" +
                "products:(" +
                  "description:S8;" +
                  "price:N6)#0-5;" +
                "status:S6;" +
                "canceled:B" +
                ")"
    }

    fun assertMetadata(metadata:Metadata, name: String, type: Subtype, size: Int =0, minOccurs:Int=0, maxOccurs:Int=1){
        assertEquals(name, metadata.name())
        assertEquals(type, metadata.info().subtype)
        assertEquals(size, metadata.info().size)
        assertEquals(minOccurs, metadata.info().minOccurs)
        assertEquals(maxOccurs, metadata.info().maxOccurs)

    }



    companion object {


        // *** Person ***
        val PERSON_METADATA_NAMES = arrayOf("name", "age", "city")
        val PERSON_METADATA_TYPES = arrayOf("S", "I", "S")
        val PERSON_NAME = "Ladybug"
        val PERSON_AGE = 15
        val PERSON_CITY = "Paris"

        val PERSON_METADATA_SIZES = intArrayOf(PERSON_NAME.length, PERSON_AGE.toString().length, PERSON_CITY.length)
        // metadata = (name:S7;age:I2;city:S5)
        val PERSON_METADATA = ("(" + PERSON_METADATA_NAMES[0] + NAME_END
                + PERSON_METADATA_TYPES[0] + PERSON_METADATA_SIZES[0] + METADATA_END + PERSON_METADATA_NAMES[1]
                + NAME_END + PERSON_METADATA_TYPES[1] + PERSON_METADATA_SIZES[1] + METADATA_END
                + PERSON_METADATA_NAMES[2] + NAME_END + PERSON_METADATA_TYPES[2] + PERSON_METADATA_SIZES[2] + ")")
        val PERSON_PAYLOAD = "Ladybug15Paris"
        // PPL = (name:S7;age:I2;city:S5)Ladybug15Paris
        val PERSON_PPL_STRING = PERSON_METADATA + PERSON_PAYLOAD

        val PERSON_INSTANCE = Person(PERSON_NAME, PERSON_AGE, PERSON_CITY)
        val STATIC_PERSON_INSTANCE = StaticPerson(PERSON_NAME, PERSON_AGE, PERSON_CITY)

        // *** StaticPerson ***

        private val STATIC_PERSON = "(personName:C10;personAge:I10;personCity:C10)Ladybug   0000000015Paris     "
        val STATIC_PERSON_NAME = "Ladybug   "
        val STATIC_PERSON_AGE = 15
        val STATIC_PERSON_CITY = "Paris     "

        // *** Order ***

        // Customer
        val ADDRESSES: MutableList<Address> = ArrayList()
        val BILING = Address("Champs Elysee 10", "Paris", "75008", AddressType.BILLING)
        val DELIVERY = Address("Baker Street 221b", "London", "NW1 6XE", AddressType.DELIVERY)
        val PHONES: MutableList<String> = ArrayList()
        val PHONE1 = "11111111"
        val PHONE2 = "22222222"
        val CUSTOMER_NAME = "Ladybug"
        val CUSTOMER: Customer
        // Products
        val PRODUCTS: MutableList<Product> = ArrayList()
        val PRODUCT1 = Product("Book", 45.99)
        val PRODUCT2 = Product("Notebook", 1200.00)
        val PRODUCT3 = Product("Clock", 25.52)
        val PRODUCT4 = Product("Software", 0.99)
        val PRODUCT5 = Product("Tablet", 500.00)
        // Order
        val ORDER_NUMBER = "1234567890"
        val ORDER_DATE = LocalDate.of(2017, 11, 30)
        val ORDER_STATUS = Status.OPENED
        val ORDER_CANCELED = false
        val ORDER_INSTANCE: Order
        val ORDER_PPL_STRING = "(number:S10;customer:(name:S7;addresses:(street:S17;city:S6;zip:S7;elementType:S8)#0-2;phones:S8#0-2);date:D;products:(description:S8;price:N6)#0-5;status:S6;canceled:B)1234567890LadybugChamps Elysee 10 Paris 75008  BILLING Baker Street 221bLondonNW1 6XEDELIVERY111111112222222220171130Book    045.99Notebook1200.0Clock   025.52Software000.99Tablet  0500.0OPENEDfalse"

        // *** Human ***
        val HUMAN_NAME = "Logan"
        val HUMAN_BIRTH = LocalDate.of(1882, 4, 5)
        val HUMAN_WEIGHT = 99.9
        val HUMAN_PPL_STRING = "(fixed-field:C3;fullName:C5;weight:N4;birthDay:DI)fixLogan99.91882-04-05"

        val HUMAN = Human(HUMAN_NAME, HUMAN_BIRTH, HUMAN_WEIGHT)

        // *** Xmen ***
        val XMEN_PPL_STRING = "(fixed-field:C3;fullName:C9;weight:N4;birthDay:DI;power:S7)fixWolverine99.01882-04-05healing"

        // *** TimePojo ***
        val YEAR = 2015
        val MONTH = 10
        val DAY = 21
        val HOUR = 15
        val MINUTE = 43
        val SECOND = 27

        val LOCAL_DATE = LocalDate.of(YEAR, MONTH, DAY)
        val LOCAL_TIME = LocalTime.of(HOUR, MINUTE, SECOND)
        val LOCAL_DATE_TIME = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND)
        val OLD_DATE = GregorianCalendar(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND).time

        init {

            // Customer
            ADDRESSES.add(BILING)
            ADDRESSES.add(DELIVERY)
            PHONES.add(PHONE1)
            PHONES.add(PHONE2)
            CUSTOMER = Customer(CUSTOMER_NAME, ADDRESSES, PHONES)

            // Products
            PRODUCTS.add(PRODUCT1)
            PRODUCTS.add(PRODUCT2)
            PRODUCTS.add(PRODUCT3)
            PRODUCTS.add(PRODUCT4)
            PRODUCTS.add(PRODUCT5)

            // Order
            ORDER_INSTANCE = Order(ORDER_NUMBER, CUSTOMER, ORDER_DATE, PRODUCTS, ORDER_STATUS, ORDER_CANCELED)
        }

    }

}