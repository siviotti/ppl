package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplspec.annotation.PplMetadata
import br.net.buzu.pplspec.exception.PplReflectionException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.Serializable
import java.lang.reflect.Field
import java.util.*
import org.junit.jupiter.api.fail


internal class ReflectKtTest {

    @Test
    fun testIsMultiple() {
        val array = arrayOfNulls<String>(0)
        // Array or Collection
        assertTrue(isMultiple(array.javaClass))
        assertTrue(isMultiple(ArrayList::class.java))
        assertTrue(isMultiple(List::class.java)) // Kotlin List
        assertTrue(isMultiple(java.util.List::class.java)) // Java List
        assertTrue(isMultiple(ArrayList::class.java))
        assertTrue(isMultiple(HashSet::class.java))
        // Single
        val s = "abc"
        assertFalse(isMultiple(s.javaClass))
        assertFalse(isMultiple(String::class.java))
        assertFalse(isMultiple(String::class.java))
        assertFalse(isMultiple(Int::class.java))
    }
    // **************************************************
    // Method Invoke
    // **************************************************

    @Test
    fun testFindGet() {
        val bean = Bean()
        val getName = findGet("name", bean)
        assertEquals("getName", getName.getName())
        val getSize = findGet("size", bean)
        assertEquals("getSize", getSize.getName())
        val isActive = findGet("active", bean)
        assertEquals("isActive", isActive.getName())

        try {
            findGet("color", bean)
            fail("")
        } catch (e: PplReflectionException) {
            assertTrue(e.message!!.contains("getColor"))
        }

    }

    @Test
    fun testFindSet() {
        val bean = Bean()
        val setName = findSet("name", bean, String::class.java)
        assertEquals("setName", setName.getName())
        val setSize = findSet("size", bean, Int::class.java)
        assertEquals("setSize", setSize.getName())

        try {
            findSet("color", bean, String::class.java)
            fail("fail")
        } catch (e: PplReflectionException) {
            assertTrue(e.message!!.contains("setColor"))
        }

    }

    @Test
    fun testGet() {
        val bean = Bean()
        bean.name="Paul"
        bean.size=5
        val s: String = findAndInvokeGet(bean, "name")
        assertEquals("Paul", s)
        val i : Int = findAndInvokeGet(bean, "size")
        assertEquals(5, i.toLong())
        val b : Boolean = findAndInvokeGet(bean, "active")
        assertEquals(false, b)
        try {
            val c: String=findAndInvokeGet(bean, "color")
            fail("fail")
        } catch (e: PplReflectionException) {
            assertTrue(e.message!!.contains("getColor"))
        }

    }

    @Test
    fun testSet() {
        val bean = Bean()
        findAndInvokeSet(bean, "name", String::class.java, "Paul")
        assertEquals("Paul", bean.name)
        findAndInvokeSet(bean, "size", Int::class.java, 5)
        assertEquals(5, bean.size.toLong())

        try {
            findAndInvokeSet(bean, "color", String::class.java, "red")
            fail("fail")
        } catch (e: PplReflectionException) {
            assertTrue(e.message!!.contains("setColor"))
        }

    }

    // **************************************************
    // Fields
    // **************************************************
    @Test
    @Throws(NoSuchFieldException::class, SecurityException::class)
    fun testGetElementType() {
        val field = Elements::class.java.getDeclaredField("field")
        assertEquals(String::class.java, field.type)
        assertEquals(String::class.java, getElementType(field))

        val strings = Elements::class.java.getDeclaredField("strings")
        assertEquals(List::class.java, strings.type)
        assertEquals(String::class.java, getElementType(strings))

        val array = Elements::class.java.getDeclaredField("array")
        assertEquals(Array<String>::class.java, array.type)
        assertEquals(String::class.java, getElementType(strings))

        val list = Elements::class.java.getDeclaredField("list")
        assertEquals(List::class.java, list.type)
        try {
            assertEquals(String::class.java, getElementType(list))
        } catch (e: PplReflectionException) {
            assertTrue(e.message!!.startsWith(UNSAFE_COLLECTION))
        }

    }

    @Test
    fun testGetAllFields() {
        // Sonar/Jacoco Bug
        var jacocoList = if (Bean::class.java.declaredFields.size > 3) 1 else 0
        val jacocoArray = jacocoList

        val beanFieldArray = Bean::class.java.declaredFields
        val beanFieldList = getAllFields(Bean::class.java)
        assertEquals(beanFieldArray.size.toLong(), beanFieldList.size.toLong())
        assertEquals((3 + jacocoList).toLong(), beanFieldList.size.toLong())
        assertEquals((3 + jacocoArray).toLong(), beanFieldArray.size.toLong())

        jacocoList = if (jacocoArray == 0) jacocoList else jacocoList + 1
        val extendedBeanFieldArray = ExtendedBean::class.java.declaredFields
        val extendedBeanFieldLIst = getAllFields(ExtendedBean::class.java)
        assertFalse(extendedBeanFieldArray.size == extendedBeanFieldLIst.size)
        assertEquals((4 + jacocoList).toLong(), extendedBeanFieldLIst.size.toLong())
        assertEquals((1 + jacocoArray).toLong(), extendedBeanFieldArray.size.toLong())

        jacocoList = if (jacocoArray == 0) jacocoList else jacocoList + 1
        val leafBeanFieldArray = LeafBean::class.java.declaredFields
        val leafBeanFieldLIst = getAllFields(LeafBean::class.java)
        assertFalse(leafBeanFieldArray.size == leafBeanFieldLIst.size)
        assertEquals((5 + jacocoList).toLong(), leafBeanFieldLIst.size.toLong())
        assertEquals((1 + jacocoArray).toLong(), leafBeanFieldArray.size.toLong())
    }

    @Test
    fun testGetPplMetadata() {
        val fields = mutableMapOf<String,Field>()
        for (field in MultiConstructor::class.java.declaredFields){
            fields.put(field.name, field)
        }

        val ppl1 = getPplMetadata(fields["s"]!!)
        assertNotNull(ppl1)
        assertEquals("s2", ppl1!!.name)

        val ppl2 = getPplMetadata(fields["leafBean"]!!)
        assertNotNull(ppl2)
        assertEquals("Leaf", ppl2!!.name)

        val ppl3 = getPplMetadata(fields["i"]!!)
        assertTrue(ppl3 == null)

    }

    @Test
    fun testNewInstance() {
        // Implicit COnstructor
        val bean = newInstance(Bean::class.java) as Bean
        assertTrue(bean is Bean)
        // # Constructors - select no parameters
        val m = newInstance(MultiConstructor::class.java) as MultiConstructor
        assertTrue(m is MultiConstructor)
        assertTrue(m.s == "DEFAULT")// Used constructor has 0 parameters
        // List
        val list = newInstance(List::class.java) as List<*>
        assertTrue(list is ArrayList<*>)
        // Set
        val set = newInstance(Set::class.java) as Set<*>
        assertTrue(set is HashSet<*>)
        // Map
        val map = newInstance(Map::class.java) as Map<*, *>
        assertTrue(map is HashMap<*, *>)

        // Explicit Construtor
        val elements = newInstance(Elements::class.java) as Elements
        assertTrue(map is HashMap<*, *>)
        // # Constructors - select 1 parameter
        val m2 = newInstance(MultiConstructorNoDefaultNotSerializable::class.java) as MultiConstructorNoDefaultNotSerializable
        assertTrue(m2 is MultiConstructorNoDefaultNotSerializable)
        assertTrue(m2.s == "ONE-PAR")// Used constructor has 1 parameter
        // # Constructors - select none constructor because is serializable
        val m3 = newInstance(MultiConstructorNoDefaultButSerializable::class.java) as MultiConstructorNoDefaultButSerializable
        assertTrue(m3 is MultiConstructorNoDefaultButSerializable)
        assertTrue(m3.i != 2)// 2 Parameter constructor sets i == 2
        assertTrue("ONE-PAR" != m3.s)// 1 parameter constructor sets s = ONE-PAR

    }
}

// ********** Model Example **********

internal open class Bean {

    var name: String? = null
    var size: Int = 0
    var isActive: Boolean = false
}

internal open class ExtendedBean : Bean() {
    var ext: String? = null
}

@PplMetadata(name = "Leaf")
internal open class LeafBean : ExtendedBean() {
    var leaf: String? = null
}

internal class Elements {
    var field: String? = null
    var strings: List<String> = ArrayList()
    var list: List<String> = listOf()
    var array = arrayOfNulls<String?>(0)

    constructor(array: Array<String?>) : super() {
        this.array = array
    }

    constructor(field: String) : super() {
        this.field = field
    }



}

internal class MultiConstructor @JvmOverloads constructor(@field:PplMetadata(name = "s2")
                                                          val s: String, i: Int = 1) {
    val i: Int
    var leafBean: LeafBean? = null

    init {
        this.i = i + 1
    }

    // THIS CONSTRUCTOR WILL BE CALLED
    constructor() : this("DEFAULT", 0) {}

}

internal class MultiConstructorNoDefaultNotSerializable// Nou used - 2 parameters
(@field:PplMetadata(name = "s2")
 val s: String, i: Int) {
    val i: Int
    var leafBean: LeafBean? = null

    init {
        this.i = 2
    }

    // THIS IS CALLED - less parameters (1)
    constructor(s: String) : this("ONE-PAR", 1) {}

}

internal class MultiConstructorNoDefaultButSerializable// Nou used (2 parameters) because is Serializable
(@field:PplMetadata(name = "s2")
 val s: String, i: Int) : Serializable {
    val i: Int
    var leafBean: LeafBean? = null

    init {
        this.i = 2
    }

    // Not used (1 parameter) because is Serializable
    constructor(s: String) : this("ONE-PAR", 1) {}

    companion object {

        private const val serialVersionUID = 1L
    }

}