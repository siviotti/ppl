package br.net.buzu.poc;

import br.net.buzu.Buzu;
import br.net.buzu.BuzuBuilder;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.model.Metadata;
import br.net.buzu.model.PplString;
import br.net.buzu.pplimpl.metadata.GenericMetadataParser;
import br.net.buzu.sample.order.Order;

import static br.net.buzu.pplimpl.metadata.MetadataCodeKt.metadataAsVerbose;

public class PocJavaKotlin {

    public static final String ORDER_PPL_STRING = "(number:S10;customer:(name:S7;addresses:(street:S17;city:S6;zip:S7;elementType:S8)#0-2;phones:S8#0-2);date:D;products:(description:S8;price:N6)#0-5;status:S6;canceled:B)1234567890LadybugChamps Elysee 10 Paris 75008  BILLING Baker Street 221bLondonNW1 6XEDELIVERY111111112222222220171130Book    045.99Notebook1200.0Clock   025.52Software000.99Tablet  0500.0OPENEDfalse";

    public static void main(String[] args) {
        pocCoderr();
    }

    private static void pocCoderr(){
        Buzu buzu = new BuzuBuilder().metadataParser(new GenericMetadataParser()).build();
        Metadata metadata = new BasicMetadataParser().parse(new PplString(ORDER_PPL_STRING));
        long t0 = System.currentTimeMillis();
        for (int i=0; i < 10000000; i++) {
            //VerboseMetadataCoder.INSTANCE.code(metadata);
            metadataAsVerbose(metadata);
            //fastSerialize(metadata,0);

        }
        long t1 = System.currentTimeMillis();
        System.out.println("time:" + (t1-t0));

    }

    private static void pocParser(){
        Buzu buzu = new BuzuBuilder().metadataParser(new GenericMetadataParser()).build();
        //Buzu buzu = new BuzuBuilder().metadataParser(new BasicMetadataParser()).build();
        long t0 = System.currentTimeMillis();
        for (int i=0; i < 10; i++) {
            buzu.fromPpl(ORDER_PPL_STRING, Order.class);
        }
        long t1 = System.currentTimeMillis();
        System.out.println("time:" + (t1-t0));
    }
}