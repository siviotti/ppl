package br.net.buzu.conserpro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PJ extends Empresa {
	
	private List<Cnae> cnaes = new ArrayList<Cnae>();
	
	public PJ() {
		super();
	}

	public PJ(String cnpj, String razaoSocial, Date dtAbertura, boolean ativa, NaturezaJuridica nj, BigDecimal capital,
			Date dtFechamento, Boolean microEmpresa, List<Socio> socios) {
		super(cnpj, razaoSocial, dtAbertura, ativa, nj, capital, dtFechamento, microEmpresa, socios);
	}

	public PJ(String cnpj, String razaoSocial, Date dtAbertura, boolean ativa, NaturezaJuridica nj,
			Socio... socioArray) {
		super(cnpj, razaoSocial, dtAbertura, ativa, nj, socioArray);
	}

	public List<Cnae> getCnaes() {
		return cnaes;
	}

	public void setCnaes(List<Cnae> cnaes) {
		this.cnaes = cnaes;
	}

}
