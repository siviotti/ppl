package br.net.buzu.pplimpl.metadata

import br.net.buzu.model.MetaInfo
import br.net.buzu.model.Metadata
import br.net.buzu.model.Subtype
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*


/**
 * @author Douglas siviotti
 */
class ComplexStaticMetadataTest {


    @Test
    fun testIsStatic() {
        assertTrue(cmm1.isStatic())
    }

    @Test
    fun testMaxSerialSize() {
        assertEquals(80, cmm1.serialMaxSize())
    }

    @Test
    fun testEquals() {
        assertEquals(cmm1, cmm2)
        assertEquals(cmm2, cmm1)
        assertEquals(cmm1, cmm1)
        assertFalse(cmm1 == null)
    }

    @Test
    fun testHascode() {
        assertEquals(cmm1.hashCode(), cmm2.hashCode())
    }

    companion object {

        internal var metaInfo = MetaInfo(31, "color", Subtype.STRING, 10, 0, 1, 2)
        internal var ssm1 = SimpleStaticMetadata(metaInfo)
        internal var ssm2 = SimpleStaticMetadata(metaInfo)
        internal var children: MutableList<Metadata> = ArrayList()
        internal var cmm1: ComplexStaticMetadata
        internal var cmm2: ComplexStaticMetadata

        init {
            children.add(ssm1)
            children.add(ssm2)

            cmm1 = ComplexStaticMetadata(metaInfo, children)
            cmm2 = ComplexStaticMetadata(metaInfo, children)
        }
    }

}
