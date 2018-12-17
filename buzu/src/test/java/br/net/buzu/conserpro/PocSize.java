package br.net.buzu.conserpro;

import br.net.buzu.Buzu;
import static br.net.buzu.lib.TextKt.*;
import br.net.buzu.java.model.PplString;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class PocSize {

	static Gson gson = new Gson();
	static XStream xstream = PocTime.getXStream();
	static Buzu buzu = new Buzu();
	static String className;

	public static void main(String[] args) {
		int[] arrayLoop = { 1, 10 };
		round(new Pessoa("Ladybug", 15, "12345678900"), arrayLoop);
		round(new BigFlat("123123123"), arrayLoop);
		round(PocTime.createEmpresa(), arrayLoop);
		round(PocTime.createNotaFiscal(4), arrayLoop);
		round(PocTime.createDue(4), arrayLoop);

	}

	private static void round(Object obj, int... loop) {

		for (int x = 0; x < loop.length; x++) {
			List<Object> list = new ArrayList<Object>();
			for (int i = 0; i < loop[x]; i++) {
				list.add(obj);
			}
			calc(list);
		}
	}

	private static void calc(List<Object> source) {
		className = source.get(0).getClass().getSimpleName();
		System.out.println("\n" + className + " X " + source.size());
		
		
		StringBuilder sb = new StringBuilder();
		String xml = cleanXml(xstream.toXML(getSource(source)));
		sb.append(dump(" XML", xml, source.size())).append(" Reduz   Reduz ").append(s(xml)).append("\n");

		String json = gson.toJson(getSource(source));
		//save(cleanJson(json), "json2");
		sb.append(dump("JSON", json, source.size())).append("  Xml     Json  ").append(s(json)).append("\n");

		String ppl1 = buzu.toPpl(getSource(source));
		sb.append(dump("PPL1", ppl1, source.size())).append(perc("XML", xml, "PPL1", ppl1))
				.append(perc("JSON", json, "PPL1", ppl1)).append(s(ppl1)).append("\n");

		String ppl2 = new PplString(buzu.toPpl(getSource(source))).getPayload();
		sb.append(dump("PPL2", ppl2, source.size())).append(perc("XML", xml, "PPL2", ppl2))
				.append(perc("JSON", json, "PPL2", ppl2)).append(s(ppl2)).append("\n");
		System.out.println(sb.toString());

	}

	private static String s(String s) {
		return "";
	}

	private static Object getSource(List<Object> source) {
		return source.size() == 1 ? source.get(0) : source;
	}

	private static String cleanXml(String xml) {
		String newXml = xml.replaceAll("\\n", "").replaceAll(" ", "").replaceAll("class=\"java.util.Arrays$ArrayList\"",
				"");
		return newXml;
	}

	private static String perc(String other, String otherValue, String ppl, String pplValue) {
		double otherSize = otherValue.length();
		double pplSize = pplValue.length();
		double perc = 100 - ((pplSize / otherSize) * 100);
		DecimalFormat df = new DecimalFormat("#00.00");
		return "[" + df.format(perc) + "%" + "]";
	}

	private static String dump(String format, String text, int size) {
		String sizeStr = leftFit("" + size, 4, '0');
		save(text, "-" + sizeStr + "-"+ "-" + format.trim());
		return format + "[" + leftFit("" + text.length(), 8, '0') + "]";
	}

	private static void save(String text, String format) {
		File file = new File("/home/douglas/poc-" +  className + "-" + format + ".txt");
		// escreve no arquivo
		FileWriter fw;
		try {
			fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
