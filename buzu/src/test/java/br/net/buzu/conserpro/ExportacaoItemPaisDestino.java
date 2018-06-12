package br.net.buzu.conserpro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe que representa o peso e a quantidade que esta sendo representada para
 * cada Pais de destino.
 *
 * @author Rodrigo Adur (099.163.947-25) #21 3509-7369
 * @since 26 de jun de 2016 Construção DUE.
 *
 */
public class ExportacaoItemPaisDestino implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private Date dataregistro;

	private int paisDestino;

	private BigDecimal qtdUnidadeEstatistica;

	private BigDecimal pesoLiquido;

	private List<Certificado> certificados = new ArrayList<Certificado>();

	private Integer tipoCertificado;

	public Date getDataregistro() {
		return dataregistro;
	}

	public void setDataregistro(Date dataregistro) {
		this.dataregistro = dataregistro;
	}

	public int getPaisDestino() {
		return paisDestino;
	}

	public void setPaisDestino(int paisDestino) {
		this.paisDestino = paisDestino;
	}

	public BigDecimal getQtdUnidadeEstatistica() {
		return qtdUnidadeEstatistica;
	}

	public void setQtdUnidadeEstatistica(BigDecimal qtdUnidadeEstatistica) {
		this.qtdUnidadeEstatistica = qtdUnidadeEstatistica;
	}

	public BigDecimal getPesoLiquido() {
		return pesoLiquido;
	}

	public void setPesoLiquido(BigDecimal pesoLiquido) {
		this.pesoLiquido = pesoLiquido;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTipoCertificado() {
		return tipoCertificado;
	}

	public void setTipoCertificado(Integer tipoCertificado) {
		this.tipoCertificado = tipoCertificado;
	}

	public List<Certificado> getCertificados() {
		return certificados;
	}

	public void setCertificados(List<Certificado> certificados) {
		this.certificados = certificados;
	}

}
