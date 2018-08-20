package br.net.buzu.poc;

import com.google.gson.Gson;

import br.net.buzu.Buzu;
import br.net.buzu.context.BasicMetadataFactory;
import br.net.buzu.context.BasicParserFactory;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.poc.model.Request;
import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.PplString;
import br.net.buzu.pplspec.model.StaticMetadata;

public class Poc {

	static final String PARSE = "P";
	static final String SERIALIZE= "S";
	static final String PPL = "PPL";
	static final String JSON = "JSON";
	static final Gson GSON = new Gson();
	static final Buzu BUZU = new Buzu();
	static final Request REQUEST = new Request("addService", "try add 2 + 2", Short.parseShort("30"), 0.3f,
			true);
    static final String REQUEST_JSON = GSON.toJson(REQUEST);
    static final String REQUEST_PPL = BUZU.toPpl(REQUEST);
    static final PplString PPL_STRING = PplString.of(REQUEST_PPL);
    static final StaticMetadata STATIC_METADATA = (StaticMetadata) new BasicMetadataParser().parse(PPL_STRING);
    static final Metaclass METACLASS = new BasicMetaclassReader().read(Request.class);
    static final PayloadParser PARSER = new BasicParserFactory().create(METACLASS);

	public static void main(String[] args) {
		String format = PPL;
		String op = SERIALIZE;
		int count = 1000;
		if (args.length >= 2) {
			op = args[0];
			format = args[1];
			count = Integer.parseInt(args[2]);
		}
		run(format, op, count);
	}

	private static void run(String format, String op, int count) {
		long t0 = System.currentTimeMillis();
		if (PPL.equalsIgnoreCase(format)){
			if (PARSE.equalsIgnoreCase(op)) {
				runParseBuzu(count);
			} else {
				runSerializeBuzu(count);
			}
		} else {
			if (PARSE.equalsIgnoreCase(op)) {
				runParseGson(count);
			} else {
				runSerializeGson(count);
			}			
		}
		long t1 = System.currentTimeMillis();
		System.out.println(op + " " + format + "(" + count + "):" + (t1 - t0));
	}

	private static void runParseGson(int count) {
		for (int i = 0; i < count; i++) {
			GSON.fromJson(REQUEST_JSON, Request.class);
		}
	}

	private static void runSerializeGson(int count) {
		for (int i = 0; i < count; i++) {
			GSON.toJson(REQUEST);
		}
	}
	private static void runParseBuzu(int count) {
		for (int i = 0; i < count; i++) {
			//BUZU.fromPpl(REQUEST_PPL, Request.class);
			BUZU.fromPayload(STATIC_METADATA, PPL_STRING.getPayload(), METACLASS);
		}
	}

	private static void runSerializeBuzu(int count) {
		for (int i = 0; i < count; i++) {
			//BUZU.toPpl(REQUEST);
			BUZU.toPpl(STATIC_METADATA, PPL_STRING.getPayload(), METACLASS);
		}
	}


}
