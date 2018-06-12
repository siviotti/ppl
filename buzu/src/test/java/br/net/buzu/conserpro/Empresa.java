package br.net.buzu.conserpro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Empresa {
	
	private String cnpj;

	private String razaoSocial;

	private Date dtAbertura;

	private boolean ativa;

	private NaturezaJuridica nj;

	private List<Socio> socios = new Vector<Socio>();

	private BigDecimal capital;

	private Date dtFechamento;

	private Boolean microEmpresa;

	public Empresa() {

	}

	public Empresa(String cnpj, String razaoSocial, Date dtAbertura, boolean ativa, NaturezaJuridica nj,
			BigDecimal capital, Date dtFechamento, Boolean microEmpresa, List<Socio> socios) {
		super();
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.dtAbertura = dtAbertura;
		this.ativa = ativa;
		this.nj = nj;
		this.socios = socios;
		this.capital = capital;
		this.dtFechamento = dtFechamento;
		this.microEmpresa = microEmpresa;
	}

	public Empresa(String cnpj, String razaoSocial, Date dtAbertura, boolean ativa, NaturezaJuridica nj,
			Socio... socioArray) {
		super();
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.dtAbertura = dtAbertura;
		this.ativa = ativa;
		this.nj = nj;
		if (socios != null) {
			for (Socio socio : socioArray) {
				socios.add(socio);
			}
		}
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public Date getDtAbertura() {
		return dtAbertura;
	}

	public void setDtAbertura(Date dtAbertura) {
		this.dtAbertura = dtAbertura;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public NaturezaJuridica getNj() {
		return nj;
	}

	public void setNj(NaturezaJuridica nj) {
		this.nj = nj;
	}

	public List<Socio> getSocios() {
		return socios;
	}

	public void setSocios(List<Socio> socios) {
		this.socios = socios;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public Date getDtFechamento() {
		return dtFechamento;
	}

	public void setDtFechamento(Date dtFechamento) {
		this.dtFechamento = dtFechamento;
	}

	public Boolean getMicroEmpresa() {
		return microEmpresa;
	}

	public void setMicroEmpresa(Boolean microEmpresa) {
		this.microEmpresa = microEmpresa;
	}

	@Override
	public String toString() {
		return cnpj + " " + razaoSocial;
	}

}
