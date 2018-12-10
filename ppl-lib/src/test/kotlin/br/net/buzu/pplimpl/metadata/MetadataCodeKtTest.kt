package br.net.buzu.pplimpl.metadata

import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.PplNode
import org.junit.jupiter.api.Test
import java.util.*

internal class MetadataCodeKtTest {

    @Test
    fun metadataAsVerbose() {
        val metadata: Metadata = parseMetadata("", PERSON, 0, createSpecificMetadata)
        println(metadata)
        println(metadataAsVerbose(metadata))
        println(metadataAsNatural(metadata))
        println(metadataAsShort(metadata))
        println(metadataAsCompact(metadata))
    }

    companion object {

        internal var NAME = PplNode("name", "S", "20", "1-1", "")
        internal var AGE = PplNode("age", "I", "2", "", "")
        internal var CITY = PplNode("city", "S", "5", "", "")
        internal var CHILDREN: MutableList<PplNode> = ArrayList()
        internal var EXT = PplNode("color", "S", "10", "0-1", "['black','white','red']", "red", "K I test")
        internal var COMPLEX_EXT = PplNode("color", "S", "10", "0-1", "['1=black','2=white','3=red']", "3",
                "K I test")

        init {
            CHILDREN.add(NAME)
            CHILDREN.add(AGE)
            CHILDREN.add(CITY)
            CHILDREN.add(EXT)
            CHILDREN.add(COMPLEX_EXT)
        }

        internal var PERSON = PplNode("", "", "", "", "", "", "", CHILDREN)
    }

}