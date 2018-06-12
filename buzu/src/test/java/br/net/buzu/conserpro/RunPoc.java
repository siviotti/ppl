package br.net.buzu.conserpro;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

import br.net.buzu.Buzu;
import br.net.buzu.context.BasicParserFactory;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.MetadataBuilder;
import br.net.buzu.parsing.Text;
import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.StaticMetadata;

public class RunPoc {

	static final String OP_SERIALIZE = "S";
	static final String OP_PARSE = "P";

	static final String XML = "XML";
	static final String JSON = "JSON";
	static final String PPL1 = "PPL1";
	static final String PPL2 = "PPL2";

	static final String PESSOA = "P";
	static final String EMPRESA = "E";
	static final String NOTA = "N";
	static final String DUE = "D";
	static final String BIGFLAT = "B";

	static final Map<String, Object> map = new HashMap<String, Object>();
	static long t0 = 0;
	static long t1 = 0;

	static {
		map.put(PESSOA, new Pessoa("Ladybug", 15, "12345678900"));
		map.put(EMPRESA, PocTime.createEmpresa());
		map.put(NOTA, PocTime.createNotaFiscal(4)); // POC = 4
		map.put(DUE, PocTime.createDue(4)); // POC = 4
		map.put(BIGFLAT, new BigFlat("12345678901234567890")); // POC = 4
	}

	public static void main(String[] args) {
		String args0 = OP_PARSE;
		String args2 = "1000";
		for (Entry<String, Object> entry : map.entrySet()) {
			calc(args0, XML, args2, entry.getKey());
			calc(args0, JSON, args2, entry.getKey());
			calc(args0, PPL1, args2, entry.getKey());
			calc(args0, PPL2, args2, entry.getKey());
		}
	}

	/**
	 * @param args
	 *            args[0] = S/P (Serializa/Parse), args[1] = XML/JSON/PPL1/PPL2,
	 *            args[2] = LOOP, args[3] = (P)essoa, (B)igflat, (E)mpresa,
	 *            (N)ota, (D)ue.
	 * @return
	 */
	public static int calc(String... args) {
		String op = args.length > 0 ? args[0] : "S";
		String format = args.length > 0 ? args[1] : "XML";
		int loop = args.length > 0 ? Integer.parseInt(args[2]) : 1000;
		// Classe d objeto usado no teste
		String type = args.length > 0 ? args[3] : "D";

		Object object = map.get(type);

		if (OP_SERIALIZE.equals(op)) {
			if (XML.equals(format)) {
				serializeXml(object, loop);
			} else if (JSON.equals(format)) {
				serializeJson(object, loop);
			} else if (PPL1.equals(format)) {
				serializePpl1(object, loop);
			} else {
				serializePpl2(object, loop);
			}
		} else {
			if (XML.equals(format)) {
				parseXml(object, loop);
			} else if (JSON.equals(format)) {
				parseJson(object, loop);
			} else if (PPL1.equals(format)) {
				parsePpl1(object, loop);
			} else {
				parsePpl2(object, loop);
			}
		}

		return (int) (t1 - t0);
	}

	private static String resume(Object object, int loop) {
		return " (" + object.getClass().getSimpleName() + " X " + loop + ")";
	}

	private static String dif(long t1, long t0) {
		return Text.leftFill("" + (t1 - t0), 6, '0');
	}

	// ********** serialize **********

	private static void serializeXml(Object object, int loop) {
		XStream xstream = PocTime.getXStream();
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			xstream.toXML(object);
		}
		t1 = System.currentTimeMillis();
		System.out.println("serialize  XML:" + dif(t1, t0) + resume(object, loop));
	}

	private static void serializeJson(Object object, int loop) {
		Gson gson = new Gson();
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			gson.toJson(object);
		}
		t1 = System.currentTimeMillis();
		System.out.println("serialize JSON:" + dif(t1, t0) + resume(object, loop));
	}

	private static void serializePpl1(Object object, int loop) {
		Buzu buzu = new Buzu();
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			buzu.toPpl(object);
		}
		t1 = System.currentTimeMillis();
		System.out.println("serialize PPL1:" + dif(t1, t0) + resume(object, loop));
	}

	private static void serializePpl2(Object object, int loop) {
		StaticMetadata metadata = (StaticMetadata) new MetadataBuilder().build(object);
		Metaclass metaclass = new BasicMetaclassReader().read(object.getClass());
		PayloadParser parser = new BasicParserFactory().create(metaclass);
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			parser.serialize(metadata, object, metaclass);
		}
		t1 = System.currentTimeMillis();
		System.out.println("serialize PPL2:" + dif(t1, t0) + resume(object, loop));
	}

	// ********** parse **********

	private static void parseXml(Object object, int loop) {
		XStream xstream = PocTime.getXStream();
		String str = xstream.toXML(object);
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			xstream.fromXML(str);
		}
		t1 = System.currentTimeMillis();
		System.out.println("parse  XML:" + dif(t1, t0) + resume(object, loop));
	}

	private static void parseJson(Object object, int loop) {
		Gson gson = new Gson();
		String str = gson.toJson(object);
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			gson.fromJson(str, object.getClass());
		}
		t1 = System.currentTimeMillis();
		System.out.println("parse JSON:" + dif(t1, t0) + resume(object, loop));
	}

	private static void parsePpl1(Object object, int loop) {
		Buzu buzu = new Buzu();
		String str = buzu.toPpl(object);
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			buzu.fromPpl(str, object.getClass());
		}
		t1 = System.currentTimeMillis();
		System.out.println("parse PPL1:" + dif(t1, t0) + resume(object, loop));
	}

	private static void parsePpl2(Object object, int loop) {
		StaticMetadata metadata = (StaticMetadata) new MetadataBuilder().build(object);
		Metaclass metaclass = new BasicMetaclassReader().read(object.getClass());
		PayloadParser parser = new BasicParserFactory().create( metaclass);
		String str = parser.serialize(metadata, object, metaclass);
		t0 = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			parser.parse(metadata, str, metaclass);
		}
		t1 = System.currentTimeMillis();
		System.out.println("parse PPL2:" + dif(t1, t0) + resume(object, loop));
	}

}
