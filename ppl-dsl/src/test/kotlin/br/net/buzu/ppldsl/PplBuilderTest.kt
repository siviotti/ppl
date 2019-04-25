package br.net.buzu.ppldsl

import br.net.buzu.pplspec.model.PplNode
import br.net.buzu.pplspec.model.Subtype
import org.junit.jupiter.api.Test

class Person(val name: String,
             val age: Int,
             val friends: List<Person>?)

internal class PplBuilderTest {
    @Test
    fun testPplBuilder() {
        val x = 3
        val metadata = ppl{ listOf(
            PplNode(name = "Name", type = "S"),
            PplNode(name = "Name", type = "S")
        )
        }.asMetadata()
    }
}



