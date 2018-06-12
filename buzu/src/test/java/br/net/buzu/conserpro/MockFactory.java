package br.net.buzu.conserpro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MockFactory {
	
	public static final String EMPRESA_NOME = "Empresa Teste";
	public static final int NJ_CODIGO = 206;
	public  static final String EMPRESA_CNPJ = "99999999999999";
	public  static final String NJ_NOME = "Sociedade Empresaria Ltda";
	public static Date ABERTURA_BB = new Date(new GregorianCalendar(1808, Calendar.OCTOBER, 12).getTimeInMillis());
	// 01/09/1977
	public static Date WW2 = new Date(new GregorianCalendar(1939, Calendar.SEPTEMBER, 1).getTimeInMillis());

	public static Empresa bancoDoBrasil() {
		List<Telefone> telefonesDaoJoao = new ArrayList<Telefone>();
		telefonesDaoJoao.add(new Telefone("21", "11111111"));
		telefonesDaoJoao.add(new Telefone("24", "44444444"));
		Socio daoJoao = new Socio("12345678900", "Dao Joao", 50.0, telefonesDaoJoao, "djoao@castelo.pt", "djoao@paco.gov.br");

		List<Telefone> telefonesCabral = new ArrayList<Telefone>();
		telefonesCabral.add(new Telefone("21", "22222222"));
		telefonesCabral.add(new Telefone("23", "33333333"));
		telefonesCabral.add(new Telefone("23", "55555555"));
		Socio cabral = new Socio("98765432100", "Cabral", 50.0, telefonesCabral, "cabral@nau.pt");


		Empresa empresa = new Empresa("00000000000191", "Banco do Basil", ABERTURA_BB, true,
				new NaturezaJuridica(252, "Financeira"), daoJoao, cabral);
		empresa.setMicroEmpresa(false);
		return empresa;
	}

	
	public static Empresa empresaVazia() {
		List<Telefone> telefonesCabral = new ArrayList<Telefone>();
		telefonesCabral.add(new Telefone("23", "55555555"));
		Socio cabral = new Socio("98765432100", "Cabral", 50.0, telefonesCabral, "teste@teste");

		

		Empresa empresa = new Empresa("00000000000191", "Empresa Vazia", new Date(), true,
				new NaturezaJuridica(252, "Financeira"), cabral);
		
		return empresa;
	}
	
	public static Empresa empresaTeste() {
		List<Telefone> telefonesSocio1 = new ArrayList<Telefone>();
		telefonesSocio1.add(new Telefone("99", "11111111"));
		telefonesSocio1.add(new Telefone("88", "22222222"));
		Socio socio1 = new Socio("33333333333", "Socio 01", 30.0, telefonesSocio1, "socio1@teste.com", "socio1@hotmail.com");

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

		Empresa empresa = new Empresa(EMPRESA_CNPJ, EMPRESA_NOME, WW2, true,
				new NaturezaJuridica(NJ_CODIGO, NJ_NOME), presidente, socio1, socio2);
		
		empresa.setCapital(new BigDecimal("5000.000"));
		empresa.setMicroEmpresa(false);
		empresa.setDtFechamento(new Date());
		return empresa;
	}

	
	public static PJ pjBancoDoBrasil(){
		List<Telefone> telefonesDaoJoao = new ArrayList<Telefone>();
		telefonesDaoJoao.add(new Telefone("21", "11111111"));
		telefonesDaoJoao.add(new Telefone("24", "44444444"));
		Socio daoJoao = new Socio("12345678900", "Dao Joao", 50.0, telefonesDaoJoao, "djoao@castelo.pt", "djoao@paco.gov.br");

		List<Telefone> telefonesCabral = new ArrayList<Telefone>();
		telefonesCabral.add(new Telefone("21", "22222222"));
		telefonesCabral.add(new Telefone("23", "33333333"));
		telefonesCabral.add(new Telefone("23", "55555555"));
		Socio cabral = new Socio("98765432100", "Cabral", 50.0, telefonesCabral, "cabral@nau.pt");

		PJ pj = new PJ("00000000000191", "Banco do Basil", ABERTURA_BB, true,
				new NaturezaJuridica(252, "Financeira"), daoJoao, cabral);
		pj.setMicroEmpresa(false);
		
		List<Cnae> cnaes = new ArrayList<Cnae>();
		cnaes.add(new Cnae(10, "Banco de Dados"));
		cnaes.add(new Cnae(20, "Rede"));
		cnaes.add(new Cnae(30, "Desenvolvimento"));
		
		pj.setCnaes(cnaes);
		
		return pj;		
	}

}
