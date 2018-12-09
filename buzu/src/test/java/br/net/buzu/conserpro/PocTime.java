package br.net.buzu.conserpro;

import br.net.buzu.Buzu;
import br.net.buzu.context.BasicParserFactory;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.MetadataBuilder;
import static br.net.buzu.lib.TextKt.*;
import br.net.buzu.pplspec.api.PayloadMapper;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.PplString;
import br.net.buzu.pplspec.model.StaticMetadata;
import br.net.buzu.sample.pojo.Person;
import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author Douglas Siviotti
 * @since 1.0
 */
public class PocTime {

	static Gson gson = new Gson();
	static XStream xStream = getXStream();
	static Buzu buzu = new Buzu();

	public static void main(String[] args) {
		int[] arrayLoop = { 1, 100 };
		round(new Person("Ladybug", 15, "Paris"), arrayLoop);
		round(createEmpresa(), arrayLoop);
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

	private static void calc(List<?> source) {
		long t0, t1 = 0;
		System.out.println("\n\n" + source.get(0).getClass().getSimpleName() + " X " + source.size());
		String empresaXml = xStream.toXML(source);
		String empresaJson = gson.toJson(source, ArrayList.class);
		String empresaPpl1 = buzu.toPpl(source);
		PplString pplString = new PplString(empresaPpl1);
		StaticMetadata metadata = (StaticMetadata) new MetadataBuilder().build(source);
		Metaclass from = new BasicMetaclassReader().read(List.class, source.get(0).getClass());
		PayloadMapper parser = new BasicParserFactory().create(from);
		Metaclass toClass = new BasicMetaclassReader().read(List.class, source.get(0).getClass());
		String empresaPpl2 = pplString.getPayload();
		Object object = source.get(0);

		System.out.println("\nSerialize");

		// Xml
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			xStream.toXML(object);
		}
		t1 = System.currentTimeMillis();
		print("XML  time:" + (t1 - t0));

		// JSon
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			gson.toJson(object);
		}
		t1 = System.currentTimeMillis();
		print("JSON time:" + (t1 - t0));

		// Ppl reCompile
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			buzu.toPpl(object);
		}
		t1 = System.currentTimeMillis();
		print("PPL1 time:" + (t1 - t0));

		// Ppl preCompile
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			parser.serialize(metadata, object, from);
		}
		t1 = System.currentTimeMillis();
		print("PPL2 time:" + (t1 - t0));

		print("\nParse");

		// Xml
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			xStream.fromXML(empresaXml);
		}
		t1 = System.currentTimeMillis();
		print("XML  time:" + (t1 - t0));

		// JSon
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			gson.fromJson(empresaJson, ArrayList.class);
		}
		t1 = System.currentTimeMillis();
		print("JSON time:" + (t1 - t0));

		// Ppl recompile
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			buzu.fromPpl(empresaPpl1, toClass);
			//buzu.fromPpl(empresaPpl1);
		}
		t1 = System.currentTimeMillis();
		print("PPL1 time:" + (t1 - t0));

		// Ppl preComlied
		t0 = System.currentTimeMillis();
		for (int i = 0; i < source.size(); i++) {
			parser.parse(metadata, empresaPpl2, toClass);
		}
		t1 = System.currentTimeMillis();
		print("PPL2 time:" + (t1 - t0));

	}

	private static void print(String s) {
		System.out.println(s);
		System.gc();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static void dump(String format, String text, int size) {
		System.out.println(format + "[" + leftFit("" + text.length(), 8, '0') + "]" + ((size == 1) ? text : ""));
	}

	public static XStream getXStream() {
		XStream xstream = new XStream();
		xstream.alias("empresa", Empresa.class);
		xstream.alias("nj", NaturezaJuridica.class);
		xstream.alias("socio", Socio.class);
		xstream.alias("telefone", Telefone.class);
		xstream.alias("person", Person.class);
		xstream.alias("pessoa", Pessoa.class);
		xstream.alias("bigflat", BigFlat.class);
		xstream.setMode(XStream.NO_REFERENCES);
		return xstream;
	}

	public static Empresa createEmpresa() {
		String EMPRESA_NOME = "Empresa Teste";
		int NJ_CODIGO = 206;
		String EMPRESA_CNPJ = "99999999999999";
		String NJ_NOME = "Sociedade Empresaria Ltda";
		Date WW2 = new Date(new GregorianCalendar(1939, Calendar.SEPTEMBER, 1).getTimeInMillis());

		List<Telefone> telefonesSocio1 = new ArrayList<Telefone>();
		telefonesSocio1.add(new Telefone("99", "11111111"));
		telefonesSocio1.add(new Telefone("88", "22222222"));
		Socio socio1 = new Socio("33333333333", "Socio 01", 30.0, telefonesSocio1, "socio1@teste.com",
				"socio1@hotmail.com");

		List<Telefone> telefonesSocio2 = new ArrayList<Telefone>();
		telefonesSocio2.add(new Telefone("77", "33333333"));
		telefonesSocio2.add(new Telefone("66", "44444444"));
		telefonesSocio2.add(new Telefone("55", "55555555"));
		Socio socio2 = new Socio("22222222222", "Socio 02", 30.0, telefonesSocio2, "socio2@teste.com");

		List<Telefone> telefonesPresidente = new ArrayList<Telefone>();
		telefonesPresidente.add(new Telefone("44", "66666666"));
		telefonesPresidente.add(new Telefone("33", "77777777"));
		telefonesPresidente.add(new Telefone("22", "88888888"));
		Socio presidente = new Socio("11111111111", "President", 30.0, telefonesSocio2, "presidente@teste.com");

		Empresa empresa = new Empresa(EMPRESA_CNPJ, EMPRESA_NOME, WW2, true, new NaturezaJuridica(NJ_CODIGO, NJ_NOME),
				presidente, socio1, socio2);

		empresa.setCapital(new BigDecimal("5000.000"));
		empresa.setMicroEmpresa(false);
		empresa.setDtFechamento(new Date());
		return empresa;
	}

	public static NotaFiscal createNotaFiscal(int itens) {
		NotaFiscal notaFiscal = new NotaFiscal();
		notaFiscal.setId(123);
		notaFiscal.setAnoMes("201712");
		notaFiscal.setCancelada("sim");
		notaFiscal.setChaveAcesso("1234587901234567890123456789012345678901234");
		notaFiscal.setCnf(12345);
		notaFiscal.setCnpjDestinatario("12345678901234");
		notaFiscal.setCnpjEmitente("12345678901234");
		notaFiscal.setCpfDestinatario("11111111111");
		notaFiscal.setDataEmissao(new Date());
		notaFiscal.setDv(34);
		notaFiscal.setEnderecoImportador("endereco teste");
		notaFiscal.setFinalidade("finalidade");
		notaFiscal.setIdentificacaoEstrangeiro("estrang");
		notaFiscal.setEnderecoImportador("endereco importador");
		notaFiscal.setModelo("modelo");
		notaFiscal.setTipoEmissao("tipo emissao");
		notaFiscal.setUfEmissor("RJ");
		ItemNotaFiscal item;
		for (int i = 0; i < itens; i++) {
			item = new ItemNotaFiscal();
			item.setCfop(2365);
			item.setCodigoProduto("cod rpdo " + i);
			item.setDescricao("descricao do produto");
			item.setNcm(45875);
			item.setUnidadeComercial("unidade estatistica");
			item.setUnidadeTributavel("unidade tributavel");

			DetalhamentoItem det1 = new DetalhamentoItem();
			det1.setArmaDescricao("arma de mao");
			det1.setArmaNumeroCano("cano da  ajh akjsdhaksjd arma");
			det1.setArmaNumeroSerie("serisa da arma");

			DetalhamentoItem det2 = new DetalhamentoItem();
			det2.setArmaDescricao("arma de mao");
			det2.setArmaNumeroCano("cano da  arma");
			det2.setArmaNumeroSerie("serisa dalskajlksdj alksa arma");

			DetalhamentoItem det3 = new DetalhamentoItem();
			det2.setArmaDescricao("arma alslakjd lkasjd laksde mao");
			det2.setArmaNumeroCano("cano da  arma");
			det2.setArmaNumeroSerie("serisa  arma");

			item.getDetalhesItem().add(det1);
			item.getDetalhesItem().add(det2);
			item.getDetalhesItem().add(det3);

			notaFiscal.getItems().add(item);

		}
		return notaFiscal;
	}

	public static Due createDue(int itens) {
		Due due = new Due();
		due.setId(123L);
		due.setCpfUsuarioLogado("07311631769");
		due.setDataCriacao(new Date());
		due.setFormaExportacao(567);
		due.setNumero("123456789");

		ItemDue item = null;
		for (int i = 0; i < itens; i++) {
			item = new ItemDue();
			item.setNiExportador("12345678901");
			item.setValorCondicaoVenda(new BigDecimal(345));
			ExportacaoItemPaisDestino exp1 = new ExportacaoItemPaisDestino();
			exp1.setDataregistro(new Date());
			exp1.setPaisDestino(151);
			exp1.setTipoCertificado(3);
			exp1.getCertificados().add(new Certificado(72662542L, "293847293847"));
			exp1.getCertificados().add(new Certificado(7266254223L, "293847293"));
			exp1.getCertificados().add(new Certificado(7266242L, "293847293847223422444"));
			item.getExportacaoPaisDestino().add(exp1);

			ExportacaoItemPaisDestino exp2 = new ExportacaoItemPaisDestino();
			exp2.setDataregistro(new Date());
			exp2.setPaisDestino(151);
			exp2.setTipoCertificado(3);
			exp2.getCertificados().add(new Certificado(72662542L, "293847293847"));
			exp2.getCertificados().add(new Certificado(7266254223L, "293847293"));
			exp2.getCertificados().add(new Certificado(7266242L, "293847293847223422444"));
			item.getExportacaoPaisDestino().add(exp2);
			due.getListaItensDue().add(item);
		}

		NotaFiscal notaFiscal1 = createNotaFiscal(itens);
		notaFiscal1.setChaveAcesso("11111111111111111111111111111111111111111111");
		due.getNotasFiscais().add(notaFiscal1);
		NotaFiscal notaFiscal2 = createNotaFiscal(itens);
		notaFiscal2.setChaveAcesso("22222222222222222222222222222222222222222222");
		due.getNotasFiscais().add(notaFiscal2);
		return due;
	}
}
