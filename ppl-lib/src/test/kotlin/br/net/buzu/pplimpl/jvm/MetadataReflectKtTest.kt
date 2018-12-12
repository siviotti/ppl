package br.net.buzu.pplimpl.jvm

import br.net.buzu.pplimpl.metadata.CreateMetadata
import br.net.buzu.pplimpl.metadata.createSpecificMetadata
import br.net.buzu.pplimpl.metadata.parseMetadata
import br.net.buzu.pplspec.model.Metadata
import br.net.buzu.pplspec.model.pplStringOf
import br.net.buzu.sample.order.Order
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MetadataReflectKtTest {

    val ORDER_PPL_STRING = "(number:S10;customer:(name:S7;addresses:(street:S17;city:S6;zip:S7;type:S8)#0-2;phones:S8#0-2);date:D;products:(description:S8;price:N6)#0-5;status:S6;canceled:B)1234567890LadybugChamps Elysee 10 Paris 75008  BILLING Baker Street 221bLondonNW1 6XEDELIVERY111111112222222220171130Book    045.99Notebook1200.0Clock   025.52Software000.99Tablet  0500.0OPENEDfalse"
    val createMetadata: CreateMetadata= createSpecificMetadata

    @Test
    fun readMetadata() {
        val metadata: Metadata = readMetadata(Order::class.java, createMetadata)
        //val metadata: Metadata = parseMetadata(pplStringOf(ORDER_PPL_STRING))
        println(metadata.toTree(0))
    }

    @Test
    fun readMetadata1() {
    }
}