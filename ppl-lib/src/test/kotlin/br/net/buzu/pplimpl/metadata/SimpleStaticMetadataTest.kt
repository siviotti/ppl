package br.net.buzu.pplimpl.metadata

import br.net.buzu.java.model.MetaInfo
import br.net.buzu.java.model.Subtype
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit Test domainOf SimpleStaticMetadata
 *
 * @author Dougla Siviotti
 */
class SimpleStaticMetadataTest {

    internal var metaInfo = MetaInfo(31, "color", Subtype.STRING, 10, 0, 1, 2)
    internal var ssm1 = SimpleStaticMetadata(metaInfo)
    internal var ssm2 = SimpleStaticMetadata(metaInfo)

    @Test
    fun testIsStatic() {
        assertTrue(ssm1.isStatic())
    }

    @Test
    fun testMaxSerialSize() {
        assertEquals(20, ssm1.serialMaxSize())
    }

    @Test
    fun testEquals() {
        assertEquals(ssm1, ssm2)
        assertEquals(ssm2, ssm1)
        assertEquals(ssm1, ssm1)
        assertFalse(ssm1 == null)
    }

    @Test
    fun testHascode() {
        assertEquals(ssm1.hashCode(), ssm2.hashCode())
    }


}

